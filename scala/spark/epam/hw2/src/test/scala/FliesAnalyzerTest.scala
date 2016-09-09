import java.nio.file.Paths

import com.epam.nv.FliesAnalyzerJob._
import com.holdenkarau.spark.testing.SharedSparkContext
import org.apache.spark.sql.SQLContext
import org.scalatest.FunSuite
import org.apache.spark.sql.Row
/**
  * Created by Nikolay_Vasilishin on 9/6/2016.
  */
class FliesAnalyzerTest extends FunSuite with SharedSparkContext{
  val basePath = """src\test\resources"""

  test("Total Flights Test") {
    val (airlines, _, _) = loadDataset(new SQLContext(sc), Paths.get(basePath, "Total Flights Test").toString)
    val df = countTotalFlights(airlines).collect()

    assertResult(Row("WN",4))(df(0))
  }

  test("Busy Airports Test") {
    val (airlines, airports, _) = loadDataset(new SQLContext(sc), Paths.get(basePath, "Busy Airports Test").toString)
    val df = getBusyAirports(airlines, airports).collect()

    assertResult(Row("Salt Lake City Intl",66))(df(0))
    assertResult(Row("George Bush Intercontinental",16))(df(1))
    assertResult(Row("Newark Intl",11))(df(2))
    assertResult(Row("Dallas-Fort Worth International",5))(df(3))
    assertResult(Row("San Francisco International",5))(df(4))
  }

  test("New York Test") {
    val (airlines, airports, _) = loadDataset(new SQLContext(sc), Paths.get(basePath, "New York Test").toString)
    val df = countNewYorkFlights(airlines, airports).collect()

    assertResult(Row("New York",2))(df(0))

  }

  test("Super Carrier Test") {
    val (airlines, _, carriers) = loadDataset(new SQLContext(sc), Paths.get(basePath, "Super Carrier Test").toString)
    val df = getSuperCarrier(airlines, carriers).collect()

    assertResult(Row("OO", "Skywest Airlines Inc.",69))(df(0))
  }
}