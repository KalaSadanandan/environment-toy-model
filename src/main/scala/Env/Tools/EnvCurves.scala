/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */

package Env.Tools

import org.apache.commons.math3.fitting._
import org.apache.commons.math3.analysis.polynomials._

/**
 * Environment curves calculation
 */
object EnvCurves {
  
  /**
   *  Calculate temperature curves
   *  
   *  @param points the weighted observed points
   */
  def polynomialCurves(points: Array[(Double, Double)]) = {
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