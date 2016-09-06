import java.nio.file.Paths

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._


/**
  * Created by Nikolay_Vasilishin on 9/6/2016.
  *
  * 1.	Use dataset http://stat-computing.org/dataexpo/2009/the-data.html (year 2007 + dimensions http://stat-computing.org/dataexpo/2009/supplemental-data.html only Airports and Carrier Codes).
    2.	Load data to dataframes (make #1 screenshot with samples from dataframe(s))
    3.	Count total number of flights per carrier in 2007 (make #2 screenshot)
    4.	The total number of flights served in Jun 2007 by NYC (all airports, use join with Airports data and don’t forget make screenshot #3
    5.	Find five most busy airports in US during Jun 01 - Aug 31 (make #4).
    6.	Find the carrier who served the biggest number of flights (make #5)

  */
object FliesAnalyzerJob {
  //TODO change filename
//  val AIRLINES_FILE = "2007.sample.csv"
  val AIRLINES_FILE = "2007.csv"
  val AIRPORTS_FILE = "airports.csv"
  val CARRIERS_FILE = "carriers.csv"

  def main(args: Array[String]): Unit = {
    val (sc, sqlContext) = initContext()
    //TODO change to args(0)
    val path = """D:\repos\my\scala\spark\epam\hw2\src\main\resources\"""

    process(sqlContext, path)
  }

  private def initContext(): (SparkContext, SQLContext) = {
    val conf = new SparkConf()
      .setAppName("Airlines Analyzer Application")
      .setMaster("local[6]")
      .set("spark.driver.allowMultipleContexts" , "true")
    val sparkContext = new SparkContext(conf)

    (sparkContext, new SQLContext(sparkContext))
  }

  def process(sqlContext: SQLContext, path: String) = {
    val (airlines, airports, carriers) = loadDataset(sqlContext, path)

    /**
      * (2)
      */
//    println("="*10 + "airlines")
//    println(airlines.show(10))
//    println("="*10 + "airports")
//    println(airports.show(10))
//    println("="*10 + "carriers")
//    println(carriers.show(10))

    /**
      * (3)
      */
//    val totalFlights = airlines.groupBy("UniqueCarrier").count().orderBy(desc("count"))

    val airlinesMerged = airlines.join(airports,
  airlines("Origin") <=> airports("iata") && airports("city") === "New York"
  || airlines("Dest") <=> airports("iata") && airports("city") === "New York", "right").where("Month = 6").groupBy("city").count().show()

    """
    SELECT airports.city, COUNT(*)
    FROM airlines, airports
    WHERE (airlines.origin = airports.iata OR airlines.dest = airports.iata)
    AND airports.city = 'New York '
    AND airlines.month = 6
    GROUP BY airports.city;
    """

  }


  private def loadDataset(sqlContext: SQLContext, path: String) = {
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