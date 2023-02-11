package ch.skyfy.ghuperms.prelaunch.callback

import ch.skyfy.ghuperms.prelaunch.mixin.LiteralAccessor
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.TypedActionResult

@FunctionalInterface
fun interface ArgumentBuilderOnThenCallback {

    companion object {
        val EVENT: Event<ArgumentBuilderOnThenCallback> = EventFactory.createArrayBacked(ArgumentBuilderOnThenCallback::class.java) { listeners ->
            ArgumentBuilderOnThenCallback { literal, instance, accessor ->
                return@ArgumentBuilderOnThenCallback listeners.firstOrNull()?.onThen(literal, instance, accessor) ?: TypedActionResult.pass("")
            }
        }
    }

    fun onThen(literal: String, instance: LiteralArgumentBuilder<*>, accessor: LiteralAccessor): TypedActionResult<String>

}