package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.Element;

public abstract class PositionedLayeredElementContainer extends LayeredElementContainer implements Element {
    protected int x, y;
    protected int width, height;
    protected boolean visible = true;
    protected boolean enabled = true;

    public PositionedLayeredElementContainer(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isWithinBounds(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    @Override
    public <T extends Element> T addElement(T element) {
        if (
                !isWithinBounds(element.getX(), element.getY()) ||
                        !isWithinBounds(element.getX() + element.getWidth(), element.getY() + element.getHeight())
        ) {
            throw new IllegalArgumentException("Element is not within bounds of overlay!");
        }
        return super.addElement(element);
    }

    @Override
    public <T extends Element> T addElement(int layer, T element) {
        if (
                !isWithinBounds(element.getX(), element.getY()) ||
                        !isWithinBounds(element.getX() + element.getWidth(), element.getY() + element.getHeight())
        ) {
            throw new IllegalArgumentException("Element is not within bounds of overlay!");
        }
        return super.addElement(layer, element);
    }
}
