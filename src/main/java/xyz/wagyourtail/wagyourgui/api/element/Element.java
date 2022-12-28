package xyz.wagyourtail.wagyourgui.api.element;

public interface Element {

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

    void setPos(int x, int y);

    void setSize(int width, int height);

    void setBounds(int x, int y, int width, int height);

    boolean isWithinBounds(int x, int y);

}
