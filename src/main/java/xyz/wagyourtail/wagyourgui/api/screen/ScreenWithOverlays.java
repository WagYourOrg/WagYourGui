package xyz.wagyourtail.wagyourgui.api.screen;

import xyz.wagyourtail.wagyourgui.api.element.Renderable;
import xyz.wagyourtail.wagyourgui.api.element.Ticking;
import xyz.wagyourtail.wagyourgui.api.keys.Key;
import xyz.wagyourtail.wagyourgui.api.overlay.OverlayElement;

public abstract class ScreenWithOverlays extends Screen {
    protected OverlayElement overlay;

    public ScreenWithOverlays(Screen parent) {
        super(parent);
    }

    public void openOverlay(OverlayElement overlay) {
        if (this.overlay != null) {
            this.overlay.onClose();
        }
        overlay.onOpened();
        this.overlay = overlay;
    }

    public void closeOverlay() {
        if (overlay != null) {
            overlay.onClose();
        }
        overlay = null;
    }

    public boolean hasOverlay() {
        return this.overlay != null;
    }

    @Override
    public boolean closeOnESC() {
        return !hasOverlay();
    }

    @Override
    public void onInit(int width, int height, boolean transparentBg) {
        super.onInit(width, height, transparentBg);
        overlay = null;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        if (overlay != null) {
            super.onRender(-1, -1);
            ((Renderable) overlay).onRender(mouseX, mouseY);
            if (overlay.shouldClose()) {
                closeOverlay();
            }
        } else {
            super.onRender(mouseX, mouseY);
        }
    }

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        if (overlay != null) {
            return overlay.onClicked(mouseX, mouseY, button, mods);
        }
        return super.onClicked(mouseX, mouseY, button, mods);
    }

    @Override
    public boolean onReleased(int mouseX, int mouseY, int button, int mods) {
        if (overlay != null) {
            return overlay.onReleased(mouseX, mouseY, button, mods);
        }
        return super.onReleased(mouseX, mouseY, button, mods);
    }

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (overlay != null) {
            return overlay.onDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.onDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        if (overlay != null) {
            return overlay.onScrolled(mouseX, mouseY, scroll);
        }
        return super.onScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean onCharTyped(char character, int mods) {
        if (overlay != null) {
            return overlay.onCharTyped(character, mods);
        }
        return super.onCharTyped(character, mods);
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (overlay != null) {
            if (Key.getKey(keyCode) == Key.Keyboard.ESCAPE) {
                closeOverlay();
                return true;
            }
            return overlay.onKeyPressed(keyCode, scanCode, modifiers);
        }
        return super.onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        if (overlay != null) {
            return overlay.onKeyReleased(keyCode, scanCode, modifiers);
        }
        return super.onKeyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void onTick() {
        super.onTick();
        if (overlay != null && overlay instanceof Ticking) {
            ((Ticking) overlay).onTick();
        }
    }
}
