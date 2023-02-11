package ch.skyfy.ghuperms.prelaunch;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
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
import java.util.function.Predicate;

import static ch.skyfy.ghuperms.utils.Test.COMMANDS;
import static ch.skyfy.ghuperms.utils.Test.CREATED_ALIASES;

@SuppressWarnings("UnusedMixin")
@Mixin(value = LiteralCommandNode.class, remap = false)
public class LiteralCommandNodeMixin {
    @Mutable
    @Shadow
    @Final
    private String literal;
    @Mutable
    @Shadow
    @Final
    private String literalLowerCase;

    @Inject(
            method = "<init>",
            at = @At(value = "TAIL")
    )
    private <S> void init(String literal, Command<S> command, Predicate<S> requirement, CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks, CallbackInfo ci) {
        if(0 == 0)return; // DISABLED
        this.literal = literal;
        this.literalLowerCase = literal.toLowerCase(Locale.ROOT);

        if (CREATED_ALIASES.containsKey(literal)) return;

        var foundCommandDispatcher = new AtomicBoolean(false);
        var foundCommandRegistrationCallback = new AtomicBoolean(false);
        var doneForCommandDispatcher = new AtomicBoolean(false);
        var doneForCommandRegistrationCallback = new AtomicBoolean(false);
        var list = new LinkedList<Class<?>>();
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach(stackFrame -> {
            list.add(stackFrame.getDeclaringClass());
            if (doneForCommandDispatcher.get()) return;
            if (doneForCommandRegistrationCallback.get()) return;

            if (foundCommandDispatcher.get()) {
                var packageArgs = stackFrame.getDeclaringClass().getPackageName().split("\\.");
                if (packageArgs[0].equalsIgnoreCase("net") && packageArgs[1].equalsIgnoreCase("minecraft")) {
                    var newLiteral = "mc:" + literal;
                    this.literal = newLiteral;
                    COMMANDS.put(literal, newLiteral);
                } else {
                    var newLiteral = packageArgs[0] + ":" + literal;
                    this.literal = newLiteral;
                    COMMANDS.put(literal, newLiteral);

                    FabricLoaderImpl.INSTANCE.getModsInternal().forEach(modContainer -> {
                        for (String entrypointKey : modContainer.getMetadata().getEntrypointKeys()) {
                            var entryPoint = modContainer.getMetadata().getEntrypoints(entrypointKey);
                            entryPoint.forEach(entrypointMetadata -> {
                                var splits = entrypointMetadata.getValue().split("\\.");
                                if (packageArgs[0].equalsIgnoreCase(splits[0]) && packageArgs[1].equalsIgnoreCase(splits[1])) {
                                    var newLiteral2 = modContainer.getMetadata().getId() + ":" + literal;
                                    this.literal = newLiteral2;
                                    COMMANDS.put(literal, newLiteral2);
                                }
                            });
                        }
                    });
                }

                this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
                doneForCommandDispatcher.set(true);
                return;
            } else {
                if (foundCommandRegistrationCallback.get()) {
                    var packageArgs = list.get(list.size() - 3).getPackageName().split("\\.");
                    var newLiteral = packageArgs[2] + ":" + literal;
                    this.literal = newLiteral;
                    this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
                    COMMANDS.put(literal, newLiteral);

                    FabricLoaderImpl.INSTANCE.getModsInternal().forEach(modContainer -> {
                        for (String entrypointKey : modContainer.getMetadata().getEntrypointKeys()) {
                            var entryPoint = modContainer.getMetadata().getEntrypoints(entrypointKey);
                            entryPoint.forEach(entrypointMetadata -> {
                                var splits = entrypointMetadata.getValue().split("\\.");
                                if (packageArgs[0].equalsIgnoreCase(splits[0]) && packageArgs[1].equalsIgnoreCase(splits[1])) {
                                    var newLiteral2 = modContainer.getMetadata().getId() + ":" + literal;
                                    this.literal = newLiteral2;
                                    COMMANDS.put(literal, newLiteral2);
                                }
                            });
                        }
                    });

                    this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
                    doneForCommandRegistrationCallback.set(true);
                    return;
                }
            }

            if (stackFrame.getDeclaringClass().getSimpleName().equalsIgnoreCase("CommandDispatcher")) foundCommandDispatcher.set(true);
            if (stackFrame.getDeclaringClass().getSimpleName().equalsIgnoreCase("CommandRegistrationCallback")) foundCommandRegistrationCallback.set(true);

        });
        this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
    }

}
