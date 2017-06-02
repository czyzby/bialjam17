package com.ownedoutcomes.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Align
import com.ownedoutcomes.stageHeight
import com.ownedoutcomes.stageWidth
import ktx.app.KtxScreen
import ktx.scene2d.buttonGroup
import ktx.scene2d.image
import ktx.scene2d.imageButton
import ktx.scene2d.table

class Game(
  val stage: Stage
) : KtxScreen {
  val view = table {
    setFillParent(true)
    align(Align.bottomLeft)
    pad(10f)
    // Game:
    table {
      for (y in 0..(stageHeight.toInt() / 50) - 1) {
        for (x in 0..(stageWidth.toInt() / 50) - 1) {
          image("grass").cell(height = 50f, width = 50f)
        }
        row()
      }
    }.cell(width = stageWidth, height = stageHeight, padBottom = -100f, row = true)

    // GUI:
    buttonGroup(minCheckedCount = 0, maxCheckedCount = 1) {
      background("brown")
      repeat(5) {
        imageButton {
          it.height(60f).width(70f).pad(5f)
        }
      }
    }.cell(growX = true, height = 90f)
    pack()
  }

  override fun show() {
    reset()
    stage.addActor(view)
    Gdx.input.inputProcessor = stage
  }

  override fun render(delta: Float) {
    stage.act(delta)
    stage.draw()
  }

  fun reset() {}
}
