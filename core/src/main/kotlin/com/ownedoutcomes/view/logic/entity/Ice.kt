package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ownedoutcomes.view.logic.EntityType.ICE
import com.ownedoutcomes.view.logic.GameManager
import com.ownedoutcomes.view.logic.angleTo
import com.ownedoutcomes.view.logic.entity.particle.FireballExplosion
import com.ownedoutcomes.view.logic.projectileCategory
import com.ownedoutcomes.view.logic.projectileMask
import ktx.actors.then
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.component1
import ktx.math.component2
import ktx.scene2d.Scene2DSkin

class Ice(
    val gameManager: GameManager,
    world: World,
    x: Float,
    y: Float) : AbstractEntity(world.body {
  type = DynamicBody
  position.set(x, y)
  circle(radius = 1.5f) {
    density = 500f
    isSensor = false
    filter {
      categoryBits = projectileCategory
      maskBits = projectileMask
    }
  }
}, entityType = ICE, spriteName = "ice") {
  val pushback = 10000f
  var lifetime = 5f
  override var speed: Float = 300000f
  override var offsetX: Float = -2.5f
  override var offsetY: Float = -1.5f
  val image = Image(Scene2DSkin.defaultSkin, "ice")

  init {
    image.setSize(5f, 5f)
    image.setScale(0f)
    image.setOrigin(2.5f, 0f)
    image.addAction(Actions.scaleTo(1f, 1f, 1f, Interpolation.exp10Out)
        then Actions.delay(lifetime - 2f)
        then Actions.scaleTo(0f, 0f, 1f, Interpolation.exp10In))
  }

  override fun update(delta: Float) {
    image.act(delta)
    lifetime -= delta
    if (lifetime <= 0f) {
      dead = true
    }
  }

  override fun render(batch: Batch) {
    val renderX = position.x + offsetX
    val renderY = position.y + offsetY
    image.setPosition(renderX, renderY)
    image.draw(batch, 1f)
  }
}