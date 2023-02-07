package ch.skyfy.fabricpermshiderkotlined.prelaunchmixin;

import ch.skyfy.fabricpermshiderkotlined.utils.Test;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.command.OpCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = CommandDispatcher.class, remap = false)
public class CommandDisMixin {

    @Inject(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;build()Lcom/mojang/brigadier/tree/LiteralCommandNode;"

            )
    )
    void literal(LiteralArgumentBuilder<?> command, CallbackInfoReturnable<LiteralCommandNode<?>> cir) {
        if(0 == 0)return;
        System.out.println("\n\n\n");

        var counter = new AtomicInteger(0);
        var found = new AtomicBoolean(false);
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach(stackFrame -> {
            if(found.get()) return;

            if (counter.get() == 2) {
                System.out.println("CommandName: " + command.getLiteral() + " modDeclaringClass: " + stackFrame.getDeclaringClass().getCanonicalName());
                Test.MAP2.putIfAbsent(command, new Test.Data(stackFrame.getDeclaringClass()));
                found.set(true);
            }

            if (stackFrame.getDeclaringClass().getSimpleName().equalsIgnoreCase("CommandDispatcher")) {
                counter.getAndIncrement();
            }
        });
    }

}
