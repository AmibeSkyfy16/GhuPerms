package ch.skyfy.ghuperms.callback;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.TypedActionResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface CanUseCallback {

    Event<CanUseCallback> EVENT = EventFactory.createArrayBacked(
            CanUseCallback.class,
            canUseCallbackS -> new CanUseCallback() {
                @Override
                public <SS> TypedActionResult<Boolean> onCanUse(Map<String, CommandNode<SS>> children, Set<CommandNode<SS>> parents, Map<RootCommandNode<SS>, List<List<CommandNode<SS>>>> ancestries, CommandNode<SS> instance, SS source) {
                    for (var onCanUseCallback2 : canUseCallbackS) {
                        TypedActionResult<Boolean> result = onCanUseCallback2.onCanUse(children, parents, ancestries, instance, source);
                        if (!result.getValue()) {
                            return result;
                        }
                    }
                    return TypedActionResult.pass(true);
                }
            }
    );

    <S> TypedActionResult<Boolean> onCanUse(
            Map<String, CommandNode<S>> children,
            Set<CommandNode<S>> parents,
            Map<RootCommandNode<S>, List<List<CommandNode<S>>>> ancestries,
            CommandNode<S> instance,
            S source);

}