package xyz.wagyourtail.wagyourgui.mc.render;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import xyz.wagyourtail.wagyourgui.api.render.MutableTexture;

public class MCMutableTexture extends MCTexture<DynamicTexture> implements MutableTexture {

    public MCMutableTexture(DynamicTexture image, ResourceLocation location) {
        super(image, location);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        assert image.getPixels() != null;
        image.getPixels().setPixelRGBA(x, y, color);
    }

    @Override
    public void setPixels(int x, int y, int width, int height, int[] pixels) {
        NativeImage img = image.getPixels();
        assert img != null;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                img.setPixelRGBA(x + i, y + j, pixels[i + j * width]);
            }
        }
    }

    @Override
    public int getPixel(int x, int y) {
        assert image.getPixels() != null;
        return image.getPixels().getPixelRGBA(x, y);
    }

    @Override
    public int[] getPixels(int x, int y, int width, int height) {
        int[] pixels = new int[width * height];
        NativeImage img = image.getPixels();
        assert img != null;
        for (int i = x; i < width; i++) {
            for (int j = y; j < height; j++) {
                pixels[i + j * width] = img.getPixelRGBA(i, j);
            }
        }
        return pixels;
    }

    @Override
    public int getWidth() {
        assert image.getPixels() != null;
        return image.getPixels().getWidth();
    }

    @Override
    public int getHeight() {
        assert image.getPixels() != null;
        return image.getPixels().getHeight();
    }

    @Override
    public String getIdentifier() {
        return location.toString();
    }
}
