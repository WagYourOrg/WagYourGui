package xyz.wagyourtail.wagyourgui.render;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TextComponentString;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public class MCScreenWrapper extends GuiScreen {
    private final Screen screen;

    protected MCScreenWrapper(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void initGui() {
        screen.onInit();
    }

    @Override
    public void onGuiClosed() {
        screen.close();
    }

    @Override
    public void mouseClicked(int $$0, int $$1, int $$2) {
        screen.onClicked($$0, $$1, $$2);
    }

    @Override
    public void mouseReleased(int $$0, int $$1, int $$2) {
        screen.onReleased($$0, $$1, $$2);
    }

    @Override
    public void mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
        return screen.onDragged((int) $$0, (int) $$1, $$2, $$3, $$4);
    }

    @Override
    public boolean mouseScrolled(double $$0, double $$1, double $$2) {
        return screen.onScrolled((int) $$0, (int) $$1, $$2);
    }

    @Override
    public boolean charTyped(char $$0, int $$1) {
        return screen.onCharTyped($$0, $$1);
    }

    @Override
    public boolean keyPressed(int $$0, int $$1, int $$2) {
        return screen.onKeyPressed($$0, $$1, $$2);
    }

    @Override
    public boolean keyReleased(int $$0, int $$1, int $$2) {
        return screen.onKeyReleased($$0, $$1, $$2);
    }
}
