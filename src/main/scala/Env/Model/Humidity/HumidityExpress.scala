package Env.Model.Humidity

import org.apache.commons.math3.fitting._
import org.apache.commons.math3.analysis.polynomials._

object HumidityExpress {
  private val vaporPressureCurves = {

    // The vapor pressure of water, or saturation vapor pressure, 
    // increases strongly with increasing temperature:(kPa)
    val vaporPressure: Array[(Double, Double)] = Array(
      (-30, 0.049),
      (-25, 0.081),
      (-20, 0.126),
      (-15, 0.191),
      (-10, 0.287),
      (-5, 0.422),
      (0, 0.61),
      (10, 1.23),
      (20, 2.34),
      (30, 4.24),
      (40, 7.37),
      (50, 12.33),
      (60, 19.92),
      (70, 31.18),
      (80, 47.34),
      (90, 70.11),
      (100, 101.33))

    polynomialCurves(vaporPressure)
  }

  def HumidityValue(temp: Double, pressure: Double): Double = {
    val hum = pressure / (vaporPressureCurves.value(temp) * 10)

    if (hum > 100) 100 else hum
  }

  private def polynomialCurves(points: Array[(Double, Double)]) = {
    val obs = new WeightedObservedPoints()
    val polynomialDegree = 3

    points.map(x => obs.add(x._1, x._2))

    // Instantiate a third-degree polynomial fitter.
    val fitter = PolynomialCurveFitter.create(polynomialDegree);

    // Retrieve fitted parameters (coefficients of the polynomial function).
    val coeff = fitter.fit(obs.toList());

    // Get new polynomial function
    new PolynomialFunction(coeff);
  }
}

