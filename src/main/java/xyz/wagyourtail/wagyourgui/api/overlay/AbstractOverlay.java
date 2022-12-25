package xyz.wagyourtail.wagyourgui.api.overlay;

import xyz.wagyourtail.wagyourgui.api.element.Renderable;
import xyz.wagyourtail.wagyourgui.api.element.Themeable;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;

public abstract class AbstractOverlay implements OverlayElement, Renderable, Themeable<Theme.OverlayTheme> {
    protected int x, y, width, height;
    private Theme.OverlayTheme theme = Theme.currentTheme.overlay[0];

    private OverlayElement overlay;
    public boolean shouldClose = false;

    public AbstractOverlay(int x, int y, int width, int height) {
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
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
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
    public void setEnabled(boolean enabled) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setThemeIndex(int index) {
        this.theme = Theme.currentTheme.overlay[index];
    }

    @Override
    public int getThemeIndex() {
        return Arrays.asList(Theme.currentTheme.overlay).indexOf(theme);
    }

    @Override
    public Theme.OverlayTheme getTheme() {
        return theme;
    }

    @Override
    public void setOverlay(OverlayElement overlay) {
        this.overlay = overlay;
    }

    @Override
    public OverlayElement getOverlay() {
        return overlay;
    }

    @Override
    public void onClose() {
        shouldClose = true;
    }

    @Override
    public boolean shouldClose() {
        return shouldClose;
    }
}
