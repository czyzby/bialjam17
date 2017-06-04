package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ownedoutcomes.view.logic.EntityType.WAVE
import com.ownedoutcomes.view.logic.angleTo
import com.ownedoutcomes.view.logic.projectileCategory
import com.ownedoutcomes.view.logic.projectileMask
import ktx.actors.then
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.component1
import ktx.math.component2
import ktx.scene2d.Scene2DSkin

class Wave(
    world: World,
    x: Float,
    y: Float,
    angle: Float) : AbstractEntity(world.body {
  type = DynamicBody
  linearDamping = 1f
  bullet = true
  position.set(x, y)
  circle(radius = 2.1f) {
    density = 0.001f
    isSensor = true
    filter {
      categoryBits = projectileCategory
      maskBits = projectileMask
    }
  }
}, entityType = WAVE, spriteName = "wave") {
  val pushback = 60000f
  val damage = 1
  override var speed: Float = 11f
  override var offsetX: Float = -3f
  override var offsetY: Float = -3f
  val image = Image(Scene2DSkin.defaultSkin, "wave")

  init {
    image.setSize(6f, 6f)
    image.setOrigin(image.width / 2f, image.height / 2f)
    image.setScale(0f)
    image.rotation = angle * MathUtils.radiansToDegrees - 90f
    image.addAction(Actions.scaleTo(1f, 1f, 1f, Interpolation.exp10Out)
        then Actions.fadeOut(0.2f)
        then Actions.run { dead = true })
  }

  override fun update(delta: Float) {
    image.act(delta)
  }

  override fun render(batch: Batch) {
    val renderX = position.x + offsetX
    val renderY = position.y + offsetY
    image.setPosition(renderX, renderY)
    image.draw(batch, 1f)
  }

  fun damage(enemy: Enemy) {
    val (forceX, forceY) = body.position angleTo enemy.position
    enemy.body.applyForceToCenter(forceX * pushback, forceY * pushback, true)
    enemy.health -= damage
  }
}