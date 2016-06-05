package Env.Input

import scala.io.Source

object AirportsSelect {
  private val airportInfo: Array[AirportInfo] = {
    val source = Source.fromFile("data/airports.dat")
    val lineInterator = source.getLines()

    def trimQuote(s: String) = s.replaceAll("\"", "")

    var infos = Array[AirportInfo]()
    for (l <- lineInterator) {
      val tokens = l.split(",")

      try {
        if (tokens != null && tokens.length > 1) {
          infos = infos :+ AirportInfo(tokens(0).toInt, trimQuote(tokens(1)), trimQuote(tokens(2)),
            trimQuote(tokens(3)), trimQuote(tokens(4)), trimQuote(tokens(5)),
            tokens(6).toDouble, tokens(7).toDouble, tokens(8).toDouble,
            tokens(9).toFloat, trimQuote(tokens(10)), trimQuote(tokens(11)))
        }
      } catch {
        case ex: NumberFormatException => {
          println(l)
          ex.printStackTrace()
        }
        case ex: Exception => ex.printStackTrace()
      }
    }

    infos
  }

  def checkAirport(iata: String): Array[AirportInfo] = {
    val len = iata.length()
    if (len >= 3 && len < 50) {
      if (len == 3) {
        val airports = airportInfo.filter { x => x.IATA.equalsIgnoreCase(iata) }
        if (airports.length > 0) {
          //airports.map { x => println(x) }
          return airports
        } else printf("Cannot find the IATA code: %s \nPlease input correct IATA code.\n", iata)
      } else {
        val airports = airportInfo.filter { x => x.City.equalsIgnoreCase(iata) && x.IATA != "" }
        if (airports.length > 0) {
          //airports.map { x => println(x) }
          return airports
        } else printf("Cannot find the city name: %s \nPlease input correct city name.\n", iata)
      }
    } else println("Please input correct IATA code or city name.")

    return null
  }
}

// As of January 2012, the OpenFlights Airports Database contains 6977 airports spanning the globe, 
// as shown in the map above. Each entry contains the following information:

// Airport ID	  Unique OpenFlights identifier for this airport.
// Name	        Name of airport. May or may not contain the City name.
// City	        Main city served by airport. May be spelled differently from Name.
// Country	    Country or territory where airport is located.
// IATA/FAA	    3-letter FAA code, for airports located in Country "United States of America".
//              3-letter IATA code, for all other airports.
//              Blank if not assigned.
// ICAO	        4-letter ICAO code.
//              Blank if not assigned.
// Latitude	    Decimal degrees, usually to six significant digits. Negative is South, positive is North.
// Longitude	  Decimal degrees, usually to six significant digits. Negative is West, positive is East.
// Altitude	    In feet.
// Timezone	    Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
// DST	        Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O (Australia), 
//              Z (New Zealand), N (None) or U (Unknown). See also: Help: Time
// Tz           database time zone	Timezone in "tz" (Olson) format, eg. "America/Los_Angeles".

// The data is ISO 8859-1 (Latin-1) encoded, with no special characters.
// Note: Rules for daylight savings time change from year to year and from country to country. 
// The current data is an approximation for 2009, built on a country level. 
// Most airports in DST-less regions in countries that generally observe DST 
// (eg. AL, HI in the USA, NT, QL in Australia, parts of Canada) are marked incorrectly.
case class AirportInfo(ID: Int, Name: String, City: String, Country: String, IATA: String, ICAO: String,
                       Latitude: Double, Longitude: Double, Altitude: Double, Timezone: Float, DST: String, Tz: String)
