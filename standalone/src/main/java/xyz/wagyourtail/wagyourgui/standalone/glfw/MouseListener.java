package xyz.wagyourtail.wagyourgui.standalone.glfw;

public interface MouseListener {

    void onMouseButton(int button, int action, int mods);

    void onScroll(double dx, double dy);

    void onMousePos(double x, double y);

}
