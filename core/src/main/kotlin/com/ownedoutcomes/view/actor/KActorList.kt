package com.ownedoutcomes.view.actor

import com.badlogic.gdx.scenes.scene2d.Group
import ktx.scene2d.KGroup
import ktx.scene2d.actor

class KActorList : Group(), KGroup

inline fun group(init: KActorList.() -> Unit) = actor(KActorList(), init)