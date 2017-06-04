package com.ownedoutcomes.view.logic.entity.particle

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.EntityType.PARTICLE
import com.ownedoutcomes.view.logic.entity.Entity
import ktx.actors.then
import ktx.scene2d.Scene2DSkin

class HealExplosion(
    override val body: Body,
    override val position: Vector2) : Entity {
  override var destination: Vector2? = null
  override val entityType: EntityType = PARTICLE
  override var dead = false
  val skin = Scene2DSkin.defaultSkin
  val image: Image = Image(skin, "wings").apply {
    setSize(7f, 7f)
    setOrigin(width / 2f, 0f)
    position.y += 0.5f
    setPosition(position.x, position.y)
    setScale(0f)
    addAction(Actions.scaleTo(1f, 1f, 0.4f, Interpolation.swingIn)
        then Actions.delay(1f)
        then Actions.scaleTo(0f, 0f, 0.4f, Interpolation.swingOut)
        then Actions.run { dead = true })
  }

  override fun update(delta: Float) {
    position.set(body.position)
    position.y += 0.5f
    image.setPosition(position.x - image.width / 2f, position.y)
    image.act(delta)
  }

  override fun render(batch: Batch) {
    image.draw(batch, 1f)
  }

  override fun destroy() {
  }
}