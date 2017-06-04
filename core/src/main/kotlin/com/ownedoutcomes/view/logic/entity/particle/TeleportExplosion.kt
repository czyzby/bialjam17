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

class TeleportExplosion(
    override val body: Body,
    override val position: Vector2) : Entity {
  override var destination: Vector2? = null
  override val entityType: EntityType = PARTICLE
  override var dead = false
  val skin = Scene2DSkin.defaultSkin
  val image: Image = Image(skin, "spell4").apply {
    setSize(2.5f, 2.5f)
    setOrigin(width / 2f, height / 2f)
    position.y += 1f
    setPosition(position.x - width / 2f, position.y - width / 2f)
    setScale(0f)
    addAction(Actions.scaleTo(1f, 1f, 0.4f, Interpolation.bounceOut)
        then Actions.repeat(2,
        Actions.scaleTo(0.9f, 0.9f, 0.2f, Interpolation.sineOut)
            then Actions.scaleTo(1f, 1f, 0.2f, Interpolation.sineIn))
        then Actions.scaleTo(0f, 0f, 0.4f, Interpolation.bounceIn)
        then Actions.run { dead = true })
  }

  override fun update(delta: Float) {
    image.act(delta)
  }

  override fun render(batch: Batch) {
    image.draw(batch, 1f)
  }

  override fun destroy() {
  }
}