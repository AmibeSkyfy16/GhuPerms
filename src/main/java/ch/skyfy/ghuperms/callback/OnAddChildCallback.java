package ch.skyfy.ghuperms.callback;

import ch.skyfy.ghuperms.ducks.CommandNodeDuck;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
public interface OnAddChildCallback {

    Event<OnAddChildCallback> EVENT = EventFactory.createArrayBacked(
            OnAddChildCallback.class,
            onAddChildCallbacks -> new OnAddChildCallback() {
                @Override
                public <S> void onAddChild(CommandNodeDuck<S> child, CommandNode<S> instance) {
                    for (var onCanUseCallback2 : onAddChildCallbacks) {
                        onCanUseCallback2.onAddChild(child, instance);
                    }

                }
            }
    );

    <S> void onAddChild(
            CommandNodeDuck<S> child,
            CommandNode<S> instance
    );

}
