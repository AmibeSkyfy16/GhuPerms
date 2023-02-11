package ch.skyfy.ghuperms.prelaunch.callback

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.TypedActionResult

@FunctionalInterface
fun interface CommandDispatcherOnRegisterCallback {

    companion object {
        @JvmField
        val EVENT: Event<CommandDispatcherOnRegisterCallback> = EventFactory.createArrayBacked(CommandDispatcherOnRegisterCallback::class.java) { listeners ->
            CommandDispatcherOnRegisterCallback { literal ->
                listeners.firstOrNull()?.onThen(literal)
            }
        }
    }

    fun onThen(literal: LiteralArgumentBuilder<*>)

}