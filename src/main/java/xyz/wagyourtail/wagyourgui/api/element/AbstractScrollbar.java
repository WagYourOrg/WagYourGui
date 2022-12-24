package xyz.wagyourtail.wagyourgui.api.element;

import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;
import java.util.function.Consumer;

public abstract class AbstractScrollbar<T extends AbstractScrollbar> extends AbstractElement implements Themeable<Theme.ScrollbarTheme> {
    private Theme.ScrollbarTheme theme = Theme.currentTheme.scrollbar[0];

    private double scroll = 0;
    private double scrollPages = 1;

    private Consumer<T> onScroll;

    public AbstractScrollbar(int x, int y, int width, int height, double pages, Consumer<T> onScroll) {
        super(x, y, width, height);
        this.scrollPages = Math.max(1, pages);
        this.onScroll = onScroll;
    }

    @Override
    public void setThemeIndex(int index) {
        this.theme = Theme.currentTheme.scrollbar[index % Theme.currentTheme.scrollbar.length];
    }

    @Override
    public int getThemeIndex() {
        return Arrays.asList(Theme.currentTheme.scrollbar).indexOf(theme);
    }

    @Override
    public Theme.ScrollbarTheme getTheme() {
        return theme;
    }

    public void setScroll(double scroll) {
        this.scroll = Math.max(0, Math.min(scroll, scrollPages));
    }

    public double getScroll() {
        return scroll;
    }

    public double getScrollPages() {
        return scrollPages;
    }

    public void setScrollPages(double pages) {
        this.scrollPages = Math.max(1, pages);
    }

    public double getScrollbarSize(int size) {
        return size / scrollPages;
    }

    public Consumer<T> getOnScroll() {
        return onScroll;
    }

    public void setOnScroll(Consumer<T> onScroll) {
        this.onScroll = onScroll;
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        scroll += scroll;
        if (scroll < 0) scroll = 0;
        if (scroll > scrollPages - 1) scroll = scrollPages - 1;
        return true;
    }
}
