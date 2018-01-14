package org.tanuneko.battle.util

object PercentUtils {

  def percentage(v: Int, p: Int): Int = {
    v - (Math.round((v * p) / 100))
  }
}
