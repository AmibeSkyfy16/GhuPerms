package ch.skyfy.fabricpermshiderkotlined.prelaunchmixin;

import ch.skyfy.fabricpermshiderkotlined.utils.Test;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = LiteralArgumentBuilder.class, remap = false)
public class CommandManagerMixin {

    @Shadow
    @Final
    private String literal;
    private static int v = 0;
    private static Map<String, Integer> count = new HashMap<>();

    @ModifyArg(
            method = "build()Lcom/mojang/brigadier/tree/LiteralCommandNode;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/tree/LiteralCommandNode;<init>(Ljava/lang/String;Lcom/mojang/brigadier/Command;Ljava/util/function/Predicate;Lcom/mojang/brigadier/tree/CommandNode;Lcom/mojang/brigadier/RedirectModifier;Z)V"

            )
    )
    String literal(String literal) {
        if (0 == 0) return literal;
        var obj = ((LiteralArgumentBuilder<?>) (Object) this);

        System.out.println("\n\n\n");

        var counter = new AtomicInteger(0);
        var found = new AtomicBoolean(false);
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach(stackFrame -> {
            System.out.println("\tstackFrame.getDeclaringClass().getCanonicalName() " + stackFrame.getDeclaringClass().getCanonicalName());
            System.out.println("\tstackFrame.getDeclaringClass().getSimpleName() " + stackFrame.getDeclaringClass().getSimpleName());
            System.out.println("\tstackFrame.getDeclaringClass().getName() " + stackFrame.getDeclaringClass().getName());
            System.out.println("\n");

            if(counter.get() == 2){
                var packageArgs = stackFrame.getDeclaringClass().getPackageName().split("\\.");
                if(packageArgs[0].equalsIgnoreCase("net") && packageArgs[1].equalsIgnoreCase("minecraft")){
                    return;
                }
                found.set(true);
            }

            if (stackFrame.getDeclaringClass().getSimpleName().equalsIgnoreCase("CommandDispatcher")) {
                counter.getAndIncrement();
            }
        });

        if (0 == 0) return literal;

        if (Test.MAP2.containsKey(obj)) {
            var data = Test.MAP2.get(obj);
//            Test.TEST.putIfAbsent(obj.build(), new Test.Data2(literal,obj, data));
//            return literal;
        }


        v++;


        if (count.containsKey(literal) && count.get(literal) > 1) {
            System.out.println("greater than one");
        }

        if (Test.MAP2.containsKey(obj)) {
            count.compute(literal, (s, integer) -> {
                if (integer == null) return 1;
                else integer++;
                return integer;
            });

            if (literal.equalsIgnoreCase("op")) {
                return "bla";
            }


            System.out.println("\tequals");
            var data = Test.MAP2.get(obj);
            var packageArgs = data.declaringClass.getPackageName().split("\\.");
            var modName = packageArgs[0];

            var renameCmd = "";

            if (packageArgs[0].equalsIgnoreCase("net") && packageArgs[1].equalsIgnoreCase("minecraft")) {
                System.out.println("\tcommandName: " + obj.getLiteral() + " modName: " + packageArgs[1]);
                renameCmd = packageArgs[1] + ":" + literal;
            } else {
                System.out.println("\tcommandName: " + obj.getLiteral() + " modName: " + modName);
                renameCmd = modName + ":" + literal;
            }


            return renameCmd;
        }

        return literal;
    }

}
