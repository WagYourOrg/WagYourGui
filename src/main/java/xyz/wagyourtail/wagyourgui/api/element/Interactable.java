package xyz.wagyourtail.wagyourgui.api.element;

public interface Interactable {
    default boolean shouldFocus(int x, int y) {
        return ((Element) this).isWithinBounds(x, y) && isEnabled();
    }
    default boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        return false;
    }

    default boolean onReleased(int mouseX, int mouseY, int button, int mods) {
        return false;
    }

    default boolean onDragged(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    default boolean onScrolled(int mouseX, int mouseY, double scroll) {
        return false;
    }

    default boolean onCharTyped(char character, int mods) {
        return false;
    }

    default boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default boolean onKeyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    default void onFocused(boolean focused) {}

    void setEnabled(boolean enabled);

    boolean isEnabled();
}
