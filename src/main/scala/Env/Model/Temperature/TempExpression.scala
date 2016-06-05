package Env.Model.Temperature

import java.text.SimpleDateFormat
import java.time._
import java.time.format._

object TempExpression {
  def TempValue(latitude: Double, longitude: Double, dt: LocalDateTime) = {

    // The value revised in a year by earth's orbit
    // I considered the Tropic of Cancer and the Tropic of Capricorn
    val tempReviseYear: Double = {

      val Tmax = math.abs(TempAvgInLatitude.TemperatureCurves.value(latitude) *
        TempAvgInLatitude.TmaxCurves.value(latitude))

      // 1
      // The regions between the Tropic of Cancer and the Tropic of Capricorn

      // 1.1 when 12.22 < Date <= 6.21, temperature varies in sin curve1
      // 1.2 when 6.21 < Date <= 12.22 temperature varies in sin curve2

      // sin curve1
      // Temperature variable coefficient expression of equator region in a year: 
      // sin(DayinHottest * 2 * Pi * (2 - La / 23.5) / 365 * w + q) =  1
      // sin(DayinColdest * 2 * Pi * (2 - La / 23.5) / 365 * w + q) = -1

      // sin curve2
      // Temperature variable coefficient expression of equator region in a year: 
      // sin(DayinHottest * 2 * Pi * (2 - La / 23.5) / 365 * w + q + Pi * (1 - La / 23.5)) =  1
      // sin(DayinColdest * 2 * Pi * (2 - La / 23.5) / 365 * w + q + Pi * (1 - La / 23.5)) = -1

      // In equator which the latitude is 0, the hottest days are in 21th March 
      // and 22th September of every year and
      // the coldest days are in 21th June and 22th December.
      // Then we could get the value of w and q with 21th March and 21th June. 
      // Here I ignored some difference between the first half year and the second.
      // sin curve1
      // sin(80 * 2 * Pi * 2 / 365 * w + q) = 1, sin(172 * 2 * Pi * 2 / 365 * w + q) = -1
      // sin curve2
      // sin(80 * 2 * Pi * 2 / 365 * w + q + Pi ) = 1, 
      // sin(172 * 2 * Pi * 2 / 365 * w + q + Pi) = -1
      val w: Double = 365.0 / (2 * 2 * 2 * (172 - 80))
      val q: Double = math.Pi / 2 - 2 * math.Pi * 172 * 2 * w / 365
      
      // 2
      // The regions out of the Tropic of Cancer and  the Tropic of Capricorn
      // sin(DayinHottest * 2 * Pi * / 365 + q) =  1
      val qOut: Double = math.Pi / 2 - 2 * math.Pi * 172 / 365

      // There is half a cycle difference in south region.
      val southRegionOut = if (latitude < 0) math.Pi else 0
      val southRegion = if (latitude < 0) -1 else 1

      val currentDays = dt.getDayOfYear
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

      val dayslast1222 = LocalDateTime.parse((dt.getYear - 1).toString() + "-12-22 00:00:00", formatter);
      val daysin0621 = LocalDateTime.parse((dt.getYear).toString() + "-06-21 00:00:00", formatter);
      val daysin0922 = LocalDateTime.parse((dt.getYear).toString() + "-09-22 00:00:00", formatter);
      val daysin1222 = LocalDateTime.parse((dt.getYear).toString() + "-12-22 00:00:00", formatter);

      val absLatitude = math.abs(latitude)
      if (latitude > -23.5 && latitude < 23.5) {
        if (dt.isAfter(dayslast1222) && dt.isBefore(daysin0922))
          Tmax * math.sin(currentDays * 2 * math.Pi * (2 - absLatitude / 23.5) * w / 365 + q) * southRegion
        else {
          Tmax * math.sin(currentDays * 2 * math.Pi * (2 - absLatitude / 23.5) * w / 365 + q +
            math.Pi * (1 - absLatitude / 23.5)) * southRegion
        }
      } else
        Tmax * math.sin(currentDays * 2 * math.Pi / 365 + qOut )* southRegion
    }

    // The value revised with day and night by earth rotation
    // I assumed the maximum variation range of temperature in a day is 10% of the latitude value.
    val tempReviseDay: Double = {
      math.abs(TempAvgInLatitude.TemperatureCurves.value(latitude)) * 0.1 * 
      math.cos((dt.getHour * 3600 + dt.getMinute * 60 + dt.getSecond) *
        2 * math.Pi / (24 * 3600)) * (-1)
    }

//    println("tempReviseYear:  " + dt.getMonth + "|" + tempReviseYear)
//    println("tempReviseDay:  " + dt.getHour + "|" + tempReviseDay)
    TempAvgInLatitude.TemperatureCurves.value(latitude) + tempReviseYear + tempReviseDay
  }

}