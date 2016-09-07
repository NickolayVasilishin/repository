package com.epam.nv

import java.nio.file.Paths

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by Nikolay_Vasilishin on 9/6/2016.
  *
  * 1.	Use dataset http://stat-computing.org/dataexpo/2009/the-data.html (year 2007 + dimensions http://stat-computing.org/dataexpo/2009/supplemental-data.html only Airports and Carrier Codes).
  * 2.	Load data to dataframes (make #1 screenshot with samples from dataframe(s))
  * 3.	Count total number of flights per carrier in 2007 (make #2 screenshot)
  * 4.	The total number of flights served in Jun 2007 by NYC (all airports, use join with Airports data and don’t forget make screenshot #3
  * 5.	Find five most busy airports in US during Jun 01 - Aug 31 (make #4).
  * 6.	Find the carrier who served the biggest number of flights (make #5)
  *
  */
object FliesAnalyzerJob {
  val AIRLINES_FILE = "2007.csv.bz2"
  val AIRPORTS_FILE = "airports.csv"
  val CARRIERS_FILE = "carriers.csv"

  def main(args: Array[String]): Unit = {
    val (sc, sqlContext) = initContext()
    val path = """C:\Users\Nikolay_Vasilishin\Downloads"""

    process(sqlContext, path)
  }

  private def initContext(): (SparkContext, SQLContext) = {
    val conf = new SparkConf()
      .setAppName("Airlines Analyzer Application")
       .setMaster("local[8]")
      .set("spark.driver.allowMultipleContexts", "true")
    val sparkContext = new SparkContext(conf)

    (sparkContext, new SQLContext(sparkContext))
  }

  def process(sqlContext: SQLContext, path: String) = {
    val (airlines, airports, carriers) = loadDataset(sqlContext, path)
    /**
      * (2)
      */
//    sampleShow(airlines, airports, carriers)

    /**
      * (3)
      */
//    countTotalFlights(airlines).show()

    /**
      * (4)
      */
    countNewYorkFlights(airlines, airports).show()

    /**
      * (5)
      */
//    getBusyAirports(airlines, airports).show()
    /**
      * (6)
      */
//    getSuperCarrier(airlines, carriers).show()
  }

  def countTotalFlights(airlines: DataFrame): DataFrame = {
    airlines
      .groupBy("UniqueCarrier")
      .count()
      .orderBy(desc("count"))
  }

  def countNewYorkFlights(airlines: DataFrame, airports: DataFrame): DataFrame = {
    airlines.join(airports,
      airlines("Origin") <=> airports("iata") && airports("city") === "New York"
        || airlines("Dest") <=> airports("iata") && airports("city") === "New York", "right")
      .where("Month = 6")
      .groupBy("city")
      .count()
  }

  def getBusyAirports(airlines: DataFrame, airports: DataFrame): DataFrame = {
    airlines.join(airports,
      airlines("Origin") <=> airports("iata")
        || airlines("Dest") <=> airports("iata"), "left")
      .where("Month BETWEEN 6 AND 8")
      .where("country = \"USA\"")
      .groupBy("airport")
      .count()
      .orderBy(desc("count"))
      .limit(5)
  }

  def getSuperCarrier(airlines: DataFrame, carriers: DataFrame): DataFrame = {
    airlines.join(carriers,
    airlines("UniqueCarrier") <=> carriers("Code"),
    "left")
      .groupBy("UniqueCarrier")
      .count()
      .orderBy(desc("count"))
      .limit(1)
  }

  def sampleShow(airlines: DataFrame, airports: DataFrame, carriers: DataFrame): Unit = {
    println("=" * 10 + "airlines")
    airlines.show(10)
    println("=" * 10 + "airports")
    airports.show(10)
    println("=" * 10 + "carriers")
    carriers.show(10)
  }

  def loadDataset(sqlContext: SQLContext, path: String) = {
    val airlines = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(Paths.get(path, AIRLINES_FILE).toString)
    val airports = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(Paths.get(path, AIRPORTS_FILE).toString)
    val carriers = sqlContext.read
      .format("com.databricks.spark.csv")
      .option("header", "true") // Use first line of all files as header
      .option("inferSchema", "true") // Automatically infer data types
      .load(Paths.get(path, CARRIERS_FILE).toString)

    (airlines, airports, carriers)
  }
}