package ch.skyfy.ghuperms;

import ch.skyfy.ghuperms.callback.AddChildCallback;
import ch.skyfy.ghuperms.callback.CanUseCallback;
import ch.skyfy.ghuperms.config.Configs;
import ch.skyfy.ghuperms.ducks.CommandNodeDuck;
import ch.skyfy.ghuperms.permission.Permissions;
import ch.skyfy.ghuperms.prelaunch.config.CommandAlias;
import ch.skyfy.ghuperms.prelaunch.config.PreLaunchConfigs;
import ch.skyfy.ghuperms.utils.ModsUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.TypedActionResult;

import java.util.*;

import static com.mojang.brigadier.CommandDispatcher.ARGUMENT_SEPARATOR_CHAR;

public class CommandNodeMixinImpl {

    public static final Map<Object, Object> MAP = new HashMap<>();

    private <S> ParseResults<S> parseNodes(final CommandNode<S> node, final StringReader originalReader, final CommandContextBuilder<S> contextSoFar, CommandDispatcher<S> commandDispatcher) {
        final S source = contextSoFar.getSource();
        Map<CommandNode<S>, CommandSyntaxException> errors = null;
        List<ParseResults<S>> potentials = null;
        final int cursor = originalReader.getCursor();

        for (final CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            final CommandContextBuilder<S> context = contextSoFar.copy();
            final StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                } catch (final RuntimeException ex) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                }
                if (reader.canRead()) {
                    if (reader.peek() != ARGUMENT_SEPARATOR_CHAR) {
                        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                    }
                }
            } catch (final CommandSyntaxException ex) {
                if (errors == null) {
                    errors = new LinkedHashMap<>();
                }
                errors.put(child, ex);
                reader.setCursor(cursor);
                continue;
            }

            context.withCommand(child.getCommand());
            if (reader.canRead(child.getRedirect() == null ? 2 : 1)) {
                reader.skip();
                if (child.getRedirect() != null) {
                    final CommandContextBuilder<S> childContext = new CommandContextBuilder<>(commandDispatcher, source, child.getRedirect(), reader.getCursor());
                    final ParseResults<S> parse = parseNodes(child.getRedirect(), reader, childContext, commandDispatcher);
                    context.withChild(parse.getContext());
                    return new ParseResults<>(context, parse.getReader(), parse.getExceptions());
                } else {
                    final ParseResults<S> parse = parseNodes(child, reader, context, commandDispatcher);
                    if (potentials == null) {
                        potentials = new ArrayList<>(1);
                    }
                    potentials.add(parse);
                }
            } else {
                if (potentials == null) {
                    potentials = new ArrayList<>(1);
                }
                potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
            }
        }

        if (potentials != null) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.get(0);
        }

        return new ParseResults<>(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }

    public CommandNodeMixinImpl() {

//        OnParseNodesCallback.EVENT.register(new OnParseNodesCallback() {
//            @Override
//            public <S> TypedActionResult<ParseResults<S>> onParseNode(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar, ParseResults<S> parseResults, CommandDispatcher<S> commandDispatcher) {
//                // TODO -----------------------
////                parseResults = parseNodes(node, originalReader, contextSoFar, commandDispatcher);
////                var command = parseResults.getReader().getString();
////                var context = parseResults.getContext().build(command);
////                MAP.put(contextSoFar.getSource(), context);
//                return TypedActionResult.pass(parseResults);
//                // TODO -----------------------
//            }
//        });

        AddChildCallback.EVENT.register(new AddChildCallback() {
            @Override
            public <S> void onAddChild(CommandNodeDuck<S> child, CommandNode<S> instance) {
                child.addParent(instance);
            }
        });

        CanUseCallback.EVENT.register(new CanUseCallback() {
            @Override
            public <S> TypedActionResult<Boolean> onCanUse(Map<String, CommandNode<S>> children, Set<CommandNode<S>> parents, Map<RootCommandNode<S>, List<List<CommandNode<S>>>> ancestries, CommandNode<S> instance, S source) {

                // TODO -----------------------
//                CommandContext<ServerCommandSource> context = null;
//                if(source instanceof ServerCommandSource) {
//                    if (MAP.containsKey(source)) {
//                        System.out.println("contains key");
//                        context = (CommandContext<ServerCommandSource>) MAP.get(source);
//                        MAP.remove(source);
//                    }
//                }
                // TODO -----------------------

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
                    var commandName = ssCommandNode.getName();
                    for (CommandAlias commandAlias : PreLaunchConfigs.COMMANDS_ALIASES.getSerializableData().component2()) {
                        if(commandAlias.getAlias().equalsIgnoreCase(ssCommandNode.getName())){
                            commandName = commandAlias.getBaseCommand();
                        }
                    }

                    if(commandName.contains(":")){
                        var splits = commandName.split(":");
                        if(splits.length >= 2) {
                            sb.replace(0, 0, splits[0] + ":");
                            sb.append(".").append(splits[1]);
                        }
                    }else{
                        if (!sb.isEmpty()) sb.append(".");
                        sb.append(commandName);
                    }
                }

                Configs.PERMISSIONS_DATA.getSerializableData().component1().add(sb.toString());

                // TODO -----------------------
//                StringBuilder secondSb = null;
//                if (context != null) {
//                    secondSb = new StringBuilder(prefix);
//                    for (int i = greater.size() - 1; i >= 0; i--) {
//                        var ssCommandNode = greater.get(i);
//                        if (!secondSb.isEmpty()) secondSb.append(".");
//                        if (ssCommandNode instanceof ArgumentCommandNode<?, ?> argumentCommandNode) {
//                            try {
//                                if (argumentCommandNode.getType() instanceof StringArgumentType)
//                                    secondSb.append(getString(context, argumentCommandNode.getName()));
////                                if (argumentCommandNode.getType() instanceof GameProfileArgumentType)
////                                    secondSb.append(getProfileArgument(context, argumentCommandNode.getName()));
//                            }catch (Exception e){
//                                System.out.println("excpetion");
//                                secondSb = null;
//                                break;
//                            }
//                        } else {
//                            secondSb.append(ssCommandNode.getName());
//                        }
//                    }
//                }
                // TODO -----------------------

                if (source instanceof ServerCommandSource serverCommandSource) {
                    if (serverCommandSource.getPlayer() == null) return TypedActionResult.pass(true);

                    // TODO -----------------------
//                    // Check a second time with "normal" permission like "command.homes.create.titi"
//                    if(secondSb != null){
//                        if (!Permissions.checkNew(ModsUtils.INSTANCE.getPlayerNameWithUUID(serverCommandSource.getPlayer()), secondSb.toString()))
//                            return TypedActionResult.fail(false);
//                        else
//                            return TypedActionResult.pass(true);
//                    }
                    // TODO -----------------------

                    // Check a first time with "normal" permission like "command.homes.create.homeName"
                    if (!Permissions.checkNew(ModsUtils.INSTANCE.getPlayerNameWithUUID(serverCommandSource.getPlayer()), sb.toString()))
                        return TypedActionResult.fail(false);

                }
                return TypedActionResult.pass(true);
            }
        });
    }

}
