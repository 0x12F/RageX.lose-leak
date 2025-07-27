package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.ArrayListHud;
import me.alpha432.oyvey.features.gui.Component;
import me.alpha432.oyvey.features.gui.items.buttons.ModuleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;

public class HudElementsGui extends Screen {
    private final ArrayList<Component> components = new ArrayList<>();

    public HudElementsGui() {
        super(Text.literal("Elements"));
        int x = 20;
        // Sadece ArrayListHud için bir component oluştur
        Component arrayListComponent = new Component("Elements", x, 30, true) {
            @Override
            public void setupItems() {
                counter1 = new int[]{1};
                Module arrayList = OyVey.moduleManager.getModuleByClass(me.alpha432.oyvey.features.modules.client.ArrayListHud.class);
                if (arrayList != null) {
                    this.addButton(new me.alpha432.oyvey.features.gui.items.buttons.ModuleButton(arrayList));
                }
            }
        };
        components.clear();
        components.add(arrayListComponent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        for (Component component : components) {
            component.drawScreen(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            component.mouseClicked((int) mouseX, (int) mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            component.mouseReleased((int) mouseX, (int) mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Component component : components) {
            component.onKeyPressed(keyCode);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Component component : components) {
            component.onKeyTyped(chr, modifiers);
        }
        return super.charTyped(chr, modifiers);
    }
} 