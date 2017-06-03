package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.ownedoutcomes.view.logic.EntityType
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin

interface Entity : Comparable<Entity> {
  val body: Body
  val dead: Boolean
  var destination: Vector2?
  val entityType: EntityType
  val position: Vector2
    get() = body.position

  fun update(delta: Float)
  fun render(batch: Batch)
  fun destroy()

  fun goTo(x: Float, y: Float) {
    val currentDestination = destination
    if (currentDestination != null) {
      currentDestination.set(x, y)
    } else {
      destination = vec2(x, y)
    }
  }
}

abstract class AbstractEntity(
    override final val body: Body,
    override val entityType: EntityType,
    spriteName: String) : Entity {
  override var destination: Vector2? = null
  private val fixture = body.fixtureList[0]
  open val speed: Float = 500f
  override var dead: Boolean = false
    protected set

  val sprite = Scene2DSkin.defaultSkin.atlas.createSprite(spriteName)
  private var rotatingLeft = true
  private val rotationSpeed = 50f
  private val rotationAngle = 10f
  private val initialOffsetY = -1f
  protected open var offsetX = -3f
  protected open var offsetY = initialOffsetY
  private var jumping = false
  private val jumpingSpeed = 9f
  private val jumpingHeight = 0.25f

  init {
    @Suppress("LeakingThis")
    body.userData = this
    body.fixtureList.forEach { it.userData = entityType }
  }

  override fun update(delta: Float) {
    animateSprite(delta)
    updateMovement(delta)
  }

  protected fun animateSprite(delta: Float) {
    destination?.let {
      val targetX = body.position.x - it.x
      sprite.setFlip(targetX > 0f, false)
    }
    if (rotatingLeft) {
      sprite.rotation = sprite.rotation + delta * rotationSpeed
      if (sprite.rotation >= rotationAngle) {
        rotatingLeft = false
      }
    } else {
      sprite.rotation = sprite.rotation - delta * rotationSpeed
      if (sprite.rotation <= -rotationAngle) {
        rotatingLeft = true
      }
    }
    if (jumping) {
      offsetY += delta * jumpingSpeed
      if (offsetY >= jumpingHeight) {
        jumping = false
      }
    } else if (offsetY != initialOffsetY) {
      offsetY = Math.max(offsetY - jumpingSpeed * delta, initialOffsetY)
    }
  }

  protected fun updateMovement(delta: Float) {
    destination?.let {
      val position = body.position
      if (fixture.testPoint(it)) {
        destination = null
        return
      }
      val angle = MathUtils.atan2(it.y - position.y, it.x - position.x)
      val forceX = MathUtils.cos(angle)
      val forceY = MathUtils.sin(angle)
      body.applyForceToCenter(forceX * speed * delta, forceY * speed * delta, true)
    }
  }

  fun hop() {
    jumping = true
  }

  protected fun setSpriteSize(width: Float, height: Float) {
    sprite.setSize(width, height)
  }

  override fun compareTo(other: Entity): Int {
    val (x, y) = position
    val (otherX, otherY) = other.position
    return when {
      y > otherY -> -1
      y < otherY -> 1
      x > otherX -> -1
      x < otherX -> 1
      else -> 0
    }
  }

  override fun render(batch: Batch) {
    val renderX = position.x + offsetX
    val renderY = position.y + offsetY
    sprite.setPosition(renderX, renderY)
    sprite.draw(batch)
  }

  override fun destroy() {
    body.world.destroyBody(body)
  }
}