package xyz.wagyourtail.wagyourgui.standalone.glfw;

public interface KeyListener {

    void onKey(int key, int scancode, int action, int mods);
    void onChar(int codepoint, int mods);

}
