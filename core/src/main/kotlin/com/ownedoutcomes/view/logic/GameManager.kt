package com.ownedoutcomes.view.logic

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.ownedoutcomes.view.logic.entity.Enemy
import com.ownedoutcomes.view.logic.entity.Entity
import com.ownedoutcomes.view.logic.entity.Player
import ktx.app.use
import ktx.box2d.createWorld
import ktx.collections.gdxArrayOf
import ktx.collections.gdxSetOf
import ktx.math.vec3

class GameManager(
    val batch: Batch,
    val background: TextureRegion) {
  val camera = OrthographicCamera(32f, 32f)
  val debugRenderer = Box2DDebugRenderer()
  val world = createWorld()
  val temp = vec3()
  val player = Player(world)
  val enemies = gdxSetOf<Enemy>()
  val entities = gdxArrayOf<Entity>()
  var timeToEnemySpawn = 0f

  val cameraMovementSpeed = 3.5f
  val backgroundSize = 512 / 32
  val backgroundRenderSize = 544f / 32f
  val spawningOffset = 20f
  val contactManager = ContactManager().apply {
    world.setContactListener(this)
  }

  fun update(delta: Float) {
    spawnEnemies(delta)
    enemies.forEach { it.update(delta) }
    player.update(delta)
    entities.add(player)
    updateCamera(delta)
    world.step(delta, 8, 3)
  }

  private fun spawnEnemies(delta: Float) {
    timeToEnemySpawn -= delta
    if (timeToEnemySpawn <= 0f) {
      val (x, y) = when (MathUtils.random(1, 4)) {
        1 -> spawningOffset to randomSpawningPosition()
        2 -> -spawningOffset to randomSpawningPosition()
        3 -> randomSpawningPosition() to spawningOffset
        else -> randomSpawningPosition() to -spawningOffset
      }
      val playerPos = player.position
      val enemy = Enemy(world,
          x = playerPos.x + x,
          y = playerPos.y + y,
          player = player)
      enemies.add(enemy)
      timeToEnemySpawn = MathUtils.random(0.5f, 1.5f)
    }
  }

  private fun randomSpawningPosition() = MathUtils.random(-spawningOffset, spawningOffset)

  fun render() {
    batch.use {
      it.color = Color.WHITE
      it.projectionMatrix = camera.combined
      renderBackground(it)
      entities.sort()
      entities.forEach { it.render(batch) }
    }
    debugRenderer.render(world, camera.combined)
  }

  private fun renderBackground(it: Batch) {
    val worldStartX = camera.position.x - 16f
    val worldEndX = worldStartX.toInt() + 32
    val worldStartY = camera.position.y - 16f
    val worldEndY = worldStartY.toInt() + 32
    val backgroundX = findBackgroundPosition(worldStartX)
    val backgroundY = findBackgroundPosition(worldStartY)
    for (x in backgroundX..worldEndX step backgroundSize) {
      for (y in backgroundY..worldEndY step backgroundSize) {
        it.draw(background, x.toFloat(), y.toFloat(), backgroundRenderSize, backgroundRenderSize)
      }
    }
  }

  fun findBackgroundPosition(position: Float): Int {
    val background = (position - position % backgroundSize).toInt()
    return if (background <= 0f) background - backgroundSize else background
  }

  fun updateCamera(delta: Float) {
    val playerPos = player.position
    val cameraPos = camera.position
    if (playerPos.x < cameraPos.x) {
      val movementX = Math.min(-1f, playerPos.x - cameraPos.x) * cameraMovementSpeed
      camera.translate(movementX * delta, 0f)
      if (playerPos.x > camera.position.x) {
        camera.position.x = playerPos.x
      }
    } else if (playerPos.x > cameraPos.x) {
      val movementX = Math.max(1f, playerPos.x - cameraPos.x) * cameraMovementSpeed
      camera.translate(movementX * delta, 0f)
      if (playerPos.x < camera.position.x) {
        camera.position.x = playerPos.x
      }
    }
    if (playerPos.y < cameraPos.y) {
      val movementY = Math.min(-1f, playerPos.y - cameraPos.y) * cameraMovementSpeed
      camera.translate(0f, movementY * delta)
      if (playerPos.y > camera.position.y) {
        camera.position.y = playerPos.y
      }
    } else if (playerPos.y > cameraPos.y) {
      val movementY = Math.max(1f, playerPos.y - cameraPos.y) * cameraMovementSpeed
      camera.translate(0f, movementY * delta)
      if (playerPos.y < camera.position.y) {
        camera.position.y = playerPos.y
      }
    }
    camera.update()
  }

  fun handleClick(x: Float, y: Float) {
    unproject(x, y)
    player.goTo(temp.x, temp.y)
  }

  fun handleSpell(x: Float, y: Float, spell: Spell) {
    unproject(x, y)
    println("$spell at $x, $y, projected to ${temp.x}, ${temp.y}")
    player.hop()
  }

  private fun unproject(x: Float, y: Float) {
    temp.set(x, y, 0f)
    camera.unproject(temp)
  }
}