package xyz.wagyourtail.wagyourgui.api.element;

public interface Element {

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
}
