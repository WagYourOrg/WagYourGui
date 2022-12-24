package xyz.wagyourtail.wagyourgui.api.render;

import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public interface Renderer<T extends Texture, M extends MutableTexture> {
    void rect(int x, int y, int width, int height, int color);

    void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex);
    void texturedRect(int x, int y, int width, int height, int u, int v, int uw, int vh, int textureWidth, int textureHeight, Texture tex);

    void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex, int color);
    void texturedRect(int x, int y, int width, int height, int u, int v, int uw, int vh, int textureWidth, int textureHeight, Texture tex, int color);

    void string(String text, int x, int y, int color);

    void string(String text, int x, int y, int color, boolean shadow);

    void centeredString(String text, int x, int y, int color);

    void centeredString(String text, int x, int y, int color, boolean shadow);

    void rightString(String text, int x, int y, int color);

    void rightString(String text, int x, int y, int color, boolean shadow);

    int getStringWidth(String text);

    int getStringWidth(ColoredString text);

    int getStringHeight();

    void stringTrimmed(String text, int x, int y, int width, int color);

    void stringTrimmed(String text, int x, int y, int width, int color, boolean shadow);

    void centeredStringTrimmed(String text, int x, int y, int width, int color);

    void centeredStringTrimmed(String text, int x, int y, int width, int color, boolean shadow);

    void rightStringTrimmed(String text, int x, int y, int width, int color);

    void rightStringTrimmed(String text, int x, int y, int width, int color, boolean shadow);

    void coloredString(ColoredString text, int x, int y);

    void coloredString(ColoredString text, int x, int y, boolean shadow);

    void centeredColoredString(ColoredString text, int x, int y);

    void centeredColoredString(ColoredString text, int x, int y, boolean shadow);

    void rightColoredString(ColoredString text, int x, int y);

    void rightColoredString(ColoredString text, int x, int y, boolean shadow);

    void coloredStringTrimmed(ColoredString text, int x, int y, int width);

    void coloredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow);

    void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width);

    void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow);

    void rightColoredStringTrimmed(ColoredString text, int x, int y, int width);

    void rightColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow);

    String trimToWidth(String text, int width);

    default ColoredString trimToWidth(ColoredString text, int width) {
        ColoredString trimmed = new ColoredString();
        int[] curWidth = {0};
        boolean[] done = {false};
        text.visit((s, c) -> {
            if (done[0]) return;
            if (curWidth[0] + getStringWidth(s) <= width) {
                trimmed.append(s, c);
                curWidth[0] += getStringWidth(s);
            } else {
                String trim = trimToWidth(s, width - curWidth[0]);
                if (trim.length() > 0) {
                    trimmed.append(trim, c);
                    curWidth[0] += getStringWidth(trim);
                    done[0] = true;
                }
            }
        });
        return trimmed;
    }

    void line(int x1, int y1, int x2, int y2, int color);

    void line(int x1, int y1, int x2, int y2, int color, int thickness);

    void gradientRect(int x, int y, int width, int height, int color1, int color2);

    T getTexture(String identifier);

    M createMutableTexture(String identifier, int width, int height);

    M createMutableTexture(String identifier, Texture image);

    void openScreen(Screen screen);

    void closeScreen();

    Screen getCurrentGuestScreen();

    Object getCurrentHostScreen();

    boolean isGuestScreen();
}