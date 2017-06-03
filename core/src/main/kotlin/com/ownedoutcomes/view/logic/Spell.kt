package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils
import com.ownedoutcomes.view.logic.entity.Orb
import ktx.collections.gdxArrayOf
import ktx.math.component1
import ktx.math.component2

enum class Spell {
  FIREBALL {
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      // TODO
    }
  },
  ORB {
    val offset = 4f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val (playerX, playerY) = gameManager.player.position
      val angle = MathUtils.atan2(y - playerY, x - playerX)
      val distanceX = MathUtils.cos(angle)
      val distanceY = MathUtils.sin(angle)
      val orbX = playerX + distanceX * offset
      val orbY = playerY + distanceY * offset
      gameManager.entities.add(Orb(gameManager.world, gameManager.player, orbX, orbY))
    }
  };

  abstract fun use(gameManager: GameManager, x: Float, y: Float)

  companion object {
    val spells = gdxArrayOf(*values())

    fun getRandomSpell(): Spell = spells.random()
  }
}