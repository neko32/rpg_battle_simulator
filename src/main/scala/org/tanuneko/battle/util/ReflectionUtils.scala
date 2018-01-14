package org.tanuneko.battle.util

import scala.reflect.runtime.universe._

object ReflectionUtils {

  def updateVariableViaReflection(target: Any, value: Any)(name: String): Either[Exception, Unit] = {
    try {
      target.getClass.getMethods.find(_.getName == name + "_$eq").get.invoke(target, value.asInstanceOf[AnyRef])
      Right(() => ())
    } catch {
      case nsE: NoSuchElementException => Left(nsE)
    }
  }
}
