package com.ownedoutcomes.view.actor

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.ownedoutcomes.view.logic.Spell
import kotlinx.coroutines.experimental.Job
import ktx.actors.alpha
import ktx.actors.plus
import ktx.actors.then
import ktx.async.ktxAsync
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.table

class SpellIcon(
    val utility: Boolean,
    var currentSpell: Spell = Spell.getRandomSpell(utility)) {
  private var used = false
  private lateinit var icon: Image
  private lateinit var label: Label
  var job: Job? = null
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
    job = ktxAsync {
      for (time in 0..9) {
        label.setText((10 - time).toString())
        label.alpha = 1f
        label + (
            Actions.alpha(0.7f, 1f)
                then Actions.moveBy(0f, 15f, 0.2f, Interpolation.fade)
                then Actions.moveBy(0f, -15f, 0.2f, Interpolation.fade))
        delay(1f)
      }
      label.setText("")
      used = false
      icon.alpha = 1f
    }
    return true
  }

  private fun randomizeSpell() {
    currentSpell = Spell.getRandomSpell(utility)
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
    job?.cancel()
  }
}