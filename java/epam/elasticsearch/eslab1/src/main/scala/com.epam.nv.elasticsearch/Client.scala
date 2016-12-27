package com.epam.nv.elasticsearch

import java.net.InetAddress

import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient

/**
  * Created by Nikolay_Vasilishin on 12/26/2016.
  */
object Client {
  val CLUSTER_NAME: String = "cluster"
  val HOSTNAME: String = "localhost"

  lazy val client = getClient(CLUSTER_NAME, HOSTNAME)

  def getClient(clusterName: String, hostName: String) = {
    val settings: Settings = Settings.builder()
      .put("cluster.name", CLUSTER_NAME).build()
    val client: TransportClient = new PreBuiltTransportClient(settings)
      .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOSTNAME), 9300))

    client
  }
}

