package xyz.wagyourtail.wagyourgui.api.element;

public interface Themeable<T> {

    void setThemeIndex(int index);
    int getThemeIndex();

    T getTheme();
}
