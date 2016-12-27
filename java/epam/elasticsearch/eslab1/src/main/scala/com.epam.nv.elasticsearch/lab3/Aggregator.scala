package com.epam.nv.elasticsearch.lab3

import com.epam.nv.elasticsearch.Client._
import com.epam.nv.elasticsearch.lab1.Indexer.INDEX
import org.elasticsearch.search.aggregations.{Aggregation, AggregationBuilder}

/**
  * Analytics
  * *
  *
  * Create an aggregation that checks the user review statistics for three price ranges of the
  * products. The ranges are below $10, between $10 and $50, and above $50. The statistic we are
  * interested in is average customer review of the products in these price ranges.
  * *
  *
  * The query from the previous task analyzed the whole timeline of our products. Using the initial
  * product publication date split the review statistics into the quarterly buckets.
  * *
  *
  * Prepare brand-specific analysis of our products. Select 2 brands to monitor (e.g. Samsung and
  * Lenovo) and prepare the dedicated quarterly user opinion statistics for them.
  * *
  *
  * Add the moving average, first and second derivative of the user opinion to the user review
  * statistics and also to the brand-specific ones.
  */
object Aggregator {

  def main(args: Array[String]): Unit = {
    aggregate()
  }

  def execute(aggregation: AggregationBuilder) = {
    client.prepareSearch(INDEX)
      .addAggregation(aggregation)
      .execute()
      .actionGet()
  }

  def aggregate() = {

  }
}
