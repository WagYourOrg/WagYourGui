package xyz.wagyourtail.wagyourgui.api.render;

public interface MutableTexture extends Texture {
    void setPixel(int x, int y, int color);
    void setPixels(int x, int y, int width, int height, int[] pixels);

    int getPixel(int x, int y);

    int[] getPixels(int x, int y, int width, int height);

    int getWidth();

    int getHeight();
}
