package xyz.wagyourtail.wagyourgui.api.overlay;

import xyz.wagyourtail.wagyourgui.api.element.Renderable;
import xyz.wagyourtail.wagyourgui.api.element.Themeable;
import xyz.wagyourtail.wagyourgui.api.keys.Key;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;

public abstract class Overlay implements OverlayElement, Renderable, Themeable<Theme.OverlayTheme> {
    public boolean shouldClose = false;
    protected int x, y, width, height;
    private Theme.OverlayTheme theme = Theme.currentTheme.overlay[0];
    private OverlayElement overlay;

    public Overlay(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {

        Theme.OverlayTheme theme = getTheme();

        if (theme.bgTexture != null) {
            RENDERER.texturedRect(x, y, width, height, 0, 0, 16, 16, theme.bgTexture);
        } else {
            RENDERER.rect(x, y, width, theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + height - theme.borderWidth, width, theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + theme.borderWidth, theme.borderWidth, height - theme.borderWidth * 2, theme.borderColor);
            RENDERER.rect(x + width - theme.borderWidth, y + theme.borderWidth, theme.borderWidth, height - theme.borderWidth * 2, theme.borderColor);

            RENDERER.rect(x + theme.borderWidth, y + theme.borderWidth, width - theme.borderWidth * 2, height - theme.borderWidth * 2, theme.bgColor);
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isWithinBounds(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    @Override
    public int getThemeIndex() {
        return Arrays.asList(Theme.currentTheme.overlay).indexOf(theme);
    }

    @Override
    public void setThemeIndex(int index) {
        this.theme = Theme.currentTheme.overlay[index];
    }

    @Override
    public Theme.OverlayTheme getTheme() {
        return theme;
    }

    @Override
    public OverlayElement getOverlay() {
        return overlay;
    }

    @Override
    public void setOverlay(OverlayElement overlay) {
        this.overlay = overlay;
    }

    @Override
    public void onClose() {
        shouldClose = true;
    }

    @Override
    public boolean shouldClose() {
        return shouldClose;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (overlay != null) return overlay.onKeyPressed(keyCode, scanCode, modifiers);
        if (Key.getKey(keyCode).equals(Key.ESCAPE)) {
            onClose();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        if (overlay != null) return overlay.onKeyReleased(keyCode, scanCode, modifiers);
        return false;
    }

    @Override
    public boolean onCharTyped(char character, int mods) {
        if (overlay != null) return overlay.onCharTyped(character, mods);
        return false;
    }

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        if (overlay != null) return overlay.onClicked(mouseX, mouseY, button, mods);
        return false;
    }

    @Override
    public boolean onReleased(int mouseX, int mouseY, int button, int mods) {
        if (overlay != null) return overlay.onReleased(mouseX, mouseY, button, mods);
        return false;
    }

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (overlay != null) return overlay.onDragged(mouseX, mouseY, button, deltaX, deltaY);
        return false;
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        if (overlay != null) return overlay.onScrolled(mouseX, mouseY, scroll);
        return false;
    }
}
