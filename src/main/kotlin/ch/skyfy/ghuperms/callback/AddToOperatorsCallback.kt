package ch.skyfy.ghuperms.callback

import com.mojang.authlib.GameProfile
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

@FunctionalInterface
fun interface AddToOperatorsCallback {

    companion object {
        val EVENT: Event<AddToOperatorsCallback> = EventFactory.createArrayBacked(AddToOperatorsCallback::class.java) { listeners ->
            AddToOperatorsCallback { profile -> for (listener in listeners) listener.onAddToOperators(profile) }
        }
    }

    fun onAddToOperators(profile: GameProfile)

}