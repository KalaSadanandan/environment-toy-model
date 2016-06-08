/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Model.Temperature

import Env.Tools._

/**
 *  Get a temperature value with latitude value by polynomial curves
 *
 *  @author Cubean Liu
 *  @version 0.1
 */
object TempInLatitude {

  private val valLatitude = 85.0 to -85.0 by -10

  /**
   * Get an average temperature value with latitude value by polynomial curves
   *
   */
  val TemperatureAvgCurves = {

    // The data contains some actual measurements 
    // from S.G. Warren & S.T. Schneider, J. Atmos. Sci. 36, 1377-91 (1979)
    val WeightedObservedPointsTemp: Array[Double] = Array(
      -16.9, -12.3, -5.1, 2.2, 8.8, 16.2, 22.9, 26.1, 26.4,
      26.1, 24.6, 21.4, 16.5, 9.9, 2.9, -6.9, -29.5, -42.3)

    EnvCurves.polynomialCurves((valLatitude zip WeightedObservedPointsTemp).toArray)

  }

  /**
   * Get a maximum variable temperature rang value with latitude value
   * by polynomial curves
   */
  val TmaxVariableCurves = {

    // The data contains some actual measurements about albedo
    // I assume there is a linear relationship between the maximum variation range of temperature and albedo.
    val valAlbedo: Array[Double] = Array(
      0.589, 0.544, 0.452, 0.407, 0.357, 0.309, 0.272, 0.248, 0.254,
      0.241, 0.236, 0.251, 0.296, 0.358, 0.426, 0.513, 0.602, 0.617)

    EnvCurves.polynomialCurves((valLatitude zip valAlbedo).toArray)
  }
}