package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ownedoutcomes.view.logic.EntityType.ORB
import com.ownedoutcomes.view.logic.projectileCategory
import com.ownedoutcomes.view.logic.projectileMask
import ktx.actors.then
import ktx.box2d.body
import ktx.box2d.filter
import ktx.box2d.ropeJointWith
import ktx.math.component1
import ktx.math.component2
import ktx.scene2d.Scene2DSkin

class Orb(
    world: World,
    val player: Player,
    x: Float,
    y: Float) : AbstractEntity(world.body {
  type = DynamicBody
  linearDamping = 1f
  position.set(x, y)
  circle(radius = 1f) {
    density = 0.001f
    isSensor = true
    filter {
      categoryBits = projectileCategory
      maskBits = projectileMask
    }
  }
}, entityType = ORB, spriteName = "spell1") {
  val pushback = 20000f
  var lifetime = 5f
  override var speed: Float = 10f
  override var offsetX: Float = -1.25f
  override var offsetY: Float = -1.25f
  val image = Image(Scene2DSkin.defaultSkin, "spell1")

  init {
    image.setSize(2.5f, 2.5f)
    image.setScale(0f)
    image.addAction(Actions.scaleTo(1f, 1f, 1f, Interpolation.bounceOut)
        then Actions.delay(lifetime - 2f)
        then Actions.scaleTo(0f, 0f, 1f, Interpolation.bounceIn))
    body.ropeJointWith(player.body) {
      maxLength = 5f
    }
  }

  override fun update(delta: Float) {
    image.act(delta)
    lifetime -= delta
    if (lifetime <= 0f) {
      dead = true
    }
    move(delta)
  }

  fun move(delta: Float) {
    val (x, y) = position
    val (playerX, playerY) = player.position
    val angle = MathUtils.atan2(y - playerY, x - playerX)
    val circularMovementAngle = angle + MathUtils.PI / 3f
    val forceX = MathUtils.cos(angle)
    val forceY = MathUtils.sin(angle)
    body.applyForceToCenter(forceX * speed * delta, forceY * speed * delta, true)
    val circularForceX = MathUtils.cos(circularMovementAngle)
    val circularForceY = MathUtils.sin(circularMovementAngle)
    body.applyForceToCenter(circularForceX * speed * delta, circularForceY * speed * delta, true)
  }

  override fun render(batch: Batch) {
    val renderX = position.x + offsetX
    val renderY = position.y + offsetY
    image.setPosition(renderX, renderY)
    image.draw(batch, 1f)
  }
}