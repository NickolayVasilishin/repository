package com.epam.nv.elasticsearch

import java.io.File
import java.net.InetAddress
import javax.xml.bind.{JAXBContext, Unmarshaller}
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}

import com.epam.nv.elasticsearch.mapping.ProductRecordRoot.Products
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
//import javax.xml.bind.{JAXBContext, Unmarshaller}
//import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}
import scala.collection.JavaConversions
import scala.collection.JavaConverters._

import com.epam.nv.elasticsearch.mapping.ProductRecordRoot

/**
  * Created by Nikolay_Vasilishin on 12/23/2016.
  */
object XmlParser {

  val context: JAXBContext = JAXBContext.newInstance(classOf[Products])
  val m: Unmarshaller = context.createUnmarshaller

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
      .put("cluster.name", "cluster").build()
    val client: TransportClient = new PreBuiltTransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
    client
  }

  def main(args: Array[String]): Unit = {
    val products = getXmlStreamFromFile("src/resources/products_0001_43900_to_1063519.xml")

    val client: TransportClient = getClient("cluster", "localhost")

    client.admin().indices().prepareCreate("products").addMapping("product", "")
    val responses: Seq[IndexResponse] = products.map(_.toMap)
      .map { product =>
      client.prepareIndex("products", "product", product.get("productId").toString).setSource(product).get()
    }

    println(responses.head.status())
  }

}
