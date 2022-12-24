package xyz.wagyourtail.wagyourgui.render;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import xyz.wagyourtail.wagyourgui.api.render.Texture;

public class MCTexture<T extends AbstractTexture> implements Texture {
    protected final T image;
    protected final ResourceLocation location;

    public MCTexture(T image, ResourceLocation location) {
        this.image = image;
        this.location = location;
    }

    public T getImage() {
        return image;
    }

    @Override
    public String getIdentifier() {
        return location.toString();
    }
}
