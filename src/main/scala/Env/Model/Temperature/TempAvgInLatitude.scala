package Env.Model.Temperature

import org.apache.commons.math3.fitting._
import org.apache.commons.math3.analysis.polynomials._

object TempAvgInLatitude {

  val TemperatureCurves = {

    // The data contains some actual measurements 
    // from S.G. Warren & S.T. Schneider, J. Atmos. Sci. 36, 1377-91 (1979)
    val regionAvg: Array[(Double, Double)] = Array(
      (85, -16.9),
      (75, -12.3),
      (65, -5.1),
      (55, 2.2),
      (45, 8.8),
      (35, 16.2),
      (25, 22.9),
      (15, 26.1),
      (5, 26.4),
      (-5, 26.1),
      (-15, 24.6),
      (-25, 21.4),
      (-35, 16.5),
      (-45, 9.9),
      (-55, 2.9),
      (-65, -6.9),
      (-75, -29.5),
      (-85, -42.3))

    polynomialCurves(regionAvg)

  }

  val TmaxCurves = {

    // The data contains some actual measurements about albedo
    // I assume there is a linear relationship between the maximum variation range of temperature and albedo.
    val albedo: Array[(Double, Double)] = Array(
      (85, 0.589),
      (75, 0.544),
      (65, 0.452),
      (55, 0.407),
      (45, 0.357),
      (35, 0.309),
      (25, 0.272),
      (15, 0.248),
      (5, 0.254),
      (-5, 0.241),
      (-15, 0.236),
      (-25, 0.251),
      (-35, 0.296),
      (-45, 0.358),
      (-55, 0.426),
      (-65, 0.513),
      (-75, 0.602),
      (-85, 0.617))

    polynomialCurves(albedo)
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