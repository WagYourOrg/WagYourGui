package xyz.wagyourtail.wagyourgui.api.element;


import xyz.wagyourtail.wagyourgui.api.render.Renderer;

import java.util.ServiceLoader;

public interface Renderable {

    Renderer<?, ?> RENDERER = ServiceLoader.load(Renderer.class).iterator().next();

    void onRender(int mouseX, int mouseY);

    void setVisible(boolean visible);

    boolean isVisible();
}
