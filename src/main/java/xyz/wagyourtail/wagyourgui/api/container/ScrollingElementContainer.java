package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.*;

import java.util.HashSet;
import java.util.Set;

public class ScrollingElementContainer extends AbstractElement implements ElementContainer {
    protected final Set<Element> elements = new HashSet<>();
    private Interactable focusedElement = null;
    public final VerticalScrollbar scrollbar;
    protected int offsetY = 0;

    public ScrollingElementContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.scrollbar = addElement(new VerticalScrollbar(x + width - 12, y, 12, height, 1, this::onScrolled));
    }

    public void onScrolled(VerticalScrollbar sb) {
        int newOffset = (int) (sb.getScroll() * height);
        if (newOffset != offsetY) {
            int diff =  offsetY - newOffset;
            offsetY = newOffset;
            for (Element e : elements) {
                if (e == scrollbar) continue;
                e.setY(e.getY() - diff);
            }
        }
    }

    private void updateScrollPages() {
        int max = 0;
        int min = 0;

        for (Element e : elements) {
            if (e == scrollbar) continue;
            if (e.getY() + e.getHeight() > max) max = e.getY() + e.getHeight();
            if (e.getY() < min) min = e.getY();
        }

        int diff = max - min;
        scrollbar.setScrollPages(diff / (double) height);
        scrollbar.setVisible(scrollbar.getScrollPages() != 1);
    }

    @Override
    public <T extends Element> T addElement(T element) {
        elements.add(element);
        updateScrollPages();
        return element;
    }

    @Override
    public void removeElement(Element element) {
        elements.remove(element);
        updateScrollPages();
    }

    @Override
    public void clearElements() {
        elements.clear();
        updateScrollPages();
    }

    @Override
    public void onFocused(boolean focused) {
        if (!focused) {
            Interactable old = focusedElement;
            focusedElement = null;
            if (old != null) old.onFocused(false);
        }
    }

    @Override
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        if (focusedElement == null) {
            scrollbar.onScrolled(mouseX, mouseY, scroll);
        } else {
            focusedElement.onScrolled(mouseX, mouseY, scroll);
        }
        return true;
    }

    @Override
    public boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (focusedElement == null) {
            scrollbar.onDragged(mouseX, mouseY, button, deltaX, deltaY);
        } else {
            focusedElement.onDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return true;
    }

    @Override
    public boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        if (focusedElement != null) {
            return focusedElement.onKeyReleased(keyCode, scanCode, modifiers);
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
    public boolean onCharTyped(char character, int mods) {
        if (focusedElement != null) {
            return focusedElement.onCharTyped(character, mods);
        }
        return false;
    }

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
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
            }
            break;
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
    public void onRender(int mouseX, int mouseY) {
        for (Element e : elements) {
            if (e instanceof Renderable && ((Renderable) e).isVisible()) {
                ((Renderable) e).onRender(mouseX, mouseY);
            }
        }
    }
}
