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
public interface OnCanUseCallback {

    Event<OnCanUseCallback> EVENT = EventFactory.createArrayBacked(
            OnCanUseCallback.class,
            onCanUseCallbackS -> new OnCanUseCallback() {
                @Override
                public <SS> TypedActionResult<Boolean> onCanUse(Map<String, CommandNode<SS>> children, Set<CommandNode<SS>> parents, Map<RootCommandNode<SS>, List<List<CommandNode<SS>>>> ancestries, CommandNode<SS> instance, SS source) {
                    for (var onCanUseCallback2 : onCanUseCallbackS) {
                        TypedActionResult<Boolean> result = onCanUseCallback2.onCanUse(children, parents, ancestries, instance, source);
                        if (!result.getValue()) {
                            return result;
                        }
                    }
                    return TypedActionResult.pass(true);
                }
            }
    );

    <SS> TypedActionResult<Boolean> onCanUse(
            Map<String, CommandNode<SS>> children,
            Set<CommandNode<SS>> parents,
            Map<RootCommandNode<SS>, List<List<CommandNode<SS>>>> ancestries,
            CommandNode<SS> instance,
            SS source);

}