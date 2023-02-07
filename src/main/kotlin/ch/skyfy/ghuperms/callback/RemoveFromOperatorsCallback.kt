package ch.skyfy.ghuperms.callback

import com.mojang.authlib.GameProfile
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

@FunctionalInterface
fun interface RemoveFromOperatorsCallback {

    companion object {
        val EVENT: Event<RemoveFromOperatorsCallback> = EventFactory.createArrayBacked(RemoveFromOperatorsCallback::class.java) { listeners ->
            RemoveFromOperatorsCallback { profile -> for (listener in listeners) listener.onRemoveFromOperators(profile) }
        }
    }

    fun onRemoveFromOperators(profile: GameProfile)

}