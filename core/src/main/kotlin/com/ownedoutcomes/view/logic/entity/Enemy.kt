package com.ownedoutcomes.view.logic.entity

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.ownedoutcomes.enemiesAmount
import com.ownedoutcomes.view.logic.EntityType
import com.ownedoutcomes.view.logic.enemyCategory
import com.ownedoutcomes.view.logic.enemyMask
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.component1
import ktx.math.component2

// TODO Chyba zwolnij wrogow
// TODO EXP, levele, upgrade spelli
// TODO Przynajmniej ze 3-4 spelle
// TODO portal nie leczy, inna ikona
// TODO MUZYKA, dzwieki
// TODO (maybe) podmiana backgrounda, main menu
// TODO REPLAY
// TODO Bonusy?

class Enemy(
    world: World,
    x: Float,
    y: Float,
    val heartSprite: Sprite,
    val player: Player) : AbstractEntity(
    world.body {
      position.set(x, y)
      linearDamping = 10f
      fixedRotation = true
      type = DynamicBody
      circle {
        density = 1f
        filter {
          categoryBits = enemyCategory
          maskBits = enemyMask
        }
      }
    }, entityType = EntityType.ENEMY, spriteName = "goblin${MathUtils.random(enemiesAmount - 1)}") {
  override val speed: Float = 23500f
  var health = 3

  init {
    setSpriteSize(6f, 6f)
    sprite.setOrigin(3f, 1.5f)
  }

  override fun update(delta: Float) {
    destination = player.position
    super.update(delta)
    if (health <= 0) {
      // TODO don't immediately remove? add particle?
      dead = true
    }
  }

  override fun render(batch: Batch) {
    super.render(batch)
    val (x, y) = position
    repeat(health) {
      heartSprite.setPosition(x - 0.4f + (it - 1), y - 2f)
      heartSprite.draw(batch)
    }
  }
}