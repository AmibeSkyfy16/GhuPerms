package ch.skyfy.ghuperms.prelaunch.mixin;

import ch.skyfy.ghuperms.prelaunch.callback.CommandDispatcherOnRegisterCallback;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CommandDispatcher.class, remap = false)
public class CommandDispatcherMixin<S> {

    @Inject(method = "register", at = @At("HEAD"))
    void onRegister(LiteralArgumentBuilder<S> command, CallbackInfoReturnable<LiteralCommandNode<S>> cir){
        CommandDispatcherOnRegisterCallback.EVENT.invoker().onThen(command);
    }

}
