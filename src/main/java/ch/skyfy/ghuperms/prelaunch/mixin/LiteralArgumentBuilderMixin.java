package ch.skyfy.ghuperms.prelaunch.mixin;

import ch.skyfy.ghuperms.prelaunch.callback.LiteralArgumentBuilderInitCallback;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static ch.skyfy.ghuperms.utils.Test.COMMANDS;
import static ch.skyfy.ghuperms.utils.Test.CREATED_ALIASES;

@Mixin(value = com.mojang.brigadier.builder.LiteralArgumentBuilder.class, remap = false)
public class LiteralArgumentBuilderMixin<S> {

    @Mutable
    @Shadow
    @Final
    private String literal;


    @Inject(
            method = "<init>",
            at = @At(value = "TAIL")
    )
    private void init(String literal, CallbackInfo ci) {
//        if(0 == 0)return; // DISABLED
        var result = LiteralArgumentBuilderInitCallback.Companion.getEVENT().invoker().onInit(literal, (LiteralArgumentBuilder<S>) (Object) this);
        if (!result.getValue().isEmpty()) this.literal = result.getValue();
    }

}
