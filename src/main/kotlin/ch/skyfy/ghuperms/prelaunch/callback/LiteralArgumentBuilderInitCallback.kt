package ch.skyfy.ghuperms.prelaunch.callback

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.TypedActionResult

@FunctionalInterface
fun interface LiteralArgumentBuilderInitCallback {

    companion object {
        val EVENT: Event<LiteralArgumentBuilderInitCallback> = EventFactory.createArrayBacked(LiteralArgumentBuilderInitCallback::class.java) { listeners ->
            LiteralArgumentBuilderInitCallback { literal, instance ->
                return@LiteralArgumentBuilderInitCallback listeners.firstOrNull()?.onInit(literal, instance) ?: TypedActionResult.pass("")
            }
        }
    }

    fun onInit(literal: String, instance: LiteralArgumentBuilder<*>): TypedActionResult<String>

}