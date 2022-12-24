package xyz.wagyourtail.wagyourgui.standalone;

import xyz.wagyourtail.wagyourgui.api.render.MutableTexture;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.DynamicTexture;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.NativeImage;

public class SMutTexture extends STexture<DynamicTexture> implements MutableTexture {
    public SMutTexture(DynamicTexture image, String identifier) {
        super(image, identifier);
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

}
