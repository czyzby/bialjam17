package com.ownedoutcomes.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.ownedoutcomes.view.actor.SpellIcon
import com.ownedoutcomes.view.logic.GameManager
import ktx.actors.alpha
import ktx.actors.onKeyDown
import ktx.actors.setKeyboardFocus
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.async.ktxAsync
import ktx.collections.gdxArrayOf
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.progressBar
import ktx.scene2d.table
import ktx.style.get

class Game(
    val stage: Stage,
    batch: Batch
) : KtxScreen {
  val spells = gdxArrayOf<SpellIcon>()
  var gameManager = GameManager(batch, Scene2DSkin.defaultSkin["background"])
  val inputProcessor = object : KtxInputAdapter {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
      gameManager.handleClick(screenX.toFloat(), screenY.toFloat())
      return false
    }
  }
  val inputListener = Actor().apply {
    onKeyDown { _, _, keyCode ->
      when (keyCode) {
        Keys.Q -> spells[0]
        Keys.W -> spells[1]
        Keys.E -> spells[2]
        Keys.R -> spells[3]
        Keys.T -> spells[4]
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
  val view = table {
    setFillParent(true)
    align(Align.bottom)

    table {
      padBottom(15f)
      progressBar {
        value = 1f
        ktxAsync {
          while (value >= 0.5f) {
            skipFrame()
            value -= 0.011f
          }
        }
      }.cell(row = true, growX = true, pad = 15f)
      table {
        for (spellId in arrayOf('Q', 'W', 'E', 'R', 'T')) {
          val icon = SpellIcon()
          println(icon.currentSpell)
          add(icon.actor).pad(5f)
          spells.add(icon)
        }
      }
    }
    pack()
  }

  override fun show() {
    stage.addActor(view)
    stage.addActor(inputListener)
    inputListener.setKeyboardFocus()
    Gdx.input.inputProcessor = InputMultiplexer(inputProcessor, stage)
  }

  override fun render(delta: Float) {
    gameManager.update(delta)
    gameManager.render()
    stage.act(delta)
    stage.draw()
  }

  override fun hide() {
    spells.forEach(SpellIcon::reset)
  }
}
