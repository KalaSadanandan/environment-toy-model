/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Test

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import java.time._
import java.time.format._
import java.io._

import Env.Cal._
import Env.Model.Temperature._
import Env.Input._
import Env.Model.Pressure._
import Env.Model.Humidity._
import Env.Tools._

/**
 * the test for weather information from expression of Temperature,Pressure, Humidity and Conditions
 */
@RunWith(classOf[JUnitRunner])
class EnvValueCalSpec extends FlatSpec with Matchers {
  Sample1.reportingStations.map(x => {

    val airportInfos = AirportsSelect.checkAirport(x._1)

    val testerName = x._1 + " at " + x._2

    /////////////////////////////////////////////////////////////////////////////////////
    // test IATA code
    testerName should
      "count the total number of an IATA code in the airports info data" in {
        airportInfos should have length 1
      }

    val dt = DateTimeCal.getLocalDateTime(x._2)
    if (dt != null) {

      val weatherInfo = CalWeatherInfo.getWeathInfo(airportInfos(0), dt)

      if (weatherInfo != null) {

        val str = "%s|%.2f,%.2f,%.0f|%s|%s|%+.1f|%.1f|%.0f".format(
          weatherInfo.iata, weatherInfo.latitude, weatherInfo.longitude, weatherInfo.altitude,
          weatherInfo.dt, weatherInfo.weatherInfo, weatherInfo.temp,
          weatherInfo.pressure, weatherInfo.humidity)

        println(str)

        /////////////////////////////////////////////////////////////////////////////////////
        // Test the output information is correct format

        val winf = CalWeatherInfo.getWeatherStr(weatherInfo.iata, dt).get

        it should "generate weather info string" in {
          winf should equal(str)
        }

        /////////////////////////////////////////////////////////////////////////////////////
        // Test temperature data

        val dtAll = DateTimeCal.getDTOfSomeDay(dt)

        if (dtAll != null) {
          // Get average temperature
          val tempAvg = TempInLatitude.TemperatureAvgCurves.value(weatherInfo.latitude)

          // Get the maximum range value of temperature variable
          val tempTmax = math.abs(tempAvg * TempInLatitude.TmaxVariableCurves.value(weatherInfo.latitude))

          // Test all regions
          it should "generate a reasonable temperature value" in {
            val Tmax = math.abs(tempAvg) * 1.1 + math.abs(tempTmax)
            weatherInfo.temp should (be >= -Tmax and be <= Tmax)
          }

          // Test the areas far from the Equator
          if (dt.isBefore(dtAll.dt0321) || dt.isAfter(dtAll.dt0922)) {
            // Sun is above the southern Hemisphere.
            // It is generally colder than average temperature in the most areas of Northern Hemisphere 
            // after considering the effect in one day
            // and warmer in the most areas of Southern Hemisphere with my algorithm.
            if (weatherInfo.latitude > GeographyInfo.laTropicofCancer / 2)
              it should "generate the lower temperature than average" in {
                weatherInfo.temp should be <= tempAvg * 1.1
              }
            else if (weatherInfo.latitude < GeographyInfo.laTropicofCapricorn / 2)
              it should "generate the higher temperature than average" in {
                weatherInfo.temp should be >= tempAvg * 0.9
              }
          } else {
            // Sun is above the northern Hemisphere.
            if (weatherInfo.latitude < GeographyInfo.laTropicofCapricorn / 2)
              it should "generate the lower temperature than average" in {
                weatherInfo.temp should be <= tempAvg * 1.1
              }
            else if (weatherInfo.latitude > GeographyInfo.laTropicofCancer / 2)
              it should "generate the higher temperature than average" in {
                weatherInfo.temp should be >= tempAvg * 0.9
              }
          }

          // Test the areas near to the Equator
          if (weatherInfo.latitude <= GeographyInfo.laTropicofCancer / 2 &&
            weatherInfo.latitude >= GeographyInfo.laTropicofCapricorn / 2) {
            // maybe we should do sth.
          }
        }

        /////////////////////////////////////////////////////////////////////////////////////
        // Test atmospheric pressure
        // Average sea-level pressure is 1013.25 hPa and record highs close to 1085.0 hbar.
        // So the highest value should lower than 1013.25 hPa and the lowest value should 
        // higher than 72 hPa considering the location of airport. 

        val pressure = PressureExpression.PressureValue(weatherInfo.temp, weatherInfo.altitude)

        it should "generate a reasonalbe Atmospheric Pressure value" in {
          pressure should be(1013.25 +- 72.0)
        }

        /////////////////////////////////////////////////////////////////////////////////////
        // Test the relationship humidity value

        val humidity = HumidityExpress.HumidityValue(weatherInfo.temp, weatherInfo.pressure).toInt

        it should "generate a reasonalbe Humidity value" in {
          humidity should (be > 0 and be <= 100)
        }

        /////////////////////////////////////////////////////////////////////////////////////
        // Test weather conditions

        it should "generate weather condition" in {
          Some(weatherInfo.weatherInfo) should contain oneOf ("Rain", "Snow", "Sunny")
        }

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
      }
    }
  })

  /////////////////////////////////////////////////////////////////////////////////////
  // Test CalWeatherInfo.getWeatherStr with not correct IATA codes.

  val exDT = DateTimeCal.getLocalDateTime("2016-11-21 23:05:37")

  val erstr1 = CalWeatherInfo.getWeatherStr("sy", exDT).
    getOrElse("Cannot get weather information.")

  "Exceptional IATA code" should "generate warning string" in {
    erstr1 should equal("Cannot get weather information.")
  }

  val erstr2 = CalWeatherInfo.getWeatherStr("sydney", exDT).
    getOrElse("Cannot get weather information.")

  val strResult = "\nThere are several airports in sydney: \n" +
    "YQY|Sydney|Sydney|Canada|46.16,-60.05,203.00|-4.00|America/Halifax\n" +
    "BWU|Sydney Bankstown|Sydney|Australia|-33.92,150.99,29.00|10.00|Australia/Sydney\n" +
    "SYD|Sydney Intl|Sydney|Australia|-33.95,151.18,21.00|10.00|Australia/Sydney\n"
  "Entering a city name" should "generates serveral selections if the number of the airports is more than one." in {
    erstr2 should equal(strResult)
  }

}

/**
 * the first test sample
 */
object Sample1 {

  private val dtLocal = DateTimeCal.getCurrentLocalTimeStr

  /**
   * samples of some IATA and local date and time in weather stations
   */
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
    ("XSP", "2016-03-20 00:00:00"),
    ("XSP", "2016-03-20 23:59:59"),
    ("XSP", "2016-03-21 00:00:00"),
    ("XSP", "2016-03-21 23:59:59"),
    ("XSP", "2016-09-22 00:00:00"),
    ("XSP", "2016-09-23 00:00:00"),
    ("XSP", "2016-01-04 23:05:37"),
    ("XSP", "2016-07-04 23:05:37"),
    ("XSP", "2016-12-23 00:00:00"),
    ("XSP", "2016-12-22 23:59:59"),

    // 9.07
    ("TDG", "2016-02-21 13:05:37"),
    ("TDG", "2016-07-14 23:05:37"),

    // Checking station in the Northern Hemisphere and 
    // north of the Tropic of Cancer (40.08)
    ("PEK", "2016-01-04 23:05:37"),
    ("PEK", "2016-07-04 23:05:37"),

    // 47.63
    ("ZBF", "2016-03-20 00:00:00"),
    ("ZBF", "2016-03-20 23:59:59"),
    ("ZBF", "2016-03-21 00:00:00"),
    ("ZBF", "2016-03-21 23:59:59"),
    //    ("ZBF", "2016-09-22 00:00:00"),
    ("ZBF", "2016-09-23 00:00:00"),
    ("ZBF", "2016-01-04 23:05:37"),
    ("ZBF", "2016-07-04 23:05:37"),
    ("ZBF", "2016-12-23 00:00:00"),
    ("ZBF", "2016-12-22 23:59:59"),

    // 57.02
    ("BRR", "2016-01-04 23:05:37"),
    ("BRR", "2016-07-04 23:05:37"),
    //    ("BRR", "2016-09-22 00:00:00"),

    // 65.03
    ("CSH", "2016-09-23 24:00:00"),
    ("CSH", "2016-01-04 12:00:00"),
    //    ("CSH", "2016-09-22 00:00:00"),

    // 70.61
    ("K03", "2016-01-04 23:05:37"),
    ("K03", "2016-07-04 23:05:37"))
}



