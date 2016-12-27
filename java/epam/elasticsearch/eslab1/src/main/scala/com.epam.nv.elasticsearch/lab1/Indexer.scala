package com.epam.nv.elasticsearch.lab1

import com.epam.nv.elasticsearch.Client.client
import com.epam.nv.elasticsearch.XmlParser.getXmlStreamFromFile
import com.epam.nv.elasticsearch.mapping.ProductRecordRoot
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder
import org.elasticsearch.action.admin.indices.delete.{DeleteIndexRequest, DeleteIndexResponse}
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.index.query.QueryBuilders

import scala.util.{Failure, Try}
/**
  * Created by Nikolay_Vasilishin on 12/26/2016.
  */
object Indexer {

  val MAPPING: String = "product"
  val INDEX: String = "products"
//  val client: TransportClient = client

  def main(args: Array[String]): Unit = {
    //    val products = getXmlStreamFromZip("src/resources/20151101productsActive.xml.zip")
    val products = getXmlStreamFromFile("src/resources/products_0013_4290850_to_4448547.xml")

    Try(deleteIndex()) match {
      case Failure(_) => println("No such index to delete: " + INDEX)
      case _ => None
    }
    createIndexWithMapping()

    indexDocuments(products)

    //    val searchResponse = search("regularPrice", "13.99")
    //    deleteDocuments(searchResponse)
  }

  def deleteDocuments(searchResponse: SearchResponse): Unit = {
    val deleteResponse: Array[DeleteResponse] = searchResponse.getHits.getHits.map { hit =>
      client.prepareDelete(INDEX, MAPPING, s"${hit.getId}").get()
    }
    deleteResponse.foreach(println)
    println(s"Total deleted: ${deleteResponse.count(_.getResult.name() == "DELETED")}")
  }

  def search(term: String, value: String): SearchResponse = {
    val searchResponse: SearchResponse = client.prepareSearch(INDEX).setQuery(QueryBuilders.termQuery(term, value)).get()
    println("Result of the query: " + searchResponse.getHits.getHits.map(_.getId).mkString(";"))
    searchResponse
  }

  def deleteIndex(): Unit = {
    val deleteIndexResponse: DeleteIndexResponse = client.admin().indices().delete(new DeleteIndexRequest(INDEX)).actionGet()
    val deletionStatus: String = if (deleteIndexResponse.isAcknowledged) s"Deleted index: $INDEX" else "Nothing was deleted"
    println(deletionStatus)
  }

  def indexDocuments(products: Seq[ProductRecordRoot]): Seq[IndexResponse] = {
    val responses: Seq[IndexResponse] = products.map(_.toMap)
      .map { product =>
        client.prepareIndex(INDEX, MAPPING, product.get("productId").toString).setSource(product).get()
      }
//    val notCreatedCount = responses.count(_.status().name() != "CREATED")
//    println(notCreatedCount)
    responses
  }

  def createIndexWithMapping(): CreateIndexRequestBuilder = {
    client.admin()
      .indices()
      .prepareCreate(INDEX)
      .setSettings("{\n  \"index.mapper.dynamic\":false \n}")
      .addMapping(MAPPING, ProductRecordRoot.jsonMapping)
  }

}
