package ch.skyfy.ghuperms;

import ch.skyfy.ghuperms.callback.OnAddChildCallback;
import ch.skyfy.ghuperms.callback.OnCanUseCallback;
import ch.skyfy.ghuperms.callback.OnParseNodesCallback;
import ch.skyfy.ghuperms.commands.EnableDisableAllCommandCmd;
import ch.skyfy.ghuperms.ducks.CommandNodeDuck;
import ch.skyfy.ghuperms.permission.Permissions;
import ch.skyfy.ghuperms.utils.ModsUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.TypedActionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandNodeMixinImpl {

    public CommandNodeMixinImpl() {

//        OnParseNodesCallback.EVENT.register(new OnParseNodesCallback() {
//            @Override
//            public <S> TypedActionResult<ParseResults<S>> onParseNode(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar, ParseResults<S> parseResults) {
//
//                System.out.println("");
//                return TypedActionResult.pass(parseResults);
//            }
//        });

        OnAddChildCallback.EVENT.register(new OnAddChildCallback() {
            @Override
            public <S> void onAddChild(CommandNodeDuck<S> child, CommandNode<S> instance) {
                child.addParent(instance);
            }
        });

        OnCanUseCallback.EVENT.register(new OnCanUseCallback() {
            @Override
            public <SS> TypedActionResult<Boolean> onCanUse(Map<String, CommandNode<SS>> children, Set<CommandNode<SS>> parents, Map<RootCommandNode<SS>, List<List<CommandNode<SS>>>> ancestries, CommandNode<SS> instance, SS source) {

//                if(EnableDisableAllCommandCmd.Companion.getENABLED()){
//                    return TypedActionResult.pass(true);
//                }else if(0 == 0) return TypedActionResult.fail(false);

                List<CommandNode<SS>> greater = new ArrayList<>();
                var prefix = "command";
                var count = 0;
                for (RootCommandNode<SS> ssRootCommandNode : ancestries.keySet()) {
                    for (List<CommandNode<SS>> commandNodes : ancestries.get(ssRootCommandNode)) {
                        if (commandNodes.size() > count) {
                            count = commandNodes.size();
                            greater = commandNodes;
                        }
                    }
                }

                var sb = new StringBuilder(prefix);
                for (int i = greater.size() - 1; i >= 0; i--) {
                    var ssCommandNode = greater.get(i);
                    if (!sb.isEmpty()) sb.append(".");
                    sb.append(ssCommandNode.getName());
                }

                if (source instanceof ServerCommandSource serverCommandSource) {

                    if(serverCommandSource.getPlayer() == null){
                        System.out.println("returned, player is null");
                        return TypedActionResult.pass(true);
                    }

                    if (!Permissions.checkNew(ModsUtils.INSTANCE.getPlayerNameWithUUID(serverCommandSource.getPlayer()), sb.toString()))
                        return TypedActionResult.fail(false);
//                    if (!Permissions.checkNew("Skyfy16#c3419ddb-f019-3764-a6b2-47a0f91e25ee", sb.toString()))

//                    if (!Permissions.check(ModsUtils.INSTANCE.getPlayerNameWithUUID(serverCommandSource.getPlayer()), sb.toString())) return TypedActionResult.fail(false);
                }
                return TypedActionResult.pass(true);
            }
        });
    }

    private static int INT = 0;

}
