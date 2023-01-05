package xyz.wagyourtail.wagyourgui.api.element;

public abstract class AbstractElement implements Element, Interactable, Renderable {
    protected int x;
    protected int y;

    protected int width;
    protected int height;

    public AbstractElement(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
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
        setX(x);
        setY(y);
    }

    @Override
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        setPos(x, y);
        setSize(width, height);
    }

    @Override
    public boolean isWithinBounds(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }
}
