package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.Element;
import xyz.wagyourtail.wagyourgui.api.element.impl.Button;

public abstract class DropDownElementContainer extends PositionedElementContainer {
    protected int maxHeight;
    protected Button dropDown;
    protected ElementScroll scrollContainer;

    public DropDownElementContainer(int x, int y, int width, int height, int maxHeight, Button dropDownButton) {
        super(x, y, width, height);
        this.dropDown = dropDownButton;
        this.maxHeight = maxHeight;
        scrollContainer = new ElementScroll(x, y + dropDown.getHeight(), width, maxHeight - dropDown.getHeight());
        dropDownButton.setOnClick(this::toggle);
    }

    public void toggle() {
        toggle(null);
    }

    protected void toggle(Button b) {
        if (elements.contains(scrollContainer)) {
            removeElement(scrollContainer);
        } else {
            addElement(scrollContainer);
        }
    }

    public void addToScrollContainer(Element e) {
        scrollContainer.addElement(e);
    }

    public void removeFromScrollContainer(Element e) {
        scrollContainer.removeElement(e);
    }

    public void clearScrollContainer() {
        scrollContainer.clearElements();
    }

    protected static class ElementScroll extends ScrollingElementContainer {

        public ElementScroll(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
    }
}
