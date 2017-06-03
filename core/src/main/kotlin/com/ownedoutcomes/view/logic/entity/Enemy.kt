package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.enemiesAmount
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.enemyCategory
import com.ownedoutcomes.view.logic.enemyMask
import ktx.box2d.body
import ktx.box2d.filter

class Enemy(
    world: World,
    x: Float,
    y: Float,
    val player: Player) : AbstractEntity(
    world.body {
      position.set(x, y)
      linearDamping = 10f
      fixedRotation = true
      type = DynamicBody
      circle {
        density = 1f
        filter {
          categoryBits = enemyCategory
          maskBits = enemyMask
        }
      }
    }, entityType = EntityType.ENEMY, spriteName = "goblin${MathUtils.random(enemiesAmount - 1)}") {
  override val speed: Float = 12000f

  init {
    setSpriteSize(6f, 6f)
    sprite.setOrigin(3f, 1.5f)
  }

  override fun update(delta: Float) {
    destination = player.position
    super.update(delta)
  }
}