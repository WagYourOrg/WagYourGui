package xyz.wagyourtail.wagyourgui.api.element;

import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;
import java.util.function.Consumer;

public class Button extends AbstractElement implements Themeable<Theme.ButtonTheme> {
    private Consumer<Button> onClick;

    private Theme.ButtonTheme theme = Theme.currentTheme.button[0];

    private Object text;

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

    public void setOnClick(Consumer<Button> onClick) {
        this.onClick = onClick;
    }

    public Consumer<Button> getOnClick() {
        return onClick;
    }

    @Override
    public void setThemeIndex(int themeIndex) {
        this.theme = Theme.currentTheme.button[themeIndex % Theme.currentTheme.button.length];
    }

    @Override
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
        if (!visible) return;
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        Theme.ButtonTheme theme = getTheme();

        if (getTheme().bgTexture != null) {
            if (enabled) {
                if (hovered) {
                    RENDERER.texturedRect(x, y, width, height, 40, 0, 256, 256, theme.bgTexture);
                } else {
                    RENDERER.texturedRect(x, y, width, height, 20, 0, 256, 256, theme.bgTexture);
                }
            } else {
                RENDERER.texturedRect(x, y, width, height, 0, 0, 256, 256, theme.bgTexture);
            }
        } else {
            RENDERER.rect(x, y, width, theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + height - theme.borderWidth, width, theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + theme.borderWidth, theme.borderWidth, height - theme.borderWidth * 2, theme.borderColor);
            RENDERER.rect(x + width - theme.borderWidth, y + theme.borderWidth, theme.borderWidth, height - theme.borderWidth * 2, theme.borderColor);

            RENDERER.rect(x + theme.borderWidth, y + theme.borderWidth, width - theme.borderWidth * 2, height - theme.borderWidth * 2, enabled ? hovered ? theme.hoverBgColor : theme.bgColor : theme.disabledBgColor);
        }

        if (text instanceof ColoredString) {
            RENDERER.centeredColoredStringTrimmed((ColoredString) text, x + width / 2, y + height / 2, width - 4, true);
        } else {
            RENDERER.centeredStringTrimmed(text.toString(), x + width / 2, y + height / 2, width - 4, theme.textColor, true);
        }
    }
}
