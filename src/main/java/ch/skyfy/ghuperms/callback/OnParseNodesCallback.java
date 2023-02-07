package ch.skyfy.ghuperms.callback;

import ch.skyfy.ghuperms.ducks.CommandNodeDuck;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.TypedActionResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface OnParseNodesCallback {

    Event<OnParseNodesCallback> EVENT = EventFactory.createArrayBacked(
            OnParseNodesCallback.class,
            onCanUseCallbackS -> new OnParseNodesCallback() {
                @Override
                public <S> TypedActionResult<ParseResults<S>>  onParseNode(CommandNode<S> node,
                                                       StringReader originalReader,
                                                       CommandContextBuilder<S> contextSoFar,
                                                       ParseResults<S> parseResults
                ) {
                    for (var onCanUseCallback2 : onCanUseCallbackS) {
                        onCanUseCallback2.onParseNode(node, originalReader, contextSoFar, parseResults);
                    }
                   return TypedActionResult.pass(parseResults);
                }
            }
    );

    <S> TypedActionResult<ParseResults<S>> onParseNode(
            CommandNode<S> node,
            StringReader originalReader,
            CommandContextBuilder<S> contextSoFar,
            ParseResults<S> parseResults
    );

}