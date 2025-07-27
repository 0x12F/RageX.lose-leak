package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;

public class ShaderChams extends Module {
    public enum EffectType {
        SOLID, GRADIENT, RAINBOW, BLOOM
    }

    public final Setting<Boolean> player = this.register(new Setting<>("Player", true));
    public final Setting<Boolean> crystal = this.register(new Setting<>("Crystal", true));
    public final Setting<Boolean> hand = this.register(new Setting<>("Hand", true));
    public final Setting<Boolean> item = this.register(new Setting<>("Item", true));
    public final Setting<EffectType> effectType = this.register(new Setting<>("EffectType", EffectType.SOLID));
    public final Setting<Boolean> fill = this.register(new Setting<>("Fill", true));
    public final Setting<Boolean> outline = this.register(new Setting<>("Outline", true));
    public final Setting<Float> glow = this.register(new Setting<>("Glow", 1.0f, 0.0f, 2.0f));

    public ShaderChams() {
        super("ShaderChams", "Custom shader effects for entities", Category.RENDER, true, false, false);
    }

    // Render hooklar ve shader uygulama kodu buraya eklenecek
} 