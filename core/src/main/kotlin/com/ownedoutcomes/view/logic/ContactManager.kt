package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.ownedoutcomes.view.logic.EntityType.ENEMY
import com.ownedoutcomes.view.logic.EntityType.PLAYER
import com.ownedoutcomes.view.logic.entity.Enemy
import com.ownedoutcomes.view.logic.entity.Entity
import com.ownedoutcomes.view.logic.entity.Player

class ContactManager : ContactListener {
  override fun beginContact(contact: Contact) {
    val fixtureA = contact.fixtureA
    val fixtureB = contact.fixtureB
    if (fixtureA.userData != null && fixtureB.userData != null) {
      val entityB = fixtureB.body.userData as Entity
      val entityA = fixtureA.body.userData as Entity
      beginContact(entityA, entityB)
      beginContact(entityB, entityA)
    }
  }

  fun beginContact(entityA: Entity, entityB: Entity) {
    val typeA = entityA.entityType
    val typeB = entityB.entityType
    when {
      typeA == PLAYER && typeB == ENEMY -> attackPlayer(entityA as Player, entityB as Enemy)
    }
  }

  fun attackPlayer(player: Player, enemy: Enemy) {
    val playerPos = player.position
    val enemyPos = enemy.position
    val angle = MathUtils.atan2(playerPos.y - enemyPos.y, playerPos.x - enemyPos.x)
    val forceX = MathUtils.cos(angle)
    val forceY = MathUtils.sin(angle)
    val speed = player.speed / 5f
    val enemySpeed = enemy.speed / 6f
    // TODO Animate enemy, damage player
    player.body.applyForceToCenter(speed * forceX, speed * forceY, true)
    enemy.body.applyForceToCenter(enemySpeed * -forceX, enemySpeed * -forceY, true)
  }

  override fun endContact(contact: Contact) {
  }

  override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
  }

  override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
  }
}