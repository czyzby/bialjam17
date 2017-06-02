package com.ownedoutcomes.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.ownedoutcomes.stageHeight
import com.ownedoutcomes.stageWidth
import com.ownedoutcomes.view.actor.group
import ktx.actors.onClick
import ktx.app.KtxScreen
import ktx.scene2d.*

class Game(
    val stage: Stage
) : KtxScreen {
  val spritesView = group {
  }
  val view = table {
    setFillParent(true)
    align(Align.bottomLeft)
    // Game:
    table {
      val tilesY = (stageHeight.toInt() / 50)
      val tilesX = (stageWidth.toInt() / 50)
      for (y in 0..tilesY - 1) {
        for (x in 0..tilesX - 1) {
          val dirtStart = 2
          val dirtEnd = 7
          image(when {
            x == dirtStart && y == dirtStart -> "grass_nw"
            x == dirtEnd && y == dirtStart -> "grass_ne"
            x in dirtStart..dirtEnd && y == dirtStart -> "grass_n"
            x == dirtStart && y == dirtEnd -> "grass_sw"
            x == dirtEnd && y == dirtEnd -> "grass_se"
            x in dirtStart..dirtEnd && y == dirtEnd -> "grass_s"
            x == dirtStart && y in dirtStart..dirtEnd -> "grass_w"
            x == dirtEnd && y in dirtStart..dirtEnd -> "grass_e"
            x in dirtStart..dirtEnd && y in dirtStart..dirtEnd -> "dirt"
            else -> "grass"
          }).cell(height = 50f, width = 50f)
        }
        row()
      }
    }.cell(width = stageWidth, height = stageHeight, padBottom = -110f, row = true)

    // GUI:
    buttonGroup(minCheckedCount = 0, maxCheckedCount = 1) {
      background("brown")
      repeat(5) { index ->
        imageButton(style = "tower$index") {
          it.height(60f).width(70f)
              .padBottom(5f).padTop(5f).padLeft(10f).padRight(10f)
        }
      }
    }.cell(growX = true, height = 90f, pad = 10f)
    pack()
  }

  override fun show() {
    reset()
    stage.addActor(view)
    stage.addActor(spritesView)
    Gdx.input.inputProcessor = stage
  }

  override fun render(delta: Float) {
    stage.act(delta)
    stage.draw()
  }

  fun reset() {}
}
