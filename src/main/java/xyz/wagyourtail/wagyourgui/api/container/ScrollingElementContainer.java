package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.Disableable;
import xyz.wagyourtail.wagyourgui.api.element.Element;
import xyz.wagyourtail.wagyourgui.api.element.Renderable;
import xyz.wagyourtail.wagyourgui.api.element.impl.VerticalScrollbar;

import java.util.Iterator;

public abstract class ScrollingElementContainer extends PositionedElementContainer {
    public final VerticalScrollbar scrollbar;
    protected int offsetY = 0;

    public ScrollingElementContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        addElement(this.scrollbar = new VerticalScrollbar(x + width - 12, y, 12, height, 1, this::onScrolled));
    }

    public void onScrolled(VerticalScrollbar sb) {
        int newOffset = (int) (sb.getScroll() * height);
        if (newOffset != offsetY) {
            int diff = offsetY - newOffset;
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
        scrollbar.setHidden(scrollbar.getScrollPages() == 1);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        if (scrollbar != null) scrollbar.setX(x + width - 12);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        if (scrollbar != null) scrollbar.setY(y);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (scrollbar != null) scrollbar.setX(x + width - 12);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (scrollbar != null) scrollbar.setHeight(height);
    }

    @Override
    public <T extends Element> T addElement(T element) {
        elements.add(element);
        // update y position
        element.setY(element.getY() + offsetY);
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
    public boolean onScrolled(int mouseX, int mouseY, double scroll) {
        if (focusedElement == null) {
            scrollbar.onScrolled(mouseX, mouseY, scroll);
        } else {
            super.onScrolled(mouseX, mouseY, scroll);
        }
        return true;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        Iterator<Element> it = elements.descendingIterator();
        while (it.hasNext()) {
            Element e = it.next();
            if (e instanceof Renderable && (!(e instanceof Disableable) || !((Disableable) e).isHidden())) {
                if (isWithinBounds(e.getX(), e.getY()) && isWithinBounds(e.getX() + e.getWidth(), e.getY() + e.getHeight())) {
                    ((Renderable) e).onRender(mouseX, mouseY);
                }
            }
        }
    }
}
