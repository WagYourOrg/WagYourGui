package xyz.wagyourtail.wagyourgui.api.element;

import xyz.wagyourtail.wagyourgui.api.container.ElementContainer;
import xyz.wagyourtail.wagyourgui.api.container.ScrollingElementContainer;

public class Selector extends ScrollingElementContainer implements ElementContainer {
    private final Button selected;
    private int openedHeight;

    public Selector(int x, int y, int width, int height, int openedHeight, Button selected) {
        super(x, y, width, height);
        this.openedHeight = openedHeight;
        this.selected = selected;
        selected.setBounds(x, y, width, height);
    }

    @Override
    public <T extends Element> T addElement(T element) {
        return null;
    }

    @Override
    public void removeElement(Element element) {

    }

    @Override
    public void clearElements() {

    }

    @Override
    public void onRender(int mouseX, int mouseY) {

    }
}
