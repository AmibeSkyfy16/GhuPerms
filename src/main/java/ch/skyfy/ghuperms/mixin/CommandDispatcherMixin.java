package ch.skyfy.ghuperms.mixin;

import ch.skyfy.ghuperms.callback.OnParseNodesCallback;
import ch.skyfy.ghuperms.config.CommandAlias;
import ch.skyfy.ghuperms.config.Configs;
import ch.skyfy.ghuperms.utils.Test;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;

import static ch.skyfy.ghuperms.utils.Test.COMMANDS;

@Mixin(value = CommandDispatcher.class, remap = false)
public abstract class CommandDispatcherMixin<S> {

    private static final Map<String, Integer> registerOnce = new HashMap<>();


//    @Inject(method = "register", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void register2(LiteralArgumentBuilder<?> command, CallbackInfoReturnable<LiteralCommandNode<?>> cir, LiteralCommandNode build) {
//        if(0 == 0)return; // DISABLED
//        var dispatcher = ((CommandDispatcher<ServerCommandSource>) (Object) this);
//
//        if (registerOnce.containsKey(command.getLiteral())) {
//            System.out.println("return");
//            return;
//        }
//
//        for (CommandAlias commandAlias : Configs.COMMAND_ALIASES.getSerializableData().getAliases()) {
//            if (commandAlias.getBaseCommand().equalsIgnoreCase(COMMANDS.get(command.getLiteral()))) {
////                shouldReturn.set(true);
//
//                Test.CREATED_ALIASES.put(commandAlias.getAlias(), commandAlias.getBaseCommand());
//                registerOnce.putIfAbsent(command.getLiteral(), 1);
//                dispatcher.register(CommandManager.literal(commandAlias.getAlias())
//                        .redirect((CommandNode<ServerCommandSource>) build));
////                shouldReturn.set(false);
//            }
//        }
//
//    }

//    @Inject(method = "parseNodes", at = @At("HEAD"), cancellable = true)
//    void onParse(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar, CallbackInfoReturnable<ParseResults<S>> cir){
//
//        OnParseNodesCallback.EVENT.invoker().onParseNode(node, originalReader, contextSoFar, cir.getReturnValue());
//
//        System.out.println();
//
//    }

}
