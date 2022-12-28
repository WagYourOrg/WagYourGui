package xyz.wagyourtail.wagyourgui.api.overlay;

import xyz.wagyourtail.wagyourgui.api.element.Element;
import xyz.wagyourtail.wagyourgui.api.element.Interactable;

public interface OverlayElement extends Element, Interactable {
    OverlayElement getOverlay();

    void setOverlay(OverlayElement overlay);

    default void onClose() {
    }

    void onOpened();

    boolean shouldClose();
}
