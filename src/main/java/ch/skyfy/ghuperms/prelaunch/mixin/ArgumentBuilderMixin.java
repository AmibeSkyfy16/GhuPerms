package ch.skyfy.ghuperms.prelaunch.mixin;

import ch.skyfy.ghuperms.prelaunch.callback.ArgumentBuilderOnThenCallback;
import ch.skyfy.ghuperms.prelaunch.callback.LiteralArgumentBuilderInitCallback;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ArgumentBuilder.class, remap = false)
public class ArgumentBuilderMixin<S, T extends ArgumentBuilder<S, T>> {




    @Inject(
            method = "then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            at = @At(value = "HEAD")
    )
    private void onThen(ArgumentBuilder<S, ?> argument, CallbackInfoReturnable<T> cir) {
//        if(0 == 0)return; // DISABLED

        var obj = (ArgumentBuilder<?, ?>)(Object)this;
        if(obj instanceof LiteralArgumentBuilder<?> l){
            var accesor = (LiteralAccessor) l;
            var result = ArgumentBuilderOnThenCallback.Companion.getEVENT().invoker().onThen(l.getLiteral(), l, accesor);
            if (!result.getValue().isEmpty()) accesor.setLiteral(result.getValue());
        }

    }

    @Inject(
            method = "then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            at = @At(value = "TAIL")
    )
    private void onThen2(ArgumentBuilder<S, ?> argument, CallbackInfoReturnable<T> cir) {
        if(0 == 0)return; // DISABLED

        var obj = (ArgumentBuilder<?, ?>)(Object)this;
        if(obj instanceof LiteralArgumentBuilder<?> l){
            var accesor = (LiteralAccessor) l;
            var result = ArgumentBuilderOnThenCallback.Companion.getEVENT().invoker().onThen(l.getLiteral(), l, accesor);
            if (!result.getValue().isEmpty()) accesor.setLiteral(result.getValue());
        }

    }

}
