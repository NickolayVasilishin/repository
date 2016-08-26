import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Nikolay_Vasilishin on 8/25/2016.
  */
object SparkJob {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("Access-log Analyzer Application")
//      .setMaster("local")
      .set( "spark.driver.allowMultipleContexts" , "true")
    val sc = new SparkContext(conf)

    new AccesslogAnalyzer(sc).process(args(0))
  }
}
