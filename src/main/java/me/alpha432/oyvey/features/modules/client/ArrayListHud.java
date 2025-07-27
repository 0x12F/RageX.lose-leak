package me.alpha432.oyvey.features.modules.client;

import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.util.ColorUtil;
import net.minecraft.util.Formatting;

public class ArrayListHud extends Module {
    public ArrayListHud() {
        super("ArrayList", "Shows active modules in the top right", Category.CLIENT, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        int y = 2;
        HudModule hud = (HudModule) me.alpha432.oyvey.OyVey.moduleManager.getModuleByClass(HudModule.class);
        int color = net.minecraft.util.Formatting.WHITE.getColorValue();
        if (hud != null) {
            if (hud.rainbow.getValue()) {
                color = me.alpha432.oyvey.util.ColorUtil.rainbow(hud.rainbowHue.getValue()).getRGB();
            } else {
                color = me.alpha432.oyvey.util.ColorUtil.toRGBA(hud.red.getValue(), hud.green.getValue(), hud.blue.getValue(), hud.alpha.getValue());
            }
        }
        java.util.List<Module> drawnModules = new java.util.ArrayList<>();
        for (Module module : me.alpha432.oyvey.OyVey.moduleManager.getEnabledModules()) {
            if (module.isDrawn() && module != this) {
                drawnModules.add(module);
            }
        }
        // Uzun isim üstte olacak şekilde sırala
        drawnModules.sort((a, b) -> Integer.compare(b.getDisplayName().length(), a.getDisplayName().length()));
        for (Module module : drawnModules) {
            String text = module.getFullArrayString();
            int width = event.getContext().getScaledWindowWidth();
            int textWidth = mc.textRenderer.getWidth(text);
            event.getContext().drawTextWithShadow(mc.textRenderer, text, width - textWidth - 2, y, color);
            y += 12;
        }
    }
} 