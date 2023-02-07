package ch.skyfy.ghuperms.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static ch.skyfy.ghuperms.utils.Test.COMMANDS;
import static ch.skyfy.ghuperms.utils.Test.CREATED_ALIASES;

@Mixin(value = LiteralArgumentBuilder.class, remap = false)
public class LiteralArgumentBuilderMixin {



    @Inject(method = "getLiteral", at = @At("RETURN"), cancellable = true)
    private void getLit(CallbackInfoReturnable<String> cir){
//        var o = (LiteralArgumentBuilder<?>)(Object)this;
//        System.out.println("getLit from LiteralCommandNode " + cir.getReturnValue());
//        System.out.println("getLit from LiteralCommandNode " + literal);


//        if(cir.getReturnValue().equalsIgnoreCase("gamerule")){
//            return;
//        }

//        if(CREATED_ALIASES.containsKey(cir.getReturnValue())){
//            System.out.println("CREATED_ALIASES.containsKey(cir.getReturnValue() CONTAIN " + cir.getReturnValue());
//            return;
//        }
////
//        if(COMMANDS.containsKey(cir.getReturnValue())){
//            System.out.println("COMMANDS CONTAINS");
//            cir.setReturnValue(COMMANDS.get(cir.getReturnValue()));
//        }
    }

}
