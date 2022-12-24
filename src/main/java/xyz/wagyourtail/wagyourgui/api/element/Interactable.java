package xyz.wagyourtail.wagyourgui.api.element;

public interface Interactable {
    default void onClicked(int mouseX, int mouseY, int button) {}

    default void onReleased(int mouseX, int mouseY, int button) {}

    default void onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {}

    default void onScrolled(int mouseX, int mouseY, double scroll) {}

    default void onCharTyped(char character, int keyCode) {}

    default void onKeyPressed(int keyCode, int scanCode, int modifiers) {}

    default void onKeyReleased(int keyCode, int scanCode, int modifiers) {}

    default void onFocused(boolean focused) {}
}
