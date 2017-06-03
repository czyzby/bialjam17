package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils
import kotlin.experimental.or

val totalSpells = 2

fun randomSpellId(): Int = MathUtils.random(0, totalSpells - 1)

val playerCategory: Short = 1 shl 0
val enemyCategory: Short = 1 shl 1
val projectileCategory: Short = 1 shl 2
val lightCategory: Short = 1 shl 3

val playerMask: Short = playerCategory or enemyCategory
val enemyMask: Short = playerCategory or enemyCategory or projectileCategory or lightCategory
val projectileMask: Short = enemyCategory
val lightMask: Short = enemyCategory