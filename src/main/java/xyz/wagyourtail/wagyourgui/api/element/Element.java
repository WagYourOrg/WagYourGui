package xyz.wagyourtail.wagyourgui.api.element;

import xyz.wagyourtail.wagyourgui.api.render.Renderer;

import java.util.ServiceLoader;

public interface Element {
    Renderer RENDERER = ServiceLoader.load(Renderer.class).findFirst().orElseThrow(() -> new RuntimeException("No RenderHelper found!"));

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void setX(int x);

    void setY(int y);

    void setWidth(int width);

    void setHeight(int height);

    void setPos(int x, int y);

    void setSize(int width, int height);

    void setBounds(int x, int y, int width, int height);

    boolean isWithinBounds(int x, int y);

    default boolean shouldFocus(int x, int y) {
        return isWithinBounds(x, y);
    }
}
