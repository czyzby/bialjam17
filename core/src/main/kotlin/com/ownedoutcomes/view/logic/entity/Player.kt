package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.initialHealthAmount
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.playerCategory
import com.ownedoutcomes.view.logic.playerMask
import ktx.box2d.body
import ktx.box2d.filter

class Player(
    world: World,
    val healthChangeCallback: (Int) -> Unit) : AbstractEntity(
    world.body {
      linearDamping = 10f
      fixedRotation = true
      type = DynamicBody
      circle {
        density = 1f
        filter {
          categoryBits = playerCategory
          maskBits = playerMask
        }
      }
    }, entityType = EntityType.PLAYER, spriteName = "witch") {
  override val speed: Float = 24000f
  var health = initialHealthAmount
    set(value) {
      val newValue = MathUtils.clamp(value, 0, initialHealthAmount)
      if (field != 0 && field != newValue) {
        field = newValue
        healthChangeCallback(newValue)
      }
    }

  init {
    setSpriteSize(6f, 6f)
    sprite.setOrigin(3f, 1.5f)
  }

  override fun update(delta: Float) {
    super.update(delta)
  }
}
