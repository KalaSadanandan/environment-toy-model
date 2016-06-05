package Env.Test

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import java.time._
import java.time.format._

import Env.Cal._
import Env.Model.Temperature._
import Env.Input._
import Env.Model.Pressure._
import Env.Model.Humidity._

@RunWith(classOf[JUnitRunner])
class EnvValueCalSpec extends FlatSpec with Matchers {
  Sample1.reportingStations.map(x => {

    val airportInfos = AirportsSelect.checkAirport(x._1)

    val testerName = x._1 + " in " + x._2

    /////////////////////////////////////////////////////////////////////////////////////
    // test IATA code
    testerName should
      "count the total number of an IATA code in the airports info data" in {
        airportInfos should have length 1
      }

    val weatherInfo = CalWeatherInfo.getWeathInfo(airportInfos(0), x._2)

    val str = "%s|%.2f,%.2f,%.0f|%s|%s|%+.1f|%.1f|%.0f".format(
      weatherInfo.iata, weatherInfo.latitude, weatherInfo.longitude, weatherInfo.altitude,
      weatherInfo.dt, weatherInfo.weatherInfo, weatherInfo.temp,
      weatherInfo.pressure, weatherInfo.humidity)

    println(str)

    /////////////////////////////////////////////////////////////////////////////////////
    // Test temperature data

    val currentDays = DateTimeCal.getLocalDateTime(x._2).getDayOfYear
    val daysFlags = DateTimeCal.getDaysInSomeDay(x._2)

    val tempAvg = TempAvgInLatitude.TemperatureCurves.value(weatherInfo.latitude)
    //println("tempAvg =" + tempAvg)

    // In autumn and winter
    if (currentDays <= daysFlags.daysof0321 || currentDays > daysFlags.daysof0922) {
      // It is generally colder than average temperature in the Northern Hemisphere 
      // after considering the effect in one day
      // and warmer in the Southern Hemisphere with my algorithm.
      if (weatherInfo.latitude >= 0)
        it should "generate the lower temperature than average" in {
          weatherInfo.temp should be <= tempAvg * 1.1
        }
      else it should "generate the higher temperature than average" in {
        weatherInfo.temp should be >= tempAvg * 0.9
      }
    } else {
      if (weatherInfo.latitude < 0)
        it should "generate the lower temperature than average" in {
          weatherInfo.temp should be <= tempAvg * 1.1
        }
      else it should "generate the higher temperature than average" in {
        weatherInfo.temp should be >= tempAvg * 0.9
      }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // Test the relationship between humidity and conditions

    if (weatherInfo.humidity >= 100)
      if (weatherInfo.temp > 0)
        it should "generate rain" in {
          weatherInfo.weatherInfo should be("Rain")
        }
      else
        it should "generate rain" in {
          weatherInfo.weatherInfo should be("Snow")
        }
    else
      it should "be Sunny" in {
        weatherInfo.weatherInfo should be("Sunny")
      }
  })
}

object Sample1 {

  val dtLocal = DateTimeCal.getCurrentLocalTimeStr

  val reportingStations: Array[(String, String)] = Array(

    // Checking station in the Southern Hemisphere and 
    // south of the Tropic of Capricorn

    // Checking boundaries in the Southern Hemisphere 
    ("RGA", "2016-03-21 23:05:37"),
    ("RGA", "2016-09-22 23:05:37"),

    ("MEL", dtLocal),
    ("MEL", "2015-12-25 02:30:55"),

    // Checking boundaries in the Southern Hemisphere
    ("ADL", "2016-06-21 23:05:37"),
    ("ADL", "2015-12-22 02:30:55"),

    // (-33.95)
    ("SYD", dtLocal),
    ("SYD", "2015-12-23 16:02:12"),

    // Checking station in the Southern Hemisphere and 
    // between the Tropic of Capricorn and the equator (-15.78)
    ("KNX", "2016-01-04 23:05:37"),
    ("KNX", "2016-07-04 23:05:37"),

    // Checking station in the Northern Hemisphere and 
    // between the Tropic of Cancer and the equator (1.42)
    ("XSP", "2016-01-04 23:05:37"),
    ("XSP", "2016-07-04 23:05:37"),

    // 9.07
    ("TDG", "2016-02-21 13:05:37"),
    ("TDG", "2016-07-14 23:05:37"),
    
    // Checking station in the Northern Hemisphere and 
    // north of the Tropic of Cancer (40.08)
    ("PEK", "2016-01-04 23:05:37"),
    ("PEK", "2016-07-04 23:05:37"),

    // 47.63
    ("ZBF", "2016-01-04 23:05:37"),
    ("ZBF", "2016-07-04 23:05:37"),

    // 57.02
    ("BRR", "2016-01-04 23:05:37"),
    ("BRR", "2016-07-04 23:05:37"),
    
    // 65.03
    ("CSH", "2016-09-23 24:00:00"),
    ("CSH", "2016-01-04 12:00:00"),
   
    // 70.61
    ("K03", "2016-01-04 23:05:37"),
    ("K03", "2016-07-04 23:05:37"))
}

object DateTimeCal {
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  val getCurrentLocalTimeStr = {
    formatter.format(LocalDateTime.now())
  }

  def getLocalDateTime(dtStr: String) = {
    LocalDateTime.parse(dtStr, formatter)
  }

  def getDaysInSomeDay(dtStr: String) = {
    val dt = LocalDateTime.parse(dtStr, formatter)
    val daysof0321 = LocalDateTime.parse((dt.getYear).toString() + "-03-21 00:00:00", formatter);
    val daysof0621 = LocalDateTime.parse((dt.getYear).toString() + "-06-21 00:00:00", formatter);
    val daysof0922 = LocalDateTime.parse((dt.getYear).toString() + "-09-22 00:00:00", formatter);
    val daysof1222 = LocalDateTime.parse((dt.getYear).toString() + "-12-22 00:00:00", formatter);

    DaysInSomeDay(daysof0321.getDayOfYear, daysof0621.getDayOfYear,
      daysof0922.getDayOfYear, daysof1222.getDayOfYear)
  }

}

case class DaysInSomeDay(daysof0321: Int, daysof0621: Int, daysof0922: Int, daysof1222: Int)


