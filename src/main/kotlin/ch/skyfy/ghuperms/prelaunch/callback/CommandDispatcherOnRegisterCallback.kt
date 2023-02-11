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
                return@CommandDispatcherOnRegisterCallback listeners.firstOrNull()?.onThen(literal) ?: TypedActionResult.pass("")
            }
        }
    }

    fun onThen(literal: LiteralArgumentBuilder<*>): TypedActionResult<String>

}