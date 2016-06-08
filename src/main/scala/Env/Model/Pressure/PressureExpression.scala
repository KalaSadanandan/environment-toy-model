/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Model.Pressure

/**
 *  Pressure expression
 *  Obtain result according temperature and altitude.
 *
 *  @author Cubean Liu
 *  @version 0.1
 */
object PressureExpression {
  // static pressure (pressure at sea level) [Pa]
  private val Pb: Double = 101325

  // standard temperature (temperature at sea level) [K]
  private val Tb: Double = 288

  // standard temperature lapse rate [K/m] = -0.0065 [K/m]
  private val Lb: Double = -0.0065

  // hb - height at the bottom of atmospheric layer [m]
  // The altitude at a given air pressure can be calculated for an altitude up to 11 km (36,090 feet).
  private val hb: Double = 11000

  // gravitational acceleration constant = 9.80665 
  private val g0 = 9.80665

  // molar mass of Earth’s air = 0.0289644 [kg/mol]
  private val M = 0.0289644

  // universal gas constant = 8.31432   
  private val R = 8.31432

  /**
   * Get the pressure value according the temperature and attitude of weather station.[Pa]
   *
   * @param temp the temperature value of weather station.[C]
   * @param altitude the altitude value of weather station. height about sea level [m].
   */
  def PressureValue(temp: Double, altitude: Double): Double = {
    Pb * math.pow((1 - 0.02257 * (altitude * 0.3048) / 1000), (-g0 * M / (R * Lb)))
  }
}