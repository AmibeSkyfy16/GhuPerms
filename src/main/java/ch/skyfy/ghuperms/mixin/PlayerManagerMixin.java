package ch.skyfy.ghuperms.mixin;

import ch.skyfy.ghuperms.callback.AddToOperatorsCallback;
import ch.skyfy.ghuperms.callback.RemoveFromOperatorsCallback;
import ch.skyfy.ghuperms.config.Configs;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "addToOperators", at = @At("HEAD"))
    void onAddToOperators(GameProfile profile, CallbackInfo ci){
        AddToOperatorsCallback.Companion.getEVENT().invoker().onAddToOperators(profile);
    }

    @Inject(method = "removeFromOperators", at = @At("HEAD"))
    void onRemoveFromOperators(GameProfile profile, CallbackInfo ci){
        RemoveFromOperatorsCallback.Companion.getEVENT().invoker().onRemoveFromOperators(profile);
    }

}
