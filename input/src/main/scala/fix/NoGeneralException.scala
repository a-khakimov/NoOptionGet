/*
rule = NoGeneralException
*/
package fix

import scala.language.higherKinds

class CustomErrorType(message: String) extends Exception(message)

object NoGeneralException {

  def raiseThrowable(error: Throwable): Unit = {}

  def raiseException(error: Exception): Unit = {}

  raiseException(new Exception("Message")) /* assert: NoGeneralException
                 ^^^^^^^^^^^^^^^^^^^^^^^^
  Exception is not allowed
  */

  raiseThrowable(new Throwable("Message")) /* assert: NoGeneralException
                 ^^^^^^^^^^^^^^^^^^^^^^^^
  Exception is not allowed
  */

  raiseThrowable(new CustomErrorType("Message"))
}
