package com.ownedoutcomes.view.logic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import ktx.assets.toInternalFile
import ktx.collections.toGdxArray

class SoundManager {
  val goblinHitSounds = (0..2)
      .map { Gdx.audio.newMusic("sound/goblin-damage$it.ogg".toInternalFile()) }
      .toGdxArray()
  val goblinAttackSounds = (0..2)
      .map { Gdx.audio.newMusic("sound/goblin-attack$it.ogg".toInternalFile()) }
      .toGdxArray()
  val witchHitSounds = (0..1)
      .map { Gdx.audio.newMusic("sound/witch-damage$it.ogg".toInternalFile()) }
      .toGdxArray()
  val fireballShoot = sound("sound/fireball-shoot.ogg")
  val ice = sound("sound/ice.ogg")
  val heal = sound("sound/heal.ogg")
  val wave = sound("sound/wave.ogg")
  val teleport = sound("sound/teleport.ogg")
  val fireball = sound("sound/fireball.ogg")
  val thunder = sound("sound/thunder.ogg")
  val orb = sound("sound/orb.ogg")

  private fun sound(path: String): Sound = Gdx.audio.newSound(path.toInternalFile())
  fun playGoblinHitSound() {
    goblinHitSounds.random().apply {
      if (!isPlaying) {
        play()
      }
    }
  }

  fun playGoblinAttackSound() {
    goblinAttackSounds.random().apply {
      if (!isPlaying) {
        play()
      }
    }
  }

  fun playWitchHitSound() {
    witchHitSounds.random().apply {
      if (!isPlaying) {
        play()
      }
    }
  }
}