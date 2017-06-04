package com.ownedoutcomes.view.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.ownedoutcomes.view.logic.EntityType.*
import com.ownedoutcomes.view.logic.entity.*
import ktx.math.component1
import ktx.math.component2

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
      typeA == ORB && typeB == ENEMY -> pushEnemy(entityA as Orb, entityB as Enemy)
      typeA == FIREBALL && typeB == ENEMY -> triggerFireball(entityA as Fireball, entityB as Enemy)
      typeA == ICE && typeB == ENEMY -> repel(entityA as Ice, entityB as Enemy)
    }
  }

  fun repel(ice: Ice, enemy: Enemy) {
    val (forceX, forceY) = ice.position angleTo enemy.position
    enemy.body.applyForceToCenter(forceX * ice.pushback, forceY * ice.pushback, true)
    enemy.health--
  }

  fun attackPlayer(player: Player, enemy: Enemy) {
    val (playerX, playerY) = player.position
    val (enemyX, enemyY) = enemy.position
    val angle = MathUtils.atan2(playerY - enemyY, playerX - enemyX)
    val forceX = MathUtils.cos(angle)
    val forceY = MathUtils.sin(angle)
    val speed = player.speed / 3f
    val enemySpeed = enemy.speed / 2f
    player.health--
    enemy.hop()
    player.body.applyForceToCenter(speed * forceX, speed * forceY, true)
    enemy.body.applyForceToCenter(enemySpeed * -forceX, enemySpeed * -forceY, true)
  }

  fun pushEnemy(orb: Orb, enemy: Enemy) {
    val (playerX, playerY) = orb.player.position
    val (enemyX, enemyY) = enemy.position
    val angle = MathUtils.atan2(enemyY - playerY, enemyX - playerX)
    val forceX = MathUtils.cos(angle)
    val forceY = MathUtils.sin(angle)
    enemy.body.applyForceToCenter(forceX * orb.pushback, forceY * orb.pushback, true)
    enemy.health--
  }

  fun triggerFireball(fireball: Fireball, enemy: Enemy) {
    fireball.dead = true
    fireball.damage(enemy)
  }

  override fun endContact(contact: Contact) {
  }

  override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
  }

  override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
  }
}