package com.ownedoutcomes.view.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.ownedoutcomes.view.logic.entity.*
import com.ownedoutcomes.view.logic.entity.particle.HealExplosion
import com.ownedoutcomes.view.logic.entity.particle.TeleportExplosion
import com.ownedoutcomes.view.logic.entity.particle.ThunderExplosion
import ktx.async.schedule
import ktx.collections.gdxArrayOf
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

enum class Spell {
  FIREBALL {
    val offset = 3f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      cast(gameManager, x, y)
      val player = gameManager.player
      val level = player.level
      repeat(1 + level) {
        schedule(1f + it) {
          if (player.health > 0) {
            player.hop()
            val input = Gdx.input
            val (castX, castY) = gameManager.unproject(input.x.toFloat(), input.y.toFloat())
            cast(gameManager, castX, castY)
          }
        }
      }
    }

    private fun cast(gameManager: GameManager, x: Float, y: Float) {
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
      addOrb(playerX, playerY, distanceX, distanceY, gameManager)
      val level = gameManager.player.level
      if (level > 0) addOrb(playerX, playerY, -distanceX, -distanceY, gameManager)
      if (level > 1) addOrb(playerX, playerY, distanceX, -distanceY, gameManager)
      if (level > 2) addOrb(playerX, playerY, -distanceX, distanceY, gameManager)
    }

    private fun addOrb(playerX: Float, playerY: Float, distanceX: Float, distanceY: Float, gameManager: GameManager) {
      val orbX = playerX + distanceX * offset
      val orbY = playerY + distanceY * offset
      gameManager.entities.add(Orb(gameManager.world, gameManager.player, orbX, orbY, offset))
    }
  },
  HEAL {
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val player = gameManager.player
      player.health += (1 + (gameManager.player.level + 1) / 2)
      gameManager.entities.add(HealExplosion(player.body, Vector2(player.position)))
    }
  },
  ICE {
    val offset = 3.5f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val (playerX, playerY) = gameManager.player.position
      val (distanceX, distanceY) = gameManager.player.position angleTo vec2(x, y)
      val orbX = playerX + distanceX * offset
      val orbY = playerY + distanceY * offset
      val level = gameManager.player.level
      val damage = if (level < 2) 1 else 2
      val lifetime = 3f + level
      val ice = Ice(gameManager, gameManager.world, orbX, orbY, damage = damage, lifetime = lifetime)
      gameManager.entities.add(ice)
      ice.body.applyForceToCenter(distanceX * ice.speed, distanceY * ice.speed, true)
    }
  },
  TELEPORT {
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val player = gameManager.player
      gameManager.entities.add(TeleportExplosion(player.body, Vector2(player.position)))
      player.body.setTransform(x, y, player.body.angle)
      player.destination = null
      gameManager.entities.add(TeleportExplosion(player.body, Vector2(player.position)))
      if (player.level > 1) {
        player.health++
      }
    }
  },
  THUNDER {
    val offset = 1f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      cast(gameManager, x, y, true)
    }

    private fun cast(gameManager: GameManager, x: Float, y: Float, allowMultiple: Boolean) {
      val player = gameManager.player
      val (playerX, playerY) = player.position
      val angle = MathUtils.atan2(y - playerY, x - playerX)
      val distanceX = MathUtils.cos(angle)
      val distanceY = MathUtils.sin(angle)
      val thunderX = playerX + distanceX * offset
      val thunderY = playerY + distanceY * offset
      val level = player.level
      val damage = (1 + (level + 1) / 2)
      val thunder = Thunder(gameManager.world, thunderX, thunderY, damage)
      gameManager.entities.add(thunder)
      thunder.body.applyForceToCenter(distanceX * thunder.speed, distanceY * thunder.speed, true)
      gameManager.entities.add(ThunderExplosion(player.body, Vector2(player.position), angle))
      if (allowMultiple) {
        if (level > 1) {
          schedule(1f) {
            if (player.health > 0) {
              cast(gameManager, x, y, false)
            }
          }
        } else {
          player.hop()
        }
      }
    }
  },
  WAVE {
    val offset = 2f
    override fun use(gameManager: GameManager, x: Float, y: Float) {
      val player = gameManager.player
      val (playerX, playerY) = player.position
      cast(gameManager, x - playerX, y - playerY)
      val level = player.level
      repeat(2 + (level * 1.5f).toInt()) {
        schedule((it + 1) * 0.3f) {
          if (player.health > 0) {
            cast(gameManager, x - playerX, y - playerY)
          }
        }
      }
    }

    private fun cast(gameManager: GameManager, offsetX: Float, offsetY: Float) {
      val player = gameManager.player
      val (playerX, playerY) = player.position
      val x = playerX + offsetX
      val y = playerY + offsetY
      val angle = MathUtils.atan2(y - playerY, x - playerX)
      val distanceX = MathUtils.cos(angle)
      val distanceY = MathUtils.sin(angle)
      val waveX = playerX + distanceX * offset
      val waveY = playerY + distanceY * offset
      val wave = Wave(gameManager.world, waveX, waveY, angle)
      gameManager.entities.add(wave)
      wave.body.applyForceToCenter(distanceX * wave.speed, distanceY * wave.speed, true)
    }
  };

  abstract fun use(gameManager: GameManager, x: Float, y: Float)

  companion object {
    val offensiveSpells = gdxArrayOf(FIREBALL, ORB, ICE, THUNDER, WAVE)
    val utilitySpells = gdxArrayOf(TELEPORT, HEAL)

    fun getRandomOffensiveSpell(): Spell = offensiveSpells.random()
    fun getRandomUtilitySpell(): Spell = utilitySpells.random()
    fun getRandomSpell(utility: Boolean): Spell = if (utility) getRandomUtilitySpell() else getRandomOffensiveSpell()
  }
}