/*                                                                      *\
**    A Toy Model of Environment                           							**
**    https://github.com/cubean/environment-toy-model.git               **
\*                                                                      */
package Env.Tools

object GeographyInfo {
  /**
   * latitude of the Tropic of Cancer
   */
  val laTropicofCancer = 23.45

  /**
   * latitude of the Tropic of Capricorn
   */
  val laTropicofCapricorn = -23.45

  /**
   * Vernal Equinox - Sun is above the equator.
   */
  val dtVernalEquinox = DateTimeCal.getLocalDateTime("2016-03-21 00:00:00")

  /**
   * Summer Solstice - Sun is above the tropic of cancer.
   */
  val dtSummerSolstice = DateTimeCal.getLocalDateTime("2016-06-21 00:00:00")

  /**
   * Autumnal Equinox - Sun is above the equator.
   */
  val dtAutumnalEquinox = DateTimeCal.getLocalDateTime("2016-09-22 00:00:00")

  /**
   * Winter Solstice - Sun is above the tropic of Capricorn.
   */
  val dtWinterSolstice = DateTimeCal.getLocalDateTime("2016-12-22 00:00:00")

}
