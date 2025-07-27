package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.features.settings.SettingFactory;

public class HudModule extends Module {
    private final Setting<Boolean> hudManagerButton;
    public Setting<Integer> red = num("Red", 0, 0, 255);
    public Setting<Integer> green = num("Green", 0, 0, 255);
    public Setting<Integer> blue = num("Blue", 255, 0, 255);
    public Setting<Integer> alpha = num("Alpha", 180, 0, 255);
    public Setting<Boolean> rainbow = bool("Rainbow", false);
    public Setting<Integer> rainbowHue = num("Delay", 240, 0, 600);
    public Setting<Float> rainbowBrightness = num("Brightness ", 150.0f, 1.0f, 255.0f);
    public Setting<Float> rainbowSaturation = num("Saturation", 150.0f, 1.0f, 255.0f);

    public HudModule() {
        super("Hud", "hud", Category.CLIENT, true, false, false);
        // 'hudmanager' adında bir buton ayarı ekle
        hudManagerButton = bool("hudmanager", false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int color = -1;
        if (rainbow.getValue()) {
            color = me.alpha432.oyvey.util.ColorUtil.rainbow(rainbowHue.getValue()).getRGB();
        } else {
            color = me.alpha432.oyvey.util.ColorUtil.toRGBA(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue());
        }
        event.getContext().drawTextWithShadow(
                mc.textRenderer,
                TextUtil.text("{global} %s {} %s", OyVey.NAME, OyVey.VERSION),
                2, 2,
                color
        );
        if (hudManagerButton.getValue()) {
            // Hud Elements GUI'yi aç
            mc.setScreen(new me.alpha432.oyvey.features.gui.HudElementsGui());
            hudManagerButton.setValue(false);
        }
    }
}
