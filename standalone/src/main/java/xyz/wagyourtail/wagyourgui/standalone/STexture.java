package xyz.wagyourtail.wagyourgui.standalone;

import xyz.wagyourtail.wagyourgui.api.render.Texture;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.BaseTex;

public class STexture<T extends BaseTex> implements Texture {
    protected final T image;
    protected final String identifier;

    public STexture(T image, String identifier) {
        this.image = image;
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public T getImage() {
        return image;
    }
}
