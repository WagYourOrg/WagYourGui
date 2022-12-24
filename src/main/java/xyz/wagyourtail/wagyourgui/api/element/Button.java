package xyz.wagyourtail.wagyourgui.api.element;

import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;
import java.util.function.Consumer;

public class Button implements Element, Interactable, Renderable {
    private int x;
    private int y;

    private int width;
    private int height;

    private boolean visible;
    private boolean enabled;

    private Consumer<Button> onClick;

    private Theme.ButtonTheme theme;

    private Object text;

    public Button(int x, int y, int width, int height, ColoredString text, Consumer<Button> onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;
        this.visible = true;
        this.enabled = true;
    }

    public Button(int x, int y, int width, int height, String text, Consumer<Button> onClick) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onClick = onClick;
        this.visible = true;
        this.enabled = true;
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
    public boolean shouldFocus(int x, int y) {
        return isWithinBounds(x, y) && enabled;
    }

    public void setOnClick(Consumer<Button> onClick) {
        this.onClick = onClick;
    }

    public Consumer<Button> getOnClick() {
        return onClick;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setThemeIndex(int themeIndex) {
        this.theme = Theme.currentTheme.button[themeIndex % Theme.currentTheme.button.length];
    }

    public int getThemeIndex() {
        return Arrays.asList(Theme.currentTheme.button).indexOf(theme);
    }

    public void setText(ColoredString text) {
        this.text = text;
    }

    public ColoredString getText() {
        if (text instanceof ColoredString) return (ColoredString) text;
        return new ColoredString().append((String) text, theme.textColor);
    }

    private Theme.ButtonTheme getTheme() {
        return theme;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        if (theme.texture != null) {
            if (enabled) {
                if (hovered) {
                    RENDERER.drawTexturedRect(x, y, width, height, 40, 0, 256, 256, theme.texture);
                } else {
                    RENDERER.drawTexturedRect(x, y, width, height, 20, 0, 256, 256, theme.texture);
                }
            } else {
                RENDERER.drawTexturedRect(x, y, width, height, 0, 0, 256, 256, theme.texture);
            }
        } else {
            RENDERER.drawLine(x, y, x + width, y, theme.borderColor);
            RENDERER.drawLine(x, y + height, x + width, y + height, theme.borderColor);
            RENDERER.drawLine(x, y, x, y + height, theme.borderColor);
            RENDERER.drawLine(x + width, y, x + width, y + height, theme.borderColor);

            if (enabled) {
                if (hovered) {
                    RENDERER.drawRect(x + 1, y + 1, width - 2, height - 2, theme.hoverBgColor);
                } else {
                    RENDERER.drawRect(x + 1, y + 1, width - 2, height - 2, theme.bgColor);
                }
            } else {
                RENDERER.drawRect(x + 1, y + 1, width - 2, height - 2, theme.hoverBgColor);
            }
        }

        if (text instanceof ColoredString) {
            RENDERER.drawCenteredColoredStringTrimmed((ColoredString) text, x + width / 2, y + height / 2, width - 4, true);
        } else {
            RENDERER.drawCenteredStringTrimmed(text.toString(), x + width / 2, y + height / 2, width - 4, theme.textColor, true);
        }
    }
}
