/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Model.Humidity

import Env.Tools._

/**
 *  Humidity expression
 *  Obtain result according temperature and pressure.
 *
 *  @author Cubean Liu
 *  @version 0.1
 */
object HumidityExpress {

  /**
   * Get humidity value according the temperature and pressure of weather station.[%]
   *
   * @param temp the temperature value of weather station.[C].
   * @param pressure the pressure value of weather station. [hPa]
   */
  def HumidityValue(temp: Double, pressure: Double): Double = {
    val hum = (pressure / 10) / vaporPressureCurves.value(temp)

    if (hum > 100) 100 else hum
  }

  // Get the pressure value by the polynomial curve
  private val vaporPressureCurves = {

    val valTempDownZero = -30.0 until 0.0 by 5
    val valTempUpZero = 0.0 to 100.0 by 10

    val valTemp = valTempDownZero ++ valTempUpZero

    // The vapor pressure of water, or saturation vapor pressure, 
    // increases strongly with increasing temperature:(kPa)
    val pressureVapor: Array[Double] = Array(
      0.049, 0.081, 0.126, 0.191, 0.287, 0.422, 0.61,
      1.23, 2.34, 4.24, 7.37, 12.33, 19.92, 31.18, 47.34, 70.11, 101.33)

    EnvCurves.polynomialCurves((valTemp zip pressureVapor).toArray)
  }
}

