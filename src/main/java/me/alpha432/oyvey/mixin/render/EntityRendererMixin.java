package me.alpha432.oyvey.mixin.render;

import me.alpha432.oyvey.features.modules.render.Shader;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderHead(Entity entity, float entityYaw, float partialTicks, CallbackInfo ci) {
        Shader shader = Shader.getInstance();
        if (shader.isEnabled()) {
            boolean shouldShader = false;
            if (shader.player.getValue() && entity.getType().toString().toLowerCase().contains("player")) shouldShader = true;
            if (shader.crystal.getValue() && entity.getType().toString().toLowerCase().contains("crystal")) shouldShader = true;
            if (shader.item.getValue() && entity.getType().toString().toLowerCase().contains("item")) shouldShader = true;
            // Hand için ayrı mixin gerekir, burada sadece entity'ler var
            if (shouldShader) {
                GL20.glUseProgram(shader.getShaderProgramId());
                shader.setShaderUniforms();
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderReturn(Entity entity, float entityYaw, float partialTicks, CallbackInfo ci) {
        Shader shader = Shader.getInstance();
        if (shader.isEnabled()) {
            boolean shouldShader = false;
            if (shader.player.getValue() && entity.getType().toString().toLowerCase().contains("player")) shouldShader = true;
            if (shader.crystal.getValue() && entity.getType().toString().toLowerCase().contains("crystal")) shouldShader = true;
            if (shader.item.getValue() && entity.getType().toString().toLowerCase().contains("item")) shouldShader = true;
            if (shouldShader) {
                GL20.glUseProgram(0);
            }
        }
    }
} 