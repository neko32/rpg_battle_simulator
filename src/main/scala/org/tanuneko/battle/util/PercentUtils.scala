package org.tanuneko.battle.util

object PercentUtils {

  def percentage(v: Int, p: Int): Int = {
    Math.round((v * p) / 100)
  }
}
