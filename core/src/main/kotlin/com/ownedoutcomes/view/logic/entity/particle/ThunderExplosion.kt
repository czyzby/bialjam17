package com.ownedoutcomes.view.logic.entity.particle

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.EntityType.PARTICLE
import com.ownedoutcomes.view.logic.entity.Entity
import ktx.actors.alpha
import ktx.actors.then
import ktx.scene2d.Scene2DSkin

class ThunderExplosion(
    override val body: Body,
    override val position: Vector2,
    val angle: Float) : Entity {
  override var destination: Vector2? = null
  override val entityType: EntityType = PARTICLE
  override var dead = false
  val skin = Scene2DSkin.defaultSkin
  val image: Image = Image(skin, "thunder").apply {
    setSize(3f, 3f)
    position.y -= 2f
    setOrigin(width / 2f, height / 2f)
    alpha = 0f
    rotation = angle * MathUtils.radiansToDegrees
    addAction(Actions.fadeIn(0.1f)
        then Actions.delay(0.2f)
        then Actions.fadeOut(0.1f)
        then Actions.run { dead = true })
  }
  val startImage: Image = Image(skin, "thunder-start").apply {
    setSize(image.width, image.height)
    setOrigin(width / 2f, height / 2f)
    alpha = 0f
    setPosition(position.x, position.y)
    rotation = image.rotation
  }
  val distanceX = MathUtils.cos(angle)
  val distanceY = MathUtils.sin(angle)

  override fun update(delta: Float) {
    image.act(delta)
    startImage.alpha = image.alpha
  }

  override fun render(batch: Batch) {
    startImage.draw(batch, 1f)
    for (part in 1..30) {
      image.setPosition(
          position.x + distanceX * part * (image.width / 2f),
          position.y + distanceY * part * (image.height / 2f))
      image.draw(batch, 1f)
    }
  }

  override fun destroy() {
  }
}