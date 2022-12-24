package xyz.wagyourtail.wagyourgui.api.render;

import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public interface Renderer {
    void drawRect(int x, int y, int width, int height, int color);

    void drawTexturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex);

    void drawTexturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex, int color);

    void drawString(String text, int x, int y, int color);

    void drawString(String text, int x, int y, int color, boolean shadow);

    void drawCenteredString(String text, int x, int y, int color);

    void drawCenteredString(String text, int x, int y, int color, boolean shadow);

    void drawRightString(String text, int x, int y, int color);

    void drawRightString(String text, int x, int y, int color, boolean shadow);

    int getStringWidth(String text);

    int getStringHeight();

    void drawStringTrimmed(String text, int x, int y, int color, int width);

    void drawStringTrimmed(String text, int x, int y, int color, int width, boolean shadow);

    void drawCenteredStringTrimmed(String text, int x, int y, int color, int width);

    void drawCenteredStringTrimmed(String text, int x, int y, int color, int width, boolean shadow);

    void drawRightStringTrimmed(String text, int x, int y, int color, int width);

    void drawRightStringTrimmed(String text, int x, int y, int color, int width, boolean shadow);

    void drawColoredString(ColoredString text, int x, int y);

    void drawColoredString(ColoredString text, int x, int y, boolean shadow);

    void drawCenteredColoredString(ColoredString text, int x, int y);

    void drawCenteredColoredString(ColoredString text, int x, int y, boolean shadow);

    void drawRightColoredString(ColoredString text, int x, int y);

    void drawRightColoredString(ColoredString text, int x, int y, boolean shadow);

    void drawColoredStringTrimmed(ColoredString text, int x, int y, int width);

    void drawColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow);

    void drawCenteredColoredStringTrimmed(ColoredString text, int x, int y, int width);

    void drawCenteredColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow);

    void drawRightColoredStringTrimmed(ColoredString text, int x, int y, int width);

    void drawRightColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow);

    void drawLine(int x1, int y1, int x2, int y2, int color);

    void drawGradientRect(int x, int y, int width, int height, int color1, int color2);

    void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color);

    void drawTexturedTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int u1, int v1, int u2, int v2, int u3, int v3, int textureWidth, int textureHeight, Texture tex);

    void drawCircle(int x, int y, int radius, int color);

    void drawTexturedCircle(int x, int y, int radius, int u, int v, int tRadius, int textureWidth, int textureHeight, Texture tex);

    Texture getTexture(String identifier);

    MutableTexture createMutableTexture(int width, int height);

    MutableTexture createMutableTexture(Texture image);

    void openScreen(Screen screen);

    void closeScreen();

    Screen getCurrentScreen();
}