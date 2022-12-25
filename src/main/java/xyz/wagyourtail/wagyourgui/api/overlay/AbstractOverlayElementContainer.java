package xyz.wagyourtail.wagyourgui.api.overlay;

import xyz.wagyourtail.wagyourgui.api.container.ElementContainer;
import xyz.wagyourtail.wagyourgui.api.element.Element;
import xyz.wagyourtail.wagyourgui.api.element.Interactable;
import xyz.wagyourtail.wagyourgui.api.element.Renderable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOverlayElementContainer extends AbstractOverlay implements ElementContainer {

    private final List<Element> elements = new ArrayList<>();
    private Interactable focusedElement = null;

    public AbstractOverlayElementContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public <T extends Element> T addElement(T element) {
        elements.add(element);
        if (
                !isWithinBounds(element.getX(), element.getY()) ||
                        !isWithinBounds(element.getX() + element.getWidth(), element.getY() + element.getHeight())
        ) {
            throw new IllegalArgumentException("Element is not within bounds of overlay!");
        }
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

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        if (super.onClicked(mouseX, mouseY, button, mods)) return true;
        for (Element e : elements) {
            if (e instanceof Interactable && ((Interactable) e).shouldFocus(mouseX, mouseY)) {
                if (focusedElement != e) {
                    Interactable old = focusedElement;
                    focusedElement = (Interactable) e;
                    if (old != null) {
                        old.onFocused(false);
                    }
                    focusedElement.onFocused(true);
                }
                break;
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
        if (super.onReleased(mouseX, mouseY, button, mods)) return true;
        if (focusedElement != null) {
            return focusedElement.onReleased(mouseX, mouseY, button, mods);
        }
        return false;
    }

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (super.onDragged(mouseX, mouseY, button, deltaX, deltaY)) return true;
        if (focusedElement != null) {
            return focusedElement.onDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        if (super.onScrolled(mouseX, mouseY, scroll)) return true;
        if (focusedElement != null) {
            return focusedElement.onScrolled(mouseX, mouseY, scroll);
        }
        return false;
    }

    @Override
    public boolean onCharTyped(char character, int mods) {
        if (super.onCharTyped(character, mods)) return true;
        if (focusedElement != null) {
            return focusedElement.onCharTyped(character, mods);
        }
        return false;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.onKeyPressed(keyCode, scanCode, modifiers)) return true;
        if (focusedElement != null) {
            return focusedElement.onKeyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        if (super.onKeyReleased(keyCode, scanCode, modifiers)) return true;
        if (focusedElement != null) {
            return focusedElement.onKeyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        super.onRender(mouseX, mouseY);
        for (Element e : elements) {
            if (e instanceof Renderable && ((Renderable) e).isVisible()) {
                ((Renderable) e).onRender(mouseX, mouseY);
            }
        }
    }
}
