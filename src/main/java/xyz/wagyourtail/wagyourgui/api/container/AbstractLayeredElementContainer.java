package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.Element;
import xyz.wagyourtail.wagyourgui.api.element.Interactable;
import xyz.wagyourtail.wagyourgui.api.element.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LayeredElementContainer implements ElementContainer, Interactable, Renderable {
    protected final Map<Integer, ElementLayer> elements = new HashMap<>();
    protected Interactable focusedElement = null;
    protected int currentLayer = 0;
    @Override
    public void addElement(Element element) {
        addElement(currentLayer, element);
    }

    @Override
    public void removeElement(Element element) {
        removeElement(currentLayer, element);
    }

    @Override
    public void clearElements() {
        clearElements(currentLayer);
    }

    public void addElement(int layer, Element element) {
        if (!elements.containsKey(layer)) {
            elements.put(layer, new ElementLayer());
        }
    }

    public void removeElement(int layer, Element element) {
        if (elements.containsKey(layer)) {
            elements.get(layer).elements.remove(element);
        }
    }

    public void clearElements(int layer) {
        if (elements.containsKey(layer)) {
            elements.get(layer).elements.clear();
        }
    }

    public void setCurrentLayer(int layer) {
        currentLayer = layer;
    }

    public int getCurrentLayer() {
        return currentLayer;
    }

    public void setLayerVisible(int layer, boolean visible) {
        if (elements.containsKey(layer)) {
            elements.get(layer).setVisible(visible);
        }
    }

    public void setLayerInteractable(int layer, boolean interactable) {
        if (elements.containsKey(layer)) {
            elements.get(layer).setInteractable(interactable);
        }
    }

    public boolean isLayerVisible(int layer) {
        return elements.containsKey(layer) && elements.get(layer).isVisible();
    }

    public boolean isLayerInteractable(int layer) {
        return elements.containsKey(layer) && elements.get(layer).isInteractable();
    }

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        outer: for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable && ((Interactable) e).shouldFocus(mouseX, mouseY)) {
                        if (focusedElement != e) {
                            Interactable old = focusedElement;
                            focusedElement = (Interactable) e;
                            if (old != null) {
                                ((Interactable) old).onFocused(false);
                            }
                            focusedElement.onFocused(true);
                        }
                    }
                    break outer;
                }
            }
        }
        if (focusedElement != null && !focusedElement.shouldFocus(mouseX, mouseY)) {
            Interactable old = focusedElement;
            focusedElement = null;
            old.onFocused(false);
        }
        if (focusedElement != null) {
            return focusedElement.onClicked(mouseX, mouseY, button, mods);
        }
        return false;
    }

    @Override
    public boolean onReleased(int mouseX, int mouseY, int button, int mods) {
        if (focusedElement != null) {
            return focusedElement.onReleased(mouseX, mouseY, button, mods);
        }
        return false;
    }

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (focusedElement != null) {
            return focusedElement.onDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        if (focusedElement != null) {
            return focusedElement.onScrolled(mouseX, mouseY, scroll);
        }
        return false;
    }

    @Override
    public boolean onCharTyped(char character, int mods) {
        if (focusedElement != null) {
            return focusedElement.onCharTyped(character, mods);
        }
        return false;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (focusedElement != null) {
            return focusedElement.onKeyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        if (focusedElement != null) {
            return focusedElement.onKeyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Renderable) {
                        ((Renderable) e).onRender(mouseX, mouseY);
                    }
                }
            }
        }
    }

    private static class ElementLayer {
        private List<Element> elements;
        private boolean visible;
        private boolean interactable;

        public ElementLayer(List<Element> elements, boolean visible, boolean interactable) {
            this.elements = elements;
            this.visible = visible;
            this.interactable = interactable;
        }

        public ElementLayer() {
            this(new ArrayList<>(), true, true);
        }

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public boolean isInteractable() {
            return interactable;
        }

        public void setInteractable(boolean interactable) {
            this.interactable = interactable;
        }
    }
}
