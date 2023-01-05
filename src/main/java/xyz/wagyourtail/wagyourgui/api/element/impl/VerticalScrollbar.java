package xyz.wagyourtail.wagyourgui.api.element.impl;

import xyz.wagyourtail.wagyourgui.api.element.AbstractScrollbar;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.function.Consumer;

public class VerticalScrollbar extends AbstractScrollbar<VerticalScrollbar> {

    public VerticalScrollbar(int x, int y, int width, int height, double pages, Consumer<VerticalScrollbar> onScroll) {
        super(x, y, width, height, pages, onScroll);
    }

    public double getScrollbarSize() {
        return super.getScrollbarSize(height);
    }

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        double sbHeight = getScrollbarSize() / 2;
        if (mouseY - sbHeight < y) {
            setScroll(0);
        } else if (mouseY + sbHeight > y + height) {
            setScroll(getScrollPages());
        } else {
            setScroll((mouseY - y - sbHeight) / (height - getScrollbarSize()) * getScrollPages());
        }
        if (getOnScroll() != null) getOnScroll().accept(this);
        return true;
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        setScroll(getScroll() - scroll);
        if (getOnScroll() != null) getOnScroll().accept(this);
        return true;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        double sbHeight = getScrollbarSize();

        Theme.ScrollbarTheme theme = getTheme();

        if (theme.texture != null) {
            // draw bg
            RENDERER.texturedRect(x, y, width, height, theme.texture, 0, 0, 32, 128, 64, 128);
            // draw scrollbar
            RENDERER.texturedRect(x, (int) (y + (getScroll() * height / getScrollPages())), x + width, (int) (y + (getScroll() + 1) * height / getScrollPages()),
                theme.texture,
                32, 0, 32, 128, 64, 128
            );
        } else {
            // draw border
            RENDERER.rect(x, y, x + width, y + theme.borderWidth, theme.borderColor);
            RENDERER.rect(x, y + height - theme.borderWidth, x + width, y + height, theme.borderColor);
            RENDERER.rect(x, y, x + theme.borderWidth, y + height, theme.borderColor);
            RENDERER.rect(x + width - theme.borderWidth, y, x + width, y + height, theme.borderColor);
            // draw bg
            RENDERER.rect(x + theme.borderWidth, y + theme.borderWidth, x + width - theme.borderWidth * 2, y + height - theme.borderWidth * 2, !disabled ? hovered ? theme.hoverBgColor : theme.bgColor : theme.disabledBgColor);
            // draw scrollbar
            RENDERER.rect(x + theme.borderWidth, (int) (y + (getScroll() * (height - theme.borderWidth * 2) / getScrollPages())), x + width - theme.borderWidth * 2, (int) (y + (getScroll() + 1) * (height - theme.borderWidth * 2) / getScrollPages()), theme.sbColor);
        }
    }
}
