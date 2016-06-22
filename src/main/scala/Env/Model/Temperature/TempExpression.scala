/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Model.Temperature

import java.text.SimpleDateFormat
import java.time._
import java.time.format._
import Env.Tools._

/**
 *  Temperature expression
 *  Obtain result according latitude and localDateTime.
 *
 *  @author Cubean Liu
 *  @version 0.1
 */
object TempExpression {

  /**
   *  Get the temperature value according the latitude and localDateTime of weather station.
   *
   *  @param latitude the latitude value of weather station.
   *  @param dt  the local date and time of weather station.
   */
  def TempValue(latitude: Double, dt: LocalDateTime) = {

    // Get average temperature
    val tempAvg = TempInLatitude.TemperatureAvgCurves.value(latitude)

    // Get the maximum range value of temperature variable
    val tempTmax = math.abs(tempAvg * TempInLatitude.TmaxVariableCurves.value(latitude))

    val PI = math.Pi

    // The value revised in a year by earth's orbit
    // I considered the Tropic of Cancer and the Tropic of Capricorn
    val tempReviseYear: Double = {

      // 1 Outside of the Tropics

      // 1.1 North Areas outside of the Tropic of Cancer:
      // tempReviseYear = tempTmax * sin(t)
      // t = (Tdays – Tdaysin321) * (2 * Pi / 365) where Tdays from 21th March to 20th Marth of next year.

      // 1.2 South Areas outside of the Tropic of Capricorn:
      // tempReviseYear = tempTmax * sin (t + Pi)
      // t = (Tdays – Tdaysin321) * (2 * Pi / 365) where Tdays from 21th March to 20th Marth of next year.

      // 2 The Tropics - regions between the Tropic of Cancer and the Tropic of Capricorn

      // The temperature is influenced by the day in one year and its’ latitude.
      // tempReviseYear = tempTmax * cos (x + t)
      // x = XLatitude * (Pi/2)/23.45 where XLatitude from -23.45 to 23.45.
      // t = (Tdays – Tdaysof0321) * (2 * Pi / days of this year) where Tdays from the begin of this year to 21th June.
      // t = (Tdaysof0922 - Tdays) * (2 * Pi / days of this year) where Tdays from 22th Jun to 22th Dec.
      // t = (Tdays - Tdaysof0321 of next year – days of next year) * (2 * Pi / days of next year) where Tdays from 23th Dec to the end of this year.

      val allDays = DateTimeCal.getDTOfSomeDay(dt)
      if (allDays != null) {
        val absDays = DateTimeCal.getDaysfrom0321(dt)
        val relativeDays = DateTimeCal.getRelaDaysfrom0321(dt)

        // There is half a cycle difference in south region.
        val southRegion = if (latitude < 0) PI else 0

        if (latitude > GeographyInfo.laTropicofCapricorn &&
          latitude < GeographyInfo.laTropicofCancer) {

          // 2 The Tropics - regions between the Tropic of Cancer and the Tropic of Capricorn
          tempTmax * (math.cos(latitude * (PI / 2) / GeographyInfo.laTropicofCancer -
            (relativeDays * (2 * PI / DateTimeCal.daysOneYear(dt)))))

        } else
          // 1 Outside of the Tropics
          tempTmax * math.sin(absDays * 2 * PI / DateTimeCal.daysOneYear(dt) + southRegion)
      } else 0
    }

    // The value revised with day and night by earth rotation
    // I assumed the maximum variation range of temperature in a day is 10% of the latitude value.
    val tempReviseDay: Double = {
      math.abs(tempAvg) * 0.1 *
        math.cos(DateTimeCal.getTotalSecondsOfDay(dt) * 2 * PI / (24 * 3600)) * (-1)
    }

    //    println("tempReviseYear:  " + dt.getMonth + "|" + tempReviseYear)
    //    println("tempReviseDay:  " + dt.getHour + "|" + tempReviseDay)

    // Get the final value with 3 items
    tempAvg + tempReviseYear + tempReviseDay
  }

}
