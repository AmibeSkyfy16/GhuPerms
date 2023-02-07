package ch.skyfy.fabricpermshiderkotlined.prelaunchmixin;

import ch.skyfy.fabricpermshiderkotlined.utils.Test;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CommandNode.class, remap = false)
public class CommandNodeMixin {

    @Inject(
            method = "addChild",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/tree/CommandNode;getChildren()Ljava/util/Collection;"
            )
    )
    void addChildd(CommandNode<?> node, CallbackInfo ci){
        var obj = ((CommandNode<?>)(Object)this);
        if(0 == 0)return;



        if(Test.TEST.containsKey(obj)){
            System.out.println("find");
        }

//        if(obj instanceof RootCommandNode<?>){
//            System.out.println("RootCommandNode !!! " + obj.getName());
//
//
//
//        }

    }

}
