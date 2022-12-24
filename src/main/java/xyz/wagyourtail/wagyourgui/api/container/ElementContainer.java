package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.Element;

public interface ElementContainer {
    void addElement(Element element);
    void removeElement(Element element);
    void clearElements();
}
