package xyz.wagyourtail.wagyourgui.api.element;

public interface Themeable<T> {

    int getThemeIndex();

    void setThemeIndex(int index);

    T getTheme();
}
