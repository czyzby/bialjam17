package com.ownedoutcomes.view.logic

import com.badlogic.gdx.Gdx
import ktx.assets.toInternalFile
import ktx.collections.toGdxArray

class SoundManager {
  val soundVolume = 0.8f
  val goblinHitSounds = (0..2)
      .map { Gdx.audio.newMusic("sound/goblin-damage$it.ogg".toInternalFile()) }
      .toGdxArray()
  val goblinAttackSounds = (0..2)
      .map { Gdx.audio.newMusic("sound/goblin-attack$it.ogg".toInternalFile()) }
      .toGdxArray()
  val witchHitSounds = (0..1)
      .map { Gdx.audio.newMusic("sound/witch-damage$it.ogg".toInternalFile()) }
      .toGdxArray()

  fun playGoblinHitSound() {
    goblinHitSounds.random().apply {
      if (!isPlaying) {
        volume = soundVolume
        play()
      }
    }
  }

  fun playGoblinAttackSound() {
    goblinAttackSounds.random().apply {
      if (!isPlaying) {
        volume = soundVolume
        play()
      }
    }
  }

  fun playWitchHitSound() {
    witchHitSounds.random().apply {
      if (!isPlaying) {
        volume = soundVolume
        play()
      }
    }
  }
}