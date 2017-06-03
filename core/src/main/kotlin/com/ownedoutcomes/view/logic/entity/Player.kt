package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.view.logic.EntityType
import ktx.box2d.body
import ktx.scene2d.Scene2DSkin

class Player(world: World) : AbstractEntity(
    world.body {
      linearDamping = 10f
      fixedRotation = true
      type = DynamicBody
      circle {
        userData = EntityType.PLAYER
        density = 1f
      }
    }, entityType = EntityType.PLAYER, spriteName = "witch") {
  override val speed: Float = 16000f

  init {
    sprite.setSize(6f, 6f)
    sprite.setOrigin(3f, 1.5f)
  }
}
