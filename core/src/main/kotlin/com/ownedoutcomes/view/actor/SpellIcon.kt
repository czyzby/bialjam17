package com.ownedoutcomes.view.actor

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.ownedoutcomes.view.logic.Spell
import com.ownedoutcomes.view.logic.Spell.Companion
import com.ownedoutcomes.view.logic.randomSpellId
import ktx.actors.alpha
import ktx.actors.plus
import ktx.actors.then
import ktx.async.ktxAsync
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.table

class SpellIcon(
    var currentSpell: Spell = Spell.getRandomSpell()) {
  private var used = false
  private lateinit var icon: Image
  private lateinit var label: Label
  val actor = table {
    icon = image("spell${currentSpell.ordinal}").cell(padBottom = -64f, row = true)
    label = label("", style = "decorative")
  }

  fun useSpell(): Boolean {
    !used || return false
    used = true
    icon.alpha = 0.2f
    icon + Actions.fadeIn(10f, Interpolation.fade)
    randomizeSpell()
    ktxAsync {
      for (time in 0..9) {
        label.setText((10 - time).toString())
        label.alpha = 1f
        label + (
            Actions.alpha(0.7f, 1f)
                then Actions.moveBy(0f, 10f, 0.1f, Interpolation.bounceOut)
                then Actions.moveBy(0f, -10f, 0.1f, Interpolation.bounceIn))
        delay(1f)
      }
      label.setText("")
      used = false
      icon.alpha = 1f
    }
    return true
  }

  private fun randomizeSpell() {
    currentSpell = Companion.getRandomSpell()
    icon.setDrawable(Scene2DSkin.defaultSkin, "spell${currentSpell.ordinal}")
  }

  fun reset() {
    label.setText("")
    label.clearActions()
    label.alpha = 1f

    randomizeSpell()
    icon.clearActions()
    icon.alpha = 1f

    used = false
  }
}