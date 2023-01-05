package xyz.wagyourtail.wagyourgui.api.render;

import xyz.wagyourtail.wagyourgui.api.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public interface Renderer<T extends Texture, M extends MutableTexture> {
    void rect(int x, int y, int width, int height, int color);

    void texturedRect(int x, int y, int width, int height, Texture tex, int u, int v, int textureWidth, int textureHeight);

    void texturedRect(int x, int y, int width, int height, Texture tex, int u, int v, int uw, int vh, int textureWidth, int textureHeight);

    void texturedRect(int x, int y, int width, int height, Texture tex, int u, int v, int textureWidth, int textureHeight, int color);

    void texturedRect(int x, int y, int width, int height, Texture tex, int u, int v, int uw, int vh, int textureWidth, int textureHeight, int color);

    void rotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, int color);

    void texturedRotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, Texture tex, int u, int v, int textureWidth, int textureHeight);

    void texturedRotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, Texture tex, int u, int v, int uw, int vh, int textureWidth, int textureHeight);

    void texturedRotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, Texture tex, int u, int v, int textureWidth, int textureHeight, int color);

    void texturedRotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, Texture tex, int u, int v, int uw, int vh, int textureWidth, int textureHeight, int color);

    void texturedRotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, Texture tex, int topLeftU, int topLeftV, int topRightU, int topRightV, int bottomLeftU, int bottomLeftV, int textureWidth, int textureHeight);

    void texturedRotatedRect(int topLeftX, int topLeftY, int topRightX, int topRightY, int bottomLeftX, int bottomLeftY, Texture tex, int topLeftU, int topLeftV, int topRightU, int topRightV, int bottomLeftU, int bottomLeftV, int textureWidth, int textureHeight, int color);

    void rotatedTextureRect(int x, int y, int width, int height, Texture tex, int topLeftU, int topLeftV, int topRightU, int topRightV, int bottomLeftU, int bottomLeftV, int textureWidth, int textureHeight, int color);

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

    List<String> wrapString(String text, int width, boolean wordWrap);

    default List<ColoredString> wrapString(ColoredString text, int width, boolean wordWrap) {
        List<ColoredString> wrapped = new ArrayList<>();
        ColoredString[] cur = {new ColoredString()};
        int[] curWidth = {0};
        text.visit((s, c) -> {
            String remaining = s;
            while (!remaining.isEmpty()) {
                if (curWidth[0] + getStringWidth(remaining) <= width) {
                    cur[0].append(remaining, c);
                    curWidth[0] += getStringWidth(remaining);
                    remaining = "";
                } else {
                    String trim = trimToWidth(remaining, width - curWidth[0]);
                    if (wordWrap) {
                        int lastSpace = trim.lastIndexOf(' ');
                        if (lastSpace > 0)
                            trim = trim.substring(0, lastSpace);
                    }
                    if (trim.length() > 0) {
                        cur[0].append(trim, c);
                        curWidth[0] += getStringWidth(trim);
                        remaining = remaining.substring(trim.length());
                    } else {
                        wrapped.add(cur[0]);
                        cur[0] = new ColoredString();
                        curWidth[0] = 0;
                    }
                }
            }
        });
        return wrapped;
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