package xyz.wagyourtail.wagyourgui.api.screen;

import xyz.wagyourtail.wagyourgui.api.container.LayeredElementContainer;
import xyz.wagyourtail.wagyourgui.api.element.Element;
import xyz.wagyourtail.wagyourgui.api.element.Interactable;
import xyz.wagyourtail.wagyourgui.api.element.Renderable;
import xyz.wagyourtail.wagyourgui.api.element.Ticking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Screen implements Interactable, Ticking, Renderable, LayeredElementContainer {
    private final Map<Integer, ElementLayer> elements = new HashMap<>();
    public final Screen parent;
    private Element focusedElement = null;
    private int currentLayer = 0;

    public Screen(Screen parent) {
        this.parent = parent;
    }

    public void onInit() {
        elements.clear();
        currentLayer = 0;
    }

    public void close() {
        Element.RENDERER.openScreen(parent);
    }

    public void onFocusChanged(Element element) {
        focusedElement = element;
    }

    public Element getFocusedElement() {
        return focusedElement;
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

    @Override
    public void addElement(int layer, Element element) {
        if (!elements.containsKey(layer)) {
            elements.put(layer, new ElementLayer());
        }
    }

    @Override
    public void removeElement(int layer, Element element) {
        if (elements.containsKey(layer)) {
            elements.get(layer).elements.remove(element);
        }
    }

    @Override
    public void clearElements(int layer) {
        if (elements.containsKey(layer)) {
            elements.get(layer).elements.clear();
        }
    }

    @Override
    public void setCurrentLayer(int layer) {
        currentLayer = layer;
    }

    @Override
    public int getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public void setLayerVisible(int layer, boolean visible) {
        if (elements.containsKey(layer)) {
            elements.get(layer).setVisible(visible);
        }
    }

    @Override
    public void setLayerInteractable(int layer, boolean interactable) {
        if (elements.containsKey(layer)) {
            elements.get(layer).setInteractable(interactable);
        }
    }

    @Override
    public boolean isLayerVisible(int layer) {
        return elements.containsKey(layer) && elements.get(layer).isVisible();
    }

    @Override
    public boolean isLayerInteractable(int layer) {
        return elements.containsKey(layer) && elements.get(layer).isInteractable();
    }

    @Override
    public void onClicked(int mouseX, int mouseY, int button) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onClicked(mouseX, mouseY, button);
                    }
                }
            }
        }
    }

    @Override
    public void onReleased(int mouseX, int mouseY, int button) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onReleased(mouseX, mouseY, button);
                    }
                }
            }
        }
    }

    @Override
    public void onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onDragged(mouseX, mouseY, button, deltaX, deltaY);
                    }
                }
            }
        }
    }

    @Override
    public void onScrolled(int mouseX, int mouseY, double scroll) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onScrolled(mouseX, mouseY, scroll);
                    }
                }
            }
        }
    }

    @Override
    public void onCharTyped(char character, int keyCode) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onCharTyped(character, keyCode);
                    }
                }
            }
        }
    }

    @Override
    public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onKeyPressed(keyCode, scanCode, modifiers);
                    }
                }
            }
        }
    }

    @Override
    public void onKeyReleased(int keyCode, int scanCode, int modifiers) {
        for (ElementLayer l : elements.values()) {
            if (l.isVisible() && l.isInteractable()) {
                for (Element e : l.getElements()) {
                    if (e instanceof Interactable) {
                        ((Interactable) e).onKeyReleased(keyCode, scanCode, modifiers);
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
