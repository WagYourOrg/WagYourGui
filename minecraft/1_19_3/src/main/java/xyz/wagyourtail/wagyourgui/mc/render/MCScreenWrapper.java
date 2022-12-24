package xyz.wagyourtail.wagyourgui.mc.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public class MCScreenWrapper extends net.minecraft.client.gui.screens.Screen {
    private final Screen screen;

    protected MCScreenWrapper(Screen screen) {
        super(Component.empty());
        this.screen = screen;
        if (!screen.isParentGuest()) {
            screen.setOpenParent(() -> minecraft.setScreen((net.minecraft.client.gui.screens.Screen) screen.getParentHost()));
        }
    }

    public Screen getScreen() {
        return screen;
    }

    @Override
    protected void init() {
        screen.onInit(width, height, minecraft.level != null);
    }

    @Override
    public void onClose() {
        screen.close();
    }

    @Override
    public boolean mouseClicked(double $$0, double $$1, int $$2) {
        int mods = 0;
        if (hasShiftDown()) mods |= 1;
        if (hasControlDown()) mods |= 2;
        if (hasAltDown()) mods |= 4;
        return screen.onClicked((int) $$0, (int) $$1, $$2, mods);
    }

    @Override
    public boolean mouseReleased(double $$0, double $$1, int $$2) {
        int mods = 0;
        if (hasShiftDown()) mods |= 1;
        if (hasControlDown()) mods |= 2;
        if (hasAltDown()) mods |= 4;
        return screen.onReleased((int) $$0, (int) $$1, $$2, mods);
    }

    @Override
    public boolean mouseDragged(double $$0, double $$1, int $$2, double $$3, double $$4) {
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

    @Override
    public void render(PoseStack $$0, int $$1, int $$2, float $$3) {
        MCRenderer.INSTANCE.bindMatrixStack($$0);
        screen.onRender($$1, $$2);
    }
}
