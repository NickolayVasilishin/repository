package com.epam.nv.elasticsearch.lab3

import com.epam.nv.elasticsearch.Client._
import com.epam.nv.elasticsearch.lab1.Indexer.INDEX
import org.elasticsearch.action.search.{SearchRequestBuilder, SearchResponse}
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders
import org.elasticsearch.search.aggregations.{Aggregation, AggregationBuilder, AggregationBuilders}

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
//    println(execute(aggregateByCost()).getHits.getHits.map(_.getId).mkString("; "))

//    println(execute(aggregateByDate()).getHits.getHits.map(_.getId).mkString("; "))

    //    println(executeWithQuery(queryManufacturers(), aggregateByDate()).getHits.getHits.map(_.getId).mkString("; "))

        println(execute(movingAverage()).getHits.getHits.map(_.getId).mkString("; "))
  }

  def executeWithQuery(query: QueryBuilder, aggregation: AggregationBuilder) = {
    val aggregationQuery: SearchRequestBuilder = client.prepareSearch(INDEX)
      .setQuery(query)
      .addAggregation(aggregation)

    println(aggregationQuery)

    val get: SearchResponse = aggregationQuery
      .execute()
      .actionGet()


    get
  }

  def execute(aggregation: AggregationBuilder) = {
    val aggregationQuery: SearchRequestBuilder = client.prepareSearch(INDEX).setSize(0)
      .addAggregation(aggregation)

    println(aggregationQuery)

    val get: SearchResponse = aggregationQuery
      .execute()
      .actionGet()


    get
  }

  def aggregateByCost() = {
    AggregationBuilders.range("prices")
      .field("salePrice")
      .addUnboundedTo("Cheap", 10.0)
      .addRange("Middle", 10, 50)
      .addUnboundedFrom("Expensive", 50)
      .subAggregation(AggregationBuilders.avg("avg_rate").field("customerReviewAverage"))
  }

  def aggregateByDate() = {
    val cost: RangeAggregationBuilder = aggregateByCost()

    AggregationBuilders
      .dateHistogram("agg")
      .field("startDate")
      .dateHistogramInterval(DateHistogramInterval.YEAR)
      .subAggregation(cost)
  }

  def queryManufacturers() = {
    QueryBuilders.termsQuery("manufacturer", "Samsung", "Lenovo")
  }

  def movingAverage() = {
    aggregateByDate()
      .subAggregation(PipelineAggregatorBuilders.movingAvg("Moving Average", "prices>avg_rate"))
  }

}
