import java.net.{ServerSocket, SocketAddress}

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{Duration, Milliseconds, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Nikolay_Vasilishin on 9/14/2016.
  */
object StreamingJob {

  def main(args: Array[String]): Unit = {
    TextToSocketProducer.produceInBackground()
    val (sc, sqlc, strc) = initContext()
    strc.checkpoint("chckpt")
    val wordCounts = strc.socketTextStream("localhost", 10000)
      .flatMap(_.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
    val stateful = wordCounts
      .updateStateByKey(updateFunction)
    //      .reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(6), Seconds(2))
    strc.start()
    strc.awaitTerminationOrTimeout(10000)
    println("*" * 40 + "Stopped sleeping")
    strc.stop()
  }

  def updateFunction(newValues: Seq[(Int)], runningCount: Option[(Int)]): Option[(Int)] = {

    var result: Option[(Int)] = null
    if (newValues.isEmpty) {
      //check if the key is present in new batch if not then return the old values
      result = Some(runningCount.get)
    }
    else {
      newValues.foreach { x => {
        // if we have keys in new batch ,iterate over them and add it
        if (runningCount.isEmpty) {
          result = Some(x) // if no previous value return the new one
        } else {
          result = Some(x + runningCount.get) // update and return the value
        }
      }
      }
    }
    result
  }

  private def initContext(duration: Duration = Milliseconds(2000)): (SparkContext, SQLContext, StreamingContext) = {
    val conf = new SparkConf()
      .setAppName("Streaming Application")
      .setMaster("local[*]")
    val sparkContext = new SparkContext(conf)

    (sparkContext, new SQLContext(sparkContext), new StreamingContext(sparkContext, duration))
  }
}
