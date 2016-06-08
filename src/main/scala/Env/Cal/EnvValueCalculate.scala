/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Cal

import java.time._
import java.time.format._
import java.time.temporal._
import java.util.function._
import java.util.Map._
import java.util._

import Env.Model.Temperature._
import Env.Input._
import Env.Model.Pressure._
import Env.Model.Humidity._
import Env.Model.Conditions._

/**
 *  Output weather information
 *
 *  @author Cubean Liu
 *  @version 0.1
 */
object CalWeatherInfo {

  // the format of input date and time
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * get weather information string
   *
   * @param iada 3-letter IATA code
   * @param localDT local date and time
   *
   * @return WeatherInfo String
   * [SYD|-33.95,151.18,21|2016-06-08T05:52:35Z|Sunny|+14.0|1012.5|85]
   */
  def getWeatherStr(iada: String, localDT: String): String = {
    val strNow = formatter.format(LocalDateTime.now())
    val infos = AirportsSelect.checkAirport(iada)

    if (infos == null || infos.length == 0) null
    else {
      var strText: String = null

      try {
        if (infos.length > 1) {

          strText = ("\nThere are several airports in " + iada + ": \n")
          infos.map {
            x =>
              strText += "%s|%s|%s|%s|%.2f,%.2f,%.2f|%.2f|%s\n".format(
                x.IATA, x.Name, x.City, x.Country,
                x.Latitude, x.Longitude, x.Altitude, x.Timezone, x.Tz)
          }
          //strText += ("\nPlease input an IATA code from the list: ")
        } else {

          val info = getWeathInfo(infos(0), localDT)
          if (info != null) {
            strText = "%s|%.2f,%.2f,%.0f|%s|%s|%+.1f|%.1f|%.0f\n".format(
              info.iata, info.latitude, info.longitude, info.altitude,
              info.dt, info.weatherInfo, info.temp, info.pressure, info.humidity)
          }
        }

      } catch {
        case ex: UnsupportedTemporalTypeException => ex.printStackTrace()
        case ex: IllegalFormatConversionException => ex.printStackTrace()
        case ex: IllegalFormatPrecisionException  => ex.printStackTrace()

        case ex: Exception                        => ex.printStackTrace()
      }

      strText
    }
  }

  /**
   * get weather information class
   *
   * @param iada 3-letter IATA code
   * @param localDT local date and time
   *
   * @return WeatherInfo
   *
   */
  def getWeathInfo(x: AirportInfo, localDT: String): WeatherInfo = {
    try {
      val dt = LocalDateTime.parse(localDT, formatter)
      val zoneDT = ZonedDateTime.of(dt, ZoneId.of(x.Tz))

      val temp = TempExpression.TempValue(x.Latitude, dt)
      val pressure = PressureExpression.PressureValue(temp, x.Altitude) / 100
      val humidity = HumidityExpress.HumidityValue(temp, pressure)
      val weatherInfo = ConditionsExpress.HumidityValue(temp, humidity)

      WeatherInfo(x.IATA, x.Latitude, x.Longitude, x.Altitude,
        zoneDT.format(DateTimeFormatter.ISO_INSTANT),
        weatherInfo, temp, pressure, humidity)

    } catch {
      case ex: DateTimeParseException => {
        println(ex)
        null
      }
      case ex: Exception =>
        println(ex)
        null
    }
  }
}

/**
 * Weather information class
 *
 * @param iata 3-letter IATA code.
 * @param latitude Decimal degrees, usually to six significant digits. Negative is South, positive is North.
 * @param longitude Decimal degrees, usually to six significant digits. Negative is West, positive is East.
 * @param altitude In feet.
 * @param dt ISO date time string.
 * @param weatherInfo weather information string [Sunny, Rain or Snow]
 * @param temp the temperature value of weather station.[C].
 * @param pressure the pressure value of weather station. [Pa]
 * @param humidity the humidity value of weather station. [%]
 *
 */
case class WeatherInfo(iata: String, latitude: Double, longitude: Double, altitude: Double,
                       dt: String, weatherInfo: String, temp: Double, pressure: Double,
                       humidity: Double)

/**
 *  main program of Model of environment
 *  get input loop from console: 
 *  1. IATA code or airport English name
 *  2. local date and time with format "yyyy-MM-dd HH:mm:ss"
 *  
 */
object EnvValueCalculate extends App {

  //  println(CalWeatherInfo.getWeatherStr("SYD", "2015-12-23 16:02:12"))
  //  println(CalWeatherInfo.getWeatherStr("Melbourne", "2015-12-25 02:30:55"))
  //  println(CalWeatherInfo.getWeatherStr("MEL", "2015-12-25 02:30:55"))

  var ok = true
  while (ok) {
    print("\nPlease enter an IATA code or city name : ")
    val iata = io.StdIn.readLine

    if (!iata.equals("quit")) {
      print("Please date and time (eg. 2015-12-23 16:02:12) : ")
      val dt = io.StdIn.readLine

      if (!dt.equals("quit")) {
        val str = CalWeatherInfo.getWeatherStr(iata, dt)
        if (str != null) println(str)
      } else {
        ok = false
        print("program exit")
      }
    } else {
      ok = false
      print("\nprogram exit")
    }
  }

}
