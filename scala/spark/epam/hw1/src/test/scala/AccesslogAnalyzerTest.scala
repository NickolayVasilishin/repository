import java.nio.file.{Files, Paths}

import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.FunSuite

/**
  * Created by Nikolay_Vasilishin on 8/24/2016.
  */
class AccesslogAnalyzerTest extends FunSuite with SharedSparkContext {

  test("Test log entry parser") {
    val userAgent = "ip1 - - [24/Apr/2011:04:10:19 -0400] \"GET /~strabal/grease/photo1/97-13.jpg HTTP/1.1\" 200 56928 \"-\" \"Mozilla/5.0 (compatible; YandexImages/3.0; +http://yandex.com/bots)\""
    assertResult(("ip1", 56928))(new AccesslogAnalyzer(sc).parseLine(userAgent))
  }

  test("Test accesslog analyzer") {
    try {
      Files.delete(Paths.get("/tmp/result.csv"))
    } catch {
      case e: Exception => None
    }
    val rdd = new AccesslogAnalyzer(sc).process("""D:\repos\my\scala\spark\epam\hw1\src\test\resources\access.log""")
    assertResult(List(("ip1", 36302.75,145211), ("ip2", 14917.0,14917)))(rdd.collect().toList)
  }


}