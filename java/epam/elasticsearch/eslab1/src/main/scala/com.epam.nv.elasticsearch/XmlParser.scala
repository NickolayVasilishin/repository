package com.epam.nv.elasticsearch

import java.io.File
import java.net.InetAddress
import javax.xml.bind.{JAXBContext, Unmarshaller}

import com.epam.nv.elasticsearch.mapping.ProductRecordRoot.Products
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder
import org.elasticsearch.action.admin.indices.delete.{DeleteIndexRequest, DeleteIndexResponse}
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.transport.client.PreBuiltTransportClient

import scala.collection.mutable
import com.epam.nv.elasticsearch.mapping.ProductRecordRoot

import scala.collection.JavaConverters._

/**
  * Created by Nikolay_Vasilishin on 12/23/2016.
  */
object XmlParser {

  val xmlMappingClass: Class[Products] = classOf[Products]
  val context: JAXBContext = JAXBContext.newInstance(xmlMappingClass)
  val m: Unmarshaller = context.createUnmarshaller
  val CLUSTER_NAME: String = "cluster"
  val HOSTNAME: String = "localhost"
  val MAPPING: String = "product"
  val INDEX: String = "products"

  def getXmlStreamFromFile(file: String) = {
    m.unmarshal(new File(file)).asInstanceOf[Products].productList.asScala
  }

  def getXmlStreamFromZip(zip: File): Stream[Products] = {
    val rootzip = new java.util.zip.ZipFile(zip)
    import scala.collection.JavaConversions._
    rootzip.entries.toStream
      .filter {
        _.getName.endsWith(".xml")
      }
      .map { zipEntry => rootzip.getInputStream(zipEntry) }
      .map {
        m.unmarshal(_).asInstanceOf[Products]
      }
  }

  def getClient(clusterName: String, hostName: String) = {
    val settings: Settings = Settings.builder()
      .put("cluster.name", CLUSTER_NAME).build()
    val client: TransportClient = new PreBuiltTransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOSTNAME), 9300))
    client
  }


  def main(args: Array[String]): Unit = {
    val products = getXmlStreamFromFile("src/resources/" + INDEX + "_0001_43900_to_1063519.xml")
    val client: TransportClient = getClient(CLUSTER_NAME, HOSTNAME)

    deleteIndex(client)
    createIndexWithMapping(client)
    val responses: Seq[IndexResponse] = indexDocuments(client, products)

    val searchResponse: SearchResponse = client.prepareSearch(INDEX).setQuery(QueryBuilders.termQuery("regularPrice", "13.99")).get()
    println("Result of the query: " + searchResponse.getHits.getHits.map(_.getId).mkString(";"))

    val deleteResponse: Array[DeleteResponse] = searchResponse.getHits.getHits.map { hit =>
      client.prepareDelete(INDEX, MAPPING, s"${hit.getId}").get()
    }

    deleteResponse.foreach(println)
    println(s"Total deleted: ${deleteResponse.count(_.getResult.name() == "DELETED")}")
  }

  def deleteIndex(client: TransportClient): Unit = {
    val deleteIndexResponse: DeleteIndexResponse = client.admin().indices().delete(new DeleteIndexRequest(INDEX)).actionGet()
    val deletionStatus: String = if (deleteIndexResponse.isAcknowledged) s"Deleted index: $INDEX" else "Nothing was deleted"
    println(deletionStatus)
  }

  def indexDocuments(client: TransportClient, products: mutable.Buffer[ProductRecordRoot]): Seq[IndexResponse] = {
    val responses: Seq[IndexResponse] = products.map(_.toMap)
      .map { product =>
        client.prepareIndex(INDEX, MAPPING, product.get("productId").toString).setSource(product).get()
      }
    val notCreatedCount = responses.count(_.status().name() != "CREATED")
    println(notCreatedCount)
    responses
  }

  def createIndexWithMapping(client: TransportClient): CreateIndexRequestBuilder = {
    client.admin()
      .indices()
      .prepareCreate(INDEX)
      .setSettings("{\n  \"index.mapper.dynamic\":false \n}")
      .addMapping(MAPPING, ProductRecordRoot.jsonMapping)
  }
}
