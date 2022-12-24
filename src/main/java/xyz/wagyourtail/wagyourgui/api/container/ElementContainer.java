package xyz.wagyourtail.wagyourgui.api.container;

import xyz.wagyourtail.wagyourgui.api.element.Element;

public interface ElementContainer {
    <T extends Element> T addElement(T element);
    void removeElement(Element element);
    void clearElements();
}
