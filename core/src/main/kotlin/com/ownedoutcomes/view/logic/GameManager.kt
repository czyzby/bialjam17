package com.ownedoutcomes.view.logic

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.ownedoutcomes.view.logic.entity.Enemy
import com.ownedoutcomes.view.logic.entity.Entity
import com.ownedoutcomes.view.logic.entity.Player
import ktx.app.color
import ktx.app.use
import ktx.box2d.createWorld
import ktx.collections.gdxArrayOf
import ktx.collections.iterate
import ktx.math.vec3
import ktx.scene2d.Scene2DSkin

class GameManager(
    val batch: Batch,
    val background: TextureRegion,
    skin: Skin = Scene2DSkin.defaultSkin,
    healthChangeCallback: (Int) -> Unit) {
  val camera = OrthographicCamera(32f, 32f)
  val debugRenderer = Box2DDebugRenderer()
  val world = createWorld()
  val temp = vec3()
  val player = Player(world, healthChangeCallback)
  val entities = gdxArrayOf<Entity>()
  var timeToEnemySpawn = 4f
  val cameraMovementSpeed = 3.5f
  val backgroundSize = 512 / 64
  val backgroundRenderSize = 544f / 64f
  val spawningOffset = 24f
  val destinationCursor: Sprite = skin.atlas.createSprite("destination").apply {
    setSize(2f, 2f)
  }
  val lightSystem = RayHandler(world).apply {
    setAmbientLight(0f, 0f, 0f, 0.7f)
    setBlur(true)
  }
  val light = PointLight(lightSystem, 100, color(1f, 1f, 1f, 0.6f), 20f, 0f, 0f).apply {
    isSoft = false
    setContactFilter(lightCategory, 0, lightMask)
  }
  val heartSprite: Sprite = skin.atlas.createSprite("heart").apply {
    setSize(1f, 1f)
  }

  init {
    entities.add(player)
    ContactManager().apply {
      world.setContactListener(this)
    }
  }

  fun update(delta: Float) {
    spawnEnemies(delta)
    entities.forEach { it.update(delta) }
    light.position = player.position
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
          gameManager = this)
      entities.add(enemy)
      timeToEnemySpawn = MathUtils.random(2f, 3f)
    }
  }

  private fun randomSpawningPosition() = MathUtils.random(-spawningOffset, spawningOffset)

  fun render() {
    batch.use {
      it.color = Color.WHITE
      it.projectionMatrix = camera.combined
      renderBackground(it)
      player.destination?.let { position ->
        destinationCursor.setPosition(position.x - 1f, position.y - 1f)
        destinationCursor.draw(it)
      }
      entities.sort()
      entities.iterate { entity, iterator ->
        entity.render(batch)
        if (entity.dead) {
          iterator.remove()
          entity.destroy()
        }
      }
    }
    lightSystem.setCombinedMatrix(camera)
    lightSystem.updateAndRender()
    debugRenderer.render(world, camera.combined) // TODO remove
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
    spell.use(this, temp.x, temp.y)
    player.hop()
  }

  private fun unproject(x: Float, y: Float) {
    temp.set(x, y, 0f)
    camera.unproject(temp)
  }
}