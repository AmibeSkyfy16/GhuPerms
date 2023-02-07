package ch.skyfy.ghuperms;

import ch.skyfy.ghuperms.callback.AddChildCallback;
import ch.skyfy.ghuperms.callback.CanUseCallback;
import ch.skyfy.ghuperms.ducks.CommandNodeDuck;
import ch.skyfy.ghuperms.permission.Permissions;
import ch.skyfy.ghuperms.utils.ModsUtils;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.TypedActionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandNodeMixinImpl {

    public CommandNodeMixinImpl() {
        AddChildCallback.EVENT.register(new AddChildCallback() {
            @Override
            public <S> void onAddChild(CommandNodeDuck<S> child, CommandNode<S> instance) {
                child.addParent(instance);
            }
        });

        CanUseCallback.EVENT.register(new CanUseCallback() {
            @Override
            public <S> TypedActionResult<Boolean> onCanUse(Map<String, CommandNode<S>> children, Set<CommandNode<S>> parents, Map<RootCommandNode<S>, List<List<CommandNode<S>>>> ancestries, CommandNode<S> instance, S source) {

                List<CommandNode<S>> greater = new ArrayList<>();
                var prefix = "command";
                var count = 0;
                for (RootCommandNode<S> ssRootCommandNode : ancestries.keySet()) {
                    for (List<CommandNode<S>> commandNodes : ancestries.get(ssRootCommandNode)) {
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
                    if(serverCommandSource.getPlayer() == null) return TypedActionResult.pass(true);

                    if (!Permissions.checkNew(ModsUtils.INSTANCE.getPlayerNameWithUUID(serverCommandSource.getPlayer()), sb.toString()))
                        return TypedActionResult.fail(false);
                }
                return TypedActionResult.pass(true);
            }
        });
    }

}
