package xyz.wagyourtail.wagyourgui.api.element.impl;

import xyz.wagyourtail.wagyourgui.api.element.AbstractElement;
import xyz.wagyourtail.wagyourgui.api.element.Disableable;
import xyz.wagyourtail.wagyourgui.api.element.Themeable;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;
import java.util.function.Consumer;

public class Button extends AbstractElement implements Themeable<Theme.ButtonTheme>, Disableable {
    private Consumer<Button> onClick;

    private Theme.ButtonTheme theme = Theme.currentTheme.button[0];

    private Object text;

    private boolean disabled = false;
    private boolean hidden = false;

    public Button(int x, int y, int width, int height, ColoredString text, Consumer<Button> onClick) {
        super(x, y, width, height);
        this.text = text;
        this.onClick = onClick;
    }

    public Button(int x, int y, int width, int height, String text, Consumer<Button> onClick) {
        super(x, y, width, height);
        this.text = text;
        this.onClick = onClick;
    }

    public Consumer<Button> getOnClick() {
        return onClick;
    }

    public void setOnClick(Consumer<Button> onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getThemeIndex() {
        return Arrays.asList(Theme.currentTheme.button).indexOf(theme);
    }

    @Override
    public void setThemeIndex(int themeIndex) {
        this.theme = Theme.currentTheme.button[themeIndex % Theme.currentTheme.button.length];
    }

    public ColoredString getText() {
        if (text instanceof ColoredString) return (ColoredString) text;
        return new ColoredString().append((String) text, getTheme().textColor);
    }

    public void setText(ColoredString text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Theme.ButtonTheme getTheme() {
        return theme;
    }

    @Override
    public boolean onReleased(int mouseX, int mouseY, int button, int mods) {
        onClick.accept(this);
        return true;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        if (hidden) return;
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        Theme.ButtonTheme theme = getTheme();

        if (getTheme().bgTexture != null) {
            if (!disabled) {
                if (hovered) {
                    RENDERER.texturedRect(x, y, width, height, theme.bgTexture, 40, 0, 200, 20, 256, 256);
                } else {
                    RENDERER.texturedRect(x, y, width, height, theme.bgTexture, 20, 0, 200, 20, 256, 256);
                }
            } else {
                RENDERER.texturedRect(x, y, width, height, theme.bgTexture, 0, 0, 256, 256);
            }
        } else {
            RENDERER.rect(x, y, width, theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + height - theme.borderWidth, width, theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + theme.borderWidth, theme.borderWidth, height - theme.borderWidth * 2, theme.borderColor);
            RENDERER.rect(x + width - theme.borderWidth, y + theme.borderWidth, theme.borderWidth, height - theme.borderWidth * 2, theme.borderColor);

            RENDERER.rect(x + theme.borderWidth, y + theme.borderWidth, width - theme.borderWidth * 2, height - theme.borderWidth * 2, !disabled ? hovered ? theme.hoverBgColor : theme.bgColor : theme.disabledBgColor);
        }

        if (text instanceof ColoredString) {
            RENDERER.centeredColoredStringTrimmed((ColoredString) text, x + width / 2, y + height / 2 - RENDERER.getStringHeight() / 2, width - 4, true);
        } else {
            RENDERER.centeredStringTrimmed(text.toString(), x + width / 2, y + height / 2 - RENDERER.getStringHeight() / 2, width - 4, theme.textColor, true);
        }
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
