package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.*;

import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class LayeredElementContainer implements ElementContainer, Interactable, Renderable, Ticking {
    protected ElementLayer[] elements = new ElementLayer[1];
    protected Interactable focusedElement = null;
    protected int currentLayer = 0;

    @Override
    public <T extends Element> T addElement(T element) {
        return addElement(currentLayer, element);
    }

    @Override
    public void removeElement(Element element) {
        removeElement(currentLayer, element);
    }

    @Override
    public void clearElements() {
        clearElements(currentLayer);
    }

    public <T extends Element> T addElement(int layer, T element) {
        if (layer < elements.length && elements[layer] != null) {
            elements[layer].elements.add(element);
        } else if (layer < elements.length) {
            elements[layer] = new ElementLayer(layer);
            elements[layer].elements.add(element);
        } else {
            elements = Arrays.copyOf(elements, elements.length << 1);
            elements[layer] = new ElementLayer(layer);
            elements[layer].elements.add(element);
        }
        return element;
    }

    public void removeElement(int layer, Element element) {
        if (layer < elements.length && elements[layer] != null) {
            elements[layer].elements.remove(element);
        }
    }

    public void clearElements(int layer) {
        if (layer < elements.length && elements[layer] != null) {
            elements[layer] = null;
        }
    }

    public int getCurrentLayer() {
        return currentLayer;
    }

    public void setCurrentLayer(int layer) {
        currentLayer = layer;
    }

    public void setLayerVisible(int layer, boolean visible) {
        if (layer < elements.length && elements[layer] != null) {
            elements[layer].visible = visible;
        }
    }

    public void setLayerInteractable(int layer, boolean interactable) {
        if (layer < elements.length && elements[layer] != null) {
            elements[layer].interactable = interactable;
        }
    }

    public boolean isLayerVisible(int layer) {
        return layer < elements.length && elements[layer] != null && elements[layer].visible;
    }

    public boolean isLayerInteractable(int layer) {
        return layer < elements.length && elements[layer] != null && elements[layer].interactable;
    }

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        outer:
        for (int i = elements.length - 1; i >= 0; i--) {
            ElementLayer l = elements[i];
            if (l == null) continue;
            if (l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable && ((Interactable) e).shouldFocus(mouseX, mouseY)) {
                        if (e instanceof Disableable && ((Disableable) e).isDisabled()) continue;
                        if (focusedElement != e) {
                            Interactable old = focusedElement;
                            focusedElement = (Interactable) e;
                            if (old != null) {
                                old.onFocused(false);
                            }
                            focusedElement.onFocused(true);
                        }
                        break outer;
                    }
                }
            }
        }
        if (focusedElement != null && (!focusedElement.shouldFocus(mouseX, mouseY) || (focusedElement instanceof Disableable && ((Disableable) focusedElement).isDisabled()))) {
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
    public void onTick() {
        for (ElementLayer l : elements) {
            if (l == null) continue;
            if (l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Ticking) {
                        ((Ticking) e).onTick();
                    }
                }
            }
        }
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        for (ElementLayer l : elements) {
            if (l == null) continue;
            if (l.isVisible()) {
                Iterator<Element> it = l.getElements().descendingIterator();
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e instanceof Renderable && (!(e instanceof Disableable) || !((Disableable) e).isHidden())) {
                        ((Renderable) e).onRender(mouseX, mouseY);
                    }
                }
            }
        }
    }

    public static class ElementLayer implements ElementContainer {
        private int layer;
        private Deque<Element> elements;
        private boolean visible;
        private boolean interactable;

        ElementLayer(Deque<Element> elements, int layer, boolean visible, boolean interactable) {
            this.elements = elements;
            this.visible = visible;
            this.interactable = interactable;
            this.layer = layer;
        }

        public ElementLayer(int layer) {
            this(new LinkedList<>(), layer, true, true);
        }

        public Deque<Element> getElements() {
            return elements;
        }

        public void setElements(Deque<Element> elements) {
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

        public int getLayer() {
            return layer;
        }

        @Override
        public <T extends Element> T addElement(T element) {
            elements.add(element);
            return element;
        }

        @Override
        public void removeElement(Element element) {
            elements.remove(element);
        }

        @Override
        public void clearElements() {
            elements.clear();
        }
    }
}
