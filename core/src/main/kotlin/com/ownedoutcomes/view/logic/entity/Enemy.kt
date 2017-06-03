package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.view.logic.EntityType
import ktx.box2d.body

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
        userData = EntityType.ENEMY
        density = 1f
      }
    }, entityType = EntityType.ENEMY, spriteName = "witch") {
  override val speed: Float = 12000f

  override fun update(delta: Float) {
    destination = player.position
    super.update(delta)
  }

  override fun render(batch: Batch) {
    // TODO
  }
}