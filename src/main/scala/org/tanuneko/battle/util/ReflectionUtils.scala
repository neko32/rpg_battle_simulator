package org.tanuneko.battle.util

import scala.reflect.runtime.universe._

object ReflectionUtils {

  def updateVariableViaReflection(target: Any, value: Any)(name: String): Either[Exception, Unit] = {
    try {
      if(List("exp", "gold").contains(name)) {
        target.getClass.getMethods.find(_.getName == name + "_$eq").get.invoke(target, value.asInstanceOf[AnyRef])
      } else {
        val vv = if(value.isInstanceOf[Option[_]]) value else Option(value)
        target.getClass.getMethods.find(_.getName == name + "Override_$eq").get.invoke(target, vv.asInstanceOf[AnyRef])
      }
      Right(() => ())
    } catch {
      case nsE: NoSuchElementException => Left(nsE)
    }
  }
}
