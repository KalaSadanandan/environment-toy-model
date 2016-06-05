package Env.Model.Conditions

object ConditionsExpress {

  def HumidityValue(temp: Double, humidity: Double) = {
    if (humidity >= 100) {
      if (temp > 0) "Rain" else "Snow"
    } else "Sunny"
  }

}