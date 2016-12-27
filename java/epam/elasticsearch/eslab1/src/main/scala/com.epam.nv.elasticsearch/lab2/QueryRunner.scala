package com.epam.nv.elasticsearch.lab2

import com.epam.nv.elasticsearch.Client._
import com.epam.nv.elasticsearch.lab1.Indexer.INDEX
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction
import org.elasticsearch.index.query.functionscore.{FieldValueFactorFunctionBuilder, ScoreFunctionBuilders}
import org.elasticsearch.index.query.{BoolQueryBuilder, QueryBuilder, QueryBuilders}

/**
  * Write a query that limits all the searches only to given product category. Lets say that user tries
  * to search for a cell phone – limit the results accordingly (watch out for the quirks in the data that
  * make it not so easy as it sounds).
  * *
  *
  * LG, Samsung, Lenovo and Huawei have paid to promote their products in the store for every
  * search performed. Add this campaign to the query.
  * *
  *
  * Users sometimes might want to search not only on the names of the products but also on the
  * descriptions as well. Make this change in the query you are creating.
  * *
  *
  * User likes some colors more than the others. The best colors for the cellphones are white and
  *gold. The black color is undesirable. Add this preferences to the query.
  * *
  *
  * Now we have all the requirements covered. Lets make a nod to the customers and promote the
  * cellphones with the high & reliable reviews that have a reasonable price.
  */
object QueryRunner {
  val category: String = ""
  def main(args: Array[String]): Unit = {
    /**
      * 1
      */
//    execute(categoryQuery())

    /**
      * 2
      */
    execute(payedCategoryQuery())

    /**
      * 3
      */

  }

  def execute(query: QueryBuilder) = {
    val searchResponse: SearchResponse = client.prepareSearch(INDEX)
      .setQuery(query)
      .get()

    println(query)
    println("Result IDs of the query: " + searchResponse.getHits.getHits.map(_.getId).mkString("; "))
    searchResponse
  }

  def categoryQuery() = {
    //    val searchResponse: SearchResponse = client.prepareSearch(INDEX).setQuery(QueryBuilders.matchPhrasePrefixQuery("categories", "cell phones")).get()

    val query: BoolQueryBuilder = QueryBuilders.boolQuery()
      .must(QueryBuilders.matchPhraseQuery("categories", "cell phones"))
      .must(QueryBuilders.matchPhraseQuery("productTemplate", "Cell_Phones"))

    query
  }

  def payedCategoryQuery() = {
    categoryQuery()
      .should(QueryBuilders.termsQuery("manufacturer", "LG", "Samsung", "Lenovo", "Huawei").boost(2))
  }

  def commentsAndCategoryQuery(comment: String) = {
    payedCategoryQuery()
      .should(QueryBuilders.termsQuery("longDescription", comment).boost(4))
  }

  def colorsAndCategoryQuery() = {
    payedCategoryQuery()
      .should(QueryBuilders.termsQuery("features", "white", "gold"))
      .mustNot(QueryBuilders.termsQuery("features", "black"))
  }

  def scoredCategoryQuery() = {
    val factor: FieldValueFactorFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction("customerReviewCount").modifier(FieldValueFactorFunction.Modifier.LOG1P).factor(2.0F)
//    val functionScoreQuery: FunctionScoreQueryBuilder = QueryBuilders.functionScoreQuery(colorsAndCategoryQuery().should(QueryBuilders.termsQuery("customerTopRated", "true")),
//      factor)
//    functionScoreQuery.scoreMode(FiltersFunctionScoreQuery.ScoreMode.MULTIPLY)
//    functionScoreQuery.boostMode(CombineFunction.MULTIPLY)
  }
}
