import eu.bitwalker.useragentutils.UserAgent
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Nikolay_Vasilishin on 8/24/2016.
  */
/**
  * IP,175.5,109854
  * acc IE, Mozzila, Other >> stdout
  */
class AccesslogAnalyzer(@transient val sc:SparkContext) extends Serializable {
  val fromLogExtract = """(ip\d+)\s(?:.)\s(?:.)\s\[(?:.*)\]\s\"(?:.*)\"\s(?:\d{3})\s(.*)\s\"(?:.*)\"\s\"(.+)\"""".r;

  val ieAccumulator = sc.accumulator(0, "IE")
  val mozzilaAccumulator = sc.accumulator(0, "Mozzila")
  val otherAccumulator = sc.accumulator(0, "Other")

  def process(file: String) : RDD[(String, Float, Int)] = {
    val fileName = util.Try(file).getOrElse("/tmp/spark/access.log")
    val lines = sc.textFile(fileName).map(line => parseLine(line))

    val statistics = lines.combineByKey(
      (v) => (v, 1),
      (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1),
      (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
    ).map{ case (key, value) => (key, value._1 / value._2.toFloat, value._1) }
    statistics.sortBy(_._3, ascending = false)

    statistics.map(a => a._1 + "," + a._2 + "," + a._3).saveAsTextFile("/tmp/result.csv")
    println(statistics.take(5).toList)
    println(s"IE Browsers: $ieAccumulator\nMozzila Browsers: $mozzilaAccumulator\nOther Browsers: $otherAccumulator")
    statistics
  }

  def parseLine(line : String): (String, Int) = {
    def toInt(s: String): Option[Int] = {
      try {
        Some(s.toInt)
      } catch {
        case e: Exception => None
      }
    }
    val fromLogExtract(ip, bytes, ua) = line
    UserAgent.parseUserAgentString(ua).getBrowser.getGroup.toString.toLowerCase match {
      case "ie" => ieAccumulator.add(1)
      case "firefox" => mozzilaAccumulator.add(1)
      case _ => otherAccumulator.add(1)
    }

    (ip, toInt(bytes).getOrElse(0))
  }

}
