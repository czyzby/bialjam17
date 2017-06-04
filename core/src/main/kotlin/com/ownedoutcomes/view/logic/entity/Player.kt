package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.initialHealthAmount
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.playerCategory
import com.ownedoutcomes.view.logic.playerMask
import kotlinx.coroutines.experimental.Job
import ktx.async.ktxAsync
import ktx.box2d.body
import ktx.box2d.filter
import ktx.scene2d.Scene2DSkin

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
    }, entityType = EntityType.PLAYER, spriteName = "witch0") {
  override val speed: Float = 24000f
  var level = 0
  var points: Int = 0
    set(value) {
      field = value
      when (value) {
        1 -> upgrade(1)
        2 -> upgrade(2)
        3 -> upgrade(3)
      }
    }
  var job: Job? = null
  var color = 1f
  var health = initialHealthAmount
    set(value) {
      val newValue = MathUtils.clamp(value, 0, initialHealthAmount)
      if (field != 0 && field != newValue) {
        field = newValue
        healthChangeCallback(newValue)
      }
    }
  val atlas = Scene2DSkin.defaultSkin.atlas

  init {
    setSpriteSize(6f, 6f)
    sprite.setOrigin(3f, 1.5f)
  }

  private fun upgrade(nextLevel: Int) {
    job?.cancel()
    level = nextLevel
    job = ktxAsync {
      while (color > 0f) {
        color = Math.max(0f, color - 0.05f)
        skipFrame()
      }
      sprite.setRegion(atlas.findRegion("witch$nextLevel"))
      while (color < 1f) {
        color = Math.min(1f, color + 0.05f)
        skipFrame()
      }
    }
  }

  override fun update(delta: Float) {
    super.update(delta)
    sprite.setColor(color, color, color, 1f)
  }
}
