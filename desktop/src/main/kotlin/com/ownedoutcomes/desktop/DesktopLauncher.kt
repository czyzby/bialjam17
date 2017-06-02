@file:JvmName("DesktopLauncher")

package com.ownedoutcomes.desktop

import com.badlogic.gdx.Files.FileType
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.ownedoutcomes.Application
import com.ownedoutcomes.screenHeight
import com.ownedoutcomes.screenWidth

fun main(args: Array<String>) {
  LwjglApplication(Application(), LwjglApplicationConfiguration().apply {
    title = "BialJam'17"
    width = screenWidth
    height = screenHeight
    resizable = false
    intArrayOf(128, 64, 32, 16).forEach {
      addIcon("libgdx$it.png", FileType.Internal)
    }
  })
}
