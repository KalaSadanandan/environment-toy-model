/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */
package Env.Tools

import java.time._
import java.time.format._

/**
 * get some date time calculation result
 */
object DateTimeCal {
  // the input format of date and time
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * get current local date and time string
   */
  val getCurrentLocalTimeStr = {
    formatter.format(LocalDateTime.now())
  }

  /**
   * get current local date and time
   *
   * @param dtStr local date and time string
   *
   * @return LocalDateTime
   */
  def getLocalDateTime(dtStr: String) = {
    try {
      LocalDateTime.parse(dtStr, formatter)
    } catch {
      case ex: DateTimeParseException => {
        println(ex)
        println("Exception 02: Please input correct format of date and time (eg. 2015-12-23 16:02:12).")
        null
      }
      case ex: Exception =>
        println(ex)
        null
    }
  }

  /**
   * get LocalDateTime of some key days in a year:21th March, 21th June, 22th September, 22th December
   *
   * @param dt local date and time
   *
   * @return DaysInSomeDay class
   */
  def getDTOfSomeDay(dt: LocalDateTime) = {
    try {
      val strYear = (dt.getYear).toString()

      val DT0321 = LocalDateTime.parse(strYear + "-03-21 00:00:00", formatter);
      val DT0621 = LocalDateTime.parse(strYear + "-06-21 00:00:00", formatter);
      val DT0922 = LocalDateTime.parse(strYear + "-09-22 00:00:00", formatter);
      val DT1222 = LocalDateTime.parse(strYear + "-12-22 00:00:00", formatter);

      DTOfSomeDay(DT0321, DT0621, DT0922, DT1222)
    } catch {
      case ex: DateTimeParseException => {
        println(ex)
        println("Exception 02: Please input correct format of date and time (eg. 2015-12-23 16:02:12).")
        null
      }
      case ex: Exception =>
        println(ex)
        null
    }
  }

  /**
   * get days of some key days in a year:Today, 21th March, 21th June, 22th September, 22th December
   *
   * @param dt local date and time
   *
   * @return DaysInSomeDay class
   */
  //  def getDaysOfSomeDay(dt: LocalDateTime) = {
  //
  //    val dtAll = getDTOfSomeDay(dt)
  //
  //    DaysOfSomeDay(dt.getDayOfYear, dtAll.dt0321.getDayOfYear, dtAll.dt0621.getDayOfYear,
  //      dtAll.dt0922.getDayOfYear, dtAll.dt1222.getDayOfYear)
  //  }

  /**
   * Get total days in one year
   *
   * @param dt - local date and time
   *
   * @return days: 365 or 366
   */
  def daysOneYear(dt: LocalDateTime): Int = {

    val allDays = getDTOfSomeDay(dt)
    if(allDays == null) return 365

    // The days in one year is decided by the February.
    val newDt = if (dt.isAfter(allDays.dt1222)) dt.plusYears(1) else dt
    val daysof1231 = LocalDateTime.parse(newDt.getYear.toString() + "-12-31 00:00:00", formatter);

    daysof1231.getDayOfYear
  }

  /**
   * get days from 21th Match of this year
   *
   * @param dt - local date and time
   *
   * @return days
   */
  def getDaysfrom0321(dt: LocalDateTime): Int = {

    val dtAll = getDTOfSomeDay(dt)
    if(dtAll == null) return 0

    if (dt.isEqual(dtAll.dt0321) || dt.isAfter(dtAll.dt0321))
      dt.getDayOfYear - dtAll.dt0321.getDayOfYear
    else (daysOneYear(dt.minusYears(1)) - dtAll.dt0321.minusYears(1).getDayOfYear) + dt.getDayOfYear
  }

  /**
   * get relative days from 21th Match of this year
   *
   * @param dt - local date and time
   *
   * @return days (+/-), which no more than half a year
   */
  def getRelaDaysfrom0321(dt: LocalDateTime): Int = {

    val dtAll = getDTOfSomeDay(dt)
    if(dtAll == null) return 0
    
    val daysOfToday = dt.getDayOfYear

    // (Tdays – Tdaysof0321) where Tdays from the begin of this year to 21th June.
    // (Tdaysof0922 - Tdays) where Tdays from 22th Jun to 22th Dec.
    // (Tdays - Tdaysof0321 of next year – days of next year) where Tdays from 23th Dec to the end of this year.
    if (dt.isBefore(dtAll.dt0621.plusDays(1)))
      daysOfToday - dtAll.dt0321.getDayOfYear
    else if (dt.isBefore(dtAll.dt1222.plusDays(1)))
      dtAll.dt0922.getDayOfYear - daysOfToday
    else daysOfToday - dtAll.dt0321.plusYears(1).getDayOfYear - daysOneYear(dt.plusYears(1))
  }

  /**
   * Get the total seconds number of a LocalDateTime in one day
   */
  def getTotalSecondsOfDay(dt: LocalDateTime): Int = {
    dt.getHour * 3600 + dt.getMinute * 60 + dt.getSecond
  }
}

/**
 * the LocalDateTime of some key dates in a year: 21th March, 21th June, 22th September, 22th December
 */
case class DTOfSomeDay(dt0321: LocalDateTime, dt0621: LocalDateTime, dt0922: LocalDateTime, dt1222: LocalDateTime)

/**
 * the days of some key dates in a year: Today, 21th March, 21th June, 22th September, 22th December
 */
//case class DaysOfSomeDay(daysofToday: Int, daysof0321: Int, daysof0621: Int, daysof0922: Int, daysof1222: Int)
