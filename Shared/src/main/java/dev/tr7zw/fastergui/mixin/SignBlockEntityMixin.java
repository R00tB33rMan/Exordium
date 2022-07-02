package dev.tr7zw.fastergui.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.fastergui.access.SignBufferHolder;
import dev.tr7zw.fastergui.util.SignBufferRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin implements SignBufferHolder {

    private SignBufferRenderer cachedBufferRenderer = null;
    private Component[] lines = new Component[4];
    
    // TODO: hook the update methods instead of hot checking stuff. 
    @Override
    public boolean renderBuffered(PoseStack poseStack, MultiBufferSource multiBufferSource) {
        SignBlockEntity sign = (SignBlockEntity)(Object)this;
        if(isSignEmpty(sign)) {
            return true; // empty sign, nothing to do (yet)
        }
        if(cachedBufferRenderer == null || lines[0] != sign.getMessage(0, false) || lines[1] != sign.getMessage(1, false) || lines[2] != sign.getMessage(2, false) || lines[3] != sign.getMessage(3, false)) {
            cachedBufferRenderer = new SignBufferRenderer(sign, multiBufferSource);
            lines[0] = sign.getMessage(0, false);
            lines[1] = sign.getMessage(1, false);
            lines[2] = sign.getMessage(2, false);
            lines[3] = sign.getMessage(3, false);
        }
        cachedBufferRenderer.render(poseStack);
        return true;
    }
    
    private boolean isSignEmpty(SignBlockEntity sign) {
        for(int i = 0; i < 4; i++) {
            if(sign.getMessage(i, false) != CommonComponents.EMPTY)return false;
        }
        return true;
    }

}
