package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import kotlin.experimental.or

val playerCategory: Short = 1 shl 0
val enemyCategory: Short = 1 shl 1
val projectileCategory: Short = 1 shl 2
val lightCategory: Short = 1 shl 3

val playerMask: Short = playerCategory or enemyCategory
val enemyMask: Short = playerCategory or enemyCategory or projectileCategory or lightCategory
val projectileMask: Short = enemyCategory
val lightMask: Short = enemyCategory

infix fun Vector2.angleTo(other: Vector2): Vector2 {
  val (x, y) = this
  val (otherX, otherY) = other
  val angle = MathUtils.atan2(otherY - y, otherX - x)
  return vec2(MathUtils.cos(angle), MathUtils.sin(angle))
}