package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.view.logic.EntityType.FIREBALL
import com.ownedoutcomes.view.logic.GameManager
import com.ownedoutcomes.view.logic.angleTo
import com.ownedoutcomes.view.logic.entity.particle.FireballExplosion
import com.ownedoutcomes.view.logic.projectileCategory
import com.ownedoutcomes.view.logic.projectileMask
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.component1
import ktx.math.component2

class Fireball(
    val gameManager: GameManager,
    world: World,
    x: Float,
    y: Float) : AbstractEntity(world.body {
  type = DynamicBody
  linearDamping = 1f
  bullet = true
  position.set(x, y)
  circle(radius = 1f) {
    density = 0.001f
    isSensor = true
    filter {
      categoryBits = projectileCategory
      maskBits = projectileMask
    }
  }
}, entityType = FIREBALL, spriteName = "spell0") {
  val pushback = 22000f
  val damage = 2
  var lifetime = 1f
  override var speed: Float = 8f
  override var offsetX: Float = -1f
  override var offsetY: Float = -1f

  init {
    setSpriteSize(2f, 2f)
  }

  override fun update(delta: Float) {
    lifetime -= delta
    if (lifetime <= 0f) {
      dead = true
    }
  }

  override fun destroy() {
    explode(radius = 4f, rays = 36) { damage(it) }
    gameManager.entities.add(FireballExplosion(body, Vector2(position)))
    gameManager.soundManager.fireball.play()
    super.destroy()
  }

  fun damage(enemy: Enemy) {
    val (forceX, forceY) = body.position angleTo enemy.position
    enemy.body.applyForceToCenter(forceX * pushback, forceY * pushback, true)
    enemy.health -= damage
  }
}