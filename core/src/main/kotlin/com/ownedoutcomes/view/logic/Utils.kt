package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils

val totalSpells = 2

fun randomSpellId(): Int = MathUtils.random(0, totalSpells - 1)