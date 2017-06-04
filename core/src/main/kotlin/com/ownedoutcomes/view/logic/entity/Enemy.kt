package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.enemiesAmount
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.GameManager
import com.ownedoutcomes.view.logic.enemyCategory
import com.ownedoutcomes.view.logic.enemyMask
import com.ownedoutcomes.view.logic.entity.particle.DeadEnemy
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.component1
import ktx.math.component2

// TODO MUZYKA, dzwieki

open class Enemy(
    world: World,
    x: Float,
    y: Float,
    val gameManager: GameManager,
    val spriteName: String = "goblin${MathUtils.random(enemiesAmount - 1)}",
    bodySize: Float = 6f,
    initialHealth: Int = 3) : AbstractEntity(
    world.body {
      position.set(x, y)
      linearDamping = 10f
      fixedRotation = true
      type = DynamicBody
      circle(radius = bodySize / 6f) {
        density = 1f
        filter {
          categoryBits = enemyCategory
          maskBits = enemyMask
        }
      }
    }, entityType = EntityType.ENEMY, spriteName = spriteName) {
  override val speed: Float = 21500f
  var health = initialHealth
    set(value) {
      if (value < field) {
        gameManager.soundManager.playGoblinHitSound()
      }
      field = value
    }
  val player = gameManager.player
  val heartSprite = gameManager.heartSprite

  init {
    setSpriteSize(bodySize, bodySize)
    sprite.setOrigin(bodySize / 2f, bodySize / 4f)
  }

  override fun update(delta: Float) {
    destination = player.position
    super.update(delta)
    if (health <= 0) {
      dead = true
    }
  }

  override fun render(batch: Batch) {
    super.render(batch)
    val (x, y) = position
    drawHealth(x, y, batch)
  }

  protected open fun drawHealth(x: Float, y: Float, batch: Batch) {
    repeat(health) {
      heartSprite.setPosition(x - 0.4f + (it - 1), y - 2f)
      heartSprite.draw(batch)
    }
  }

  override fun destroy() {
    super.destroy()
    gameManager.entities.add(DeadEnemy(body, Vector2(position), spriteName, originalSprite = sprite))
    gameManager.player.points++
  }
}

class Boss(
    world: World,
    x: Float,
    y: Float,
    gameManager: GameManager,
    spriteName: String = "goblin${MathUtils.random(enemiesAmount - 1)}")
  : Enemy(world, x, y, gameManager, spriteName, bodySize = 9f, initialHealth = 6) {
  override val speed: Float = 60000f

  override fun destroy() {
    super.destroy()
    gameManager.player.points++
    gameManager.player.points++
  }
}