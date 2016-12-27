package com.epam.nv.elasticsearch

import java.io.File
import javax.xml.bind.{JAXBContext, Unmarshaller}

import com.epam.nv.elasticsearch.mapping.ProductRecordRoot
import com.epam.nv.elasticsearch.mapping.ProductRecordRoot.Products

import scala.collection.JavaConverters._

/**
  * Created by Nikolay_Vasilishin on 12/23/2016.
  */
object XmlParser {

  val context: JAXBContext = JAXBContext.newInstance(classOf[Products])
  val m: Unmarshaller = context.createUnmarshaller

  def getXmlStreamFromFile(file: String): Seq[ProductRecordRoot] = {
    m.unmarshal(new File(file)).asInstanceOf[Products].productList.asScala
  }

  def getXmlStreamFromZip(zip: String): Seq[ProductRecordRoot] = {
    val rootzip = new java.util.zip.ZipFile(zip)
    import scala.collection.JavaConversions._
    rootzip.entries
      .filter {
        _.getName.endsWith(".xml")
      }
      .map { zipEntry => rootzip.getInputStream(zipEntry) }
      .map {
        m.unmarshal(_).asInstanceOf[Products]
      }.flatMap(_.productList).toSeq
    //consider to add some laziness, e.g. via yield
  }




}
