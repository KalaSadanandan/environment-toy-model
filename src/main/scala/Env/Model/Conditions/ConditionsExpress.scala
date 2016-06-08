/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Model.Conditions

/**
 *  Conditions expression
 *  Obtain result according temperature and humidity.
 *
 *  @author Cubean Liu
 *  @version 0.1
 */
object ConditionsExpress {

  /**
   * Get the weather condition according the temperature and humidity of weather station.
   * 
   * @param temp the temperature value of weather station.[C].
   * @param humidity the humidity value of weather station. [%]
   * 
   * @return Sunny, Rain or Snow
   */
  def HumidityValue(temp: Double, humidity: Double) = {
    if (humidity >= 100) {
      if (temp > 0) "Rain" else "Snow"
    } else "Sunny"
  }

}