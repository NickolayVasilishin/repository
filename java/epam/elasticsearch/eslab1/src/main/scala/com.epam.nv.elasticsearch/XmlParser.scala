package com.epam.nv.elasticsearch
import java.io.File
//import javax.xml.bind.{JAXBContext, Unmarshaller}
//import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}
import scala.xml.XML
import scala.collection.JavaConverters._

import com.epam.nv.elasticsearch.mapping.ProductRecordRoot
/**
  * Created by Nikolay_Vasilishin on 12/23/2016.
  */
object XmlParser {

  val context: JAXBContext  = JAXBContext.newInstance(classOf[ProductRecordRoot])
  val m: Unmarshaller  = context.createUnmarshaller

  def getXmlStreamFromFile(file: String) = {
    m.unmarshal(new File(file)).asInstanceOf[ProductRecordRoot]
  }

  def getXmlStreamFromZip(zip: File): Stream[ProductRecordRoot] = {
    val rootzip = new java.util.zip.ZipFile(zip)
    import scala.collection.JavaConversions._
    rootzip.entries.toStream
      .filter{ _.getName.endsWith(".xml") }
      .map { zipEntry => rootzip.getInputStream(zipEntry) }
      .map { m.unmarshal(_).asInstanceOf[ProductRecordRoot] }
  }

  def main(args: Array[String]): Unit = {
    val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    val db: DocumentBuilder  = dbf.newDocumentBuilder()

    db.parse("src/resources/products_0001_43900_to_1063518.xml").normalizeDocument()
//    println(m.getSchema)
//    getXmlStreamFromZip(new File("src/resources/20151101productsActive.xml.zip"))
      println(getXmlStreamFromFile("src/resources/products_0001_43900_to_1063518.xml"))
//        .toMap
  }

}
