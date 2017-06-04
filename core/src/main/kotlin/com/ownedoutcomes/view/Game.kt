package com.ownedoutcomes.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable.enabled
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.ownedoutcomes.initialHealthAmount
import com.ownedoutcomes.view.actor.SpellIcon
import com.ownedoutcomes.view.logic.GameManager
import ktx.actors.*
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.async.ktxAsync
import ktx.collections.gdxArrayOf
import ktx.collections.gdxListOf
import ktx.scene2d.*
import ktx.style.get

class Game(
    val stage: Stage,
    val batch: Batch
) : KtxScreen {
  val spells = gdxArrayOf<SpellIcon>()
  val hearts = mutableListOf<Image>()
  var gameManager = createGameManager()
  val inputProcessor = object : KtxInputAdapter {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
      gameManager.handleClick(screenX.toFloat(), screenY.toFloat())
      return false
    }
  }
  val inputListener = Actor().apply {
    onKeyDown { _, _, keyCode ->
      if (gameManager.player.health > 0) {
        when (keyCode) {
          Keys.Q -> spells[0]
          Keys.W -> spells[1]
          Keys.E -> spells[2]
          Keys.R -> spells[3]
          else -> null
        }?.let {
          val spell = it.currentSpell
          if (it.useSpell()) {
            val x = Gdx.input.x.toFloat()
            val y = Gdx.input.y.toFloat()
            gameManager.handleSpell(x, y, spell)
          }
        }
      }
    }
  }
  val view = table {
    setFillParent(true)
    align(Align.bottom)

    table {
      padBottom(15f)
      table {
        repeat(initialHealthAmount) {
          hearts.add(image("heart").cell(width = 32f, height = 32f, pad = 2f))
        }
      }.cell(row = true, growX = true)
      table {
        for (spellId in arrayOf('Q', 'W', 'E', 'R')) {
          val icon = SpellIcon()
          add(icon.actor).pad(5f)
          spells.add(icon)
        }
      }
    }
    pack()
  }
  val lossDialog = dialog(title = "") {
    image("loss")
    touchable = enabled
    onClick { _, _ ->
      hide(Actions.run { this@Game.reset() } then Actions.fadeOut(0.4f))
    }
  }

  private fun createGameManager() = GameManager(batch, Scene2DSkin.defaultSkin["background"]) {
    updateHeartsPanel(it)
  }

  override fun show() {
    stage.addActor(view)
    stage.addActor(inputListener)
    inputListener.setKeyboardFocus()
    Gdx.input.inputProcessor = InputMultiplexer(inputProcessor, stage)
  }

  fun reset() {
    gameManager = createGameManager()
    hearts.forEach {
      it.clearActions()
      it.alpha = 5f
      // TODO remove extra?
    }
    spells.forEach { it.reset() }
  }

  override fun render(delta: Float) {
    gameManager.update(delta)
    gameManager.render()
    stage.act(delta)
    stage.draw()
  }

  fun updateHeartsPanel(health: Int) {
    if (health == 0) {
      lossDialog.show(stage)
    }
    hearts.forEachIndexed { index, icon ->
      if (index < health) {
        if (icon.alpha < 1f) {
          icon.clearActions()
          icon.addAction(Actions.fadeIn(0.25f, Interpolation.fade) then Actions.alpha(1f))
        }
      } else if (icon.alpha > 0f) {
        icon.clearActions()
        icon.addAction(Actions.fadeOut(0.25f) then Actions.alpha(0f))
      }
    }
  }

  override fun hide() {
    spells.forEach(SpellIcon::reset)
  }
}
