package com.ownedoutcomes

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.ownedoutcomes.view.Game
import com.ownedoutcomes.view.Menu
import ktx.app.KtxGame
import ktx.assets.toInternalFile
import ktx.async.enableKtxCoroutines
import ktx.inject.Context
import ktx.scene2d.Scene2DSkin
import ktx.style.defaultStyle
import ktx.style.label
import ktx.style.progressBar
import ktx.style.skin

class Application : KtxGame<Screen>() {
  val context = Context()

  override fun create() {
    enableKtxCoroutines(asynchronousExecutorConcurrencyLevel = 1)
    context.register {
      bindSingleton(TextureAtlas("skin.atlas"))
      bindSingleton<Batch>(SpriteBatch())
      bindSingleton<Viewport>(ScreenViewport())
      bindSingleton(Stage(inject(), inject()))
      bindSingleton(createSkin(inject()))
      Scene2DSkin.defaultSkin = inject()
      bindSingleton(this@Application)
      bindSingleton(Menu(inject(), inject()))
      bindSingleton(Game(inject(), inject()))
    }

    addScreen(context.inject<Menu>())
    addScreen(context.inject<Game>())
    setScreen<Menu>()
  }

  fun createSkin(atlas: TextureAtlas): Skin = skin(atlas) { skin ->
    add(defaultStyle, BitmapFont())
    add("decorative", FreeTypeFontGenerator("decorative.ttf".toInternalFile())
        .generateFont(FreeTypeFontParameter().apply {
          borderWidth = 2f
          borderColor = Color.GRAY
          size = 50
        }))
    label {
      font = skin.getFont(defaultStyle)
    }
    label("decorative") {
      font = skin.getFont("decorative")
    }
  }

  override fun dispose() {
    context.dispose()
  }
}
