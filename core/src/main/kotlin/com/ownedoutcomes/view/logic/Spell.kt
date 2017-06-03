package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.ownedoutcomes.view.logic.entity.Fireball
import com.ownedoutcomes.view.logic.entity.Orb
import com.ownedoutcomes.view.logic.entity.particle.HealExplosion
import ktx.collections.gdxArrayOf
import ktx.math.component1
import ktx.math.component2

enum class Spell {
  FIREBALL {
    val offset = 3f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val (playerX, playerY) = gameManager.player.position
      val angle = MathUtils.atan2(y - playerY, x - playerX)
      val distanceX = MathUtils.cos(angle)
      val distanceY = MathUtils.sin(angle)
      val orbX = playerX + distanceX * offset
      val orbY = playerY + distanceY * offset
      val fireball = Fireball(gameManager, gameManager.world, orbX, orbY)
      gameManager.entities.add(fireball)
      fireball.body.applyForceToCenter(distanceX * fireball.speed, distanceY * fireball.speed, true)
    }
  },
  ORB {
    val offset = 5f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val (playerX, playerY) = gameManager.player.position
      val angle = MathUtils.atan2(y - playerY, x - playerX)
      val distanceX = MathUtils.cos(angle)
      val distanceY = MathUtils.sin(angle)
      val orbX = playerX + distanceX * offset
      val orbY = playerY + distanceY * offset
      gameManager.entities.add(Orb(gameManager.world, gameManager.player, orbX, orbY))
    }
  },
  HEAL {
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val player = gameManager.player
      gameManager.entities.add(HealExplosion(player.body, Vector2(player.position)))
      player.body.setTransform(x, y, player.body.angle)
      player.destination = null
      gameManager.entities.add(HealExplosion(player.body, Vector2(player.position)))
      player.health += 2
    }
  };

  abstract fun use(gameManager: GameManager, x: Float, y: Float)

  companion object {
    val spells = gdxArrayOf(*values())

    fun getRandomSpell(): Spell = spells.random()
  }
}