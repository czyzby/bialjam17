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

class FireballExplosion(
    override val body: Body,
    override val position: Vector2) : Entity {
  override var destination: Vector2? = null
  override val entityType: EntityType = PARTICLE
  override var dead = false
  val skin = Scene2DSkin.defaultSkin
  val image: Image = Image(skin, "explosion").apply {
    setSize(12f, 12f)
    setOrigin(width / 2f, height / 2f)
    setPosition(position.x - width / 2f, position.y - width / 2f)
    setScale(0f)
    addAction(Actions.parallel(
        Actions.scaleTo(1f, 1f, 0.4f, Interpolation.bounceOut)
            then Actions.delay(0.1f)
            then Actions.scaleTo(0f, 0f, 0.4f, Interpolation.bounceIn),
        Actions.repeat(3, Actions.rotateBy(30f, 0.15f, Interpolation.fade)
            then Actions.rotateBy(-30f, 0.15f, Interpolation.fade))
    ) then Actions.run { dead = true })
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