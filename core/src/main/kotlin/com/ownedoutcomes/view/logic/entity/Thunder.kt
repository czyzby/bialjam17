package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.view.logic.EntityType.FIREBALL
import com.ownedoutcomes.view.logic.EntityType.THUNDER
import com.ownedoutcomes.view.logic.GameManager
import com.ownedoutcomes.view.logic.angleTo
import com.ownedoutcomes.view.logic.entity.particle.FireballExplosion
import com.ownedoutcomes.view.logic.projectileCategory
import com.ownedoutcomes.view.logic.projectileMask
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.component1
import ktx.math.component2

class Thunder(
    world: World,
    x: Float,
    y: Float,
    val damage: Int) : AbstractEntity(world.body {
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
}, entityType = THUNDER, spriteName = "black-alpha") {
  val pushback = 15000f
  var lifetime = 0.3f
  override var speed: Float = 40f

  override fun update(delta: Float) {
    lifetime -= delta
    if (lifetime <= 0f) {
      dead = true
    }
  }

  override fun render(batch: Batch) {
  }

  fun damage(enemy: Enemy) {
    val (forceX, forceY) = body.position angleTo enemy.position
    enemy.body.applyForceToCenter(forceX * pushback, forceY * pushback, true)
    enemy.health -= damage
  }
}