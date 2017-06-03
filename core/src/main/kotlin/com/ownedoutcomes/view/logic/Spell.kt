package com.ownedoutcomes.view.logic

import ktx.collections.gdxArrayOf

enum class Spell {
  FIREBALL,
  TELEPORT;

  companion object {
    val spells = gdxArrayOf(*values())

    fun getRandomSpell(): Spell = spells.random()
  }
}