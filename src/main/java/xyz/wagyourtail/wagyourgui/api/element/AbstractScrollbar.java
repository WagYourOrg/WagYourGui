package xyz.wagyourtail.wagyourgui.api.element;

import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;

public class Scrollbar extends AbstractElement implements Themeable<Theme.ScrollbarTheme> {
    private Theme.ScrollbarTheme theme = Theme.currentTheme.scrollbar[0];

    private double pages = 1;
    private double scrolled = 0;

    public Scrollbar(int x, int y, int width, int height, double pages) {
        super(x, y, width, height);
        this.pages = pages;
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

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        
    }

    @Override
    public void onRender(int mouseX, int mouseY) {

    }
}
