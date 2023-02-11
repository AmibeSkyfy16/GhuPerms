package ch.skyfy.ghuperms.prelaunch.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = LiteralArgumentBuilder.class, remap = false)
public interface LiteralAccessor {
    @Mutable
    @Accessor("literal")
    void setLiteral(String literal);

}
