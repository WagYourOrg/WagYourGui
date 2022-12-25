package xyz.wagyourtail.wagyourgui.standalone;

import org.lwjgl.opengl.GL11;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.render.Renderer;
import xyz.wagyourtail.wagyourgui.api.render.Texture;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;
import xyz.wagyourtail.wagyourgui.standalone.glfw.GLBuilder;
import xyz.wagyourtail.wagyourgui.standalone.glfw.GLFWSession;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.BaseTex;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.DynamicTexture;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.NativeImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SRenderer implements Renderer<STexture<BaseTex>, SMutTexture> {
    public static SRenderer INSTANCE;

    private GLFWSession session = new GLFWSession();

    public SRenderer() {
        INSTANCE = this;
    }

    @Override
    public void rect(int x, int y, int width, int height, int color) {
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP)
                .vertex(x, y).color(color).next()
                .vertex(x + width, y).color(color).next()
                .vertex(x, y + height).color(color).next()
                .vertex(x + width, y + height).color(color).next()
                .end();
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex) {
        ((STexture<BaseTex>) tex).getImage().bind();
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP, GLBuilder.VertexFormat.POS_TEX)
                .vertex(x, y + height).uv(u, v + height, textureWidth, textureHeight).next()
                .vertex(x + width, y + height).uv(u + width, v + height, textureWidth, textureHeight).next()
                .vertex(x + width, y).uv(u + width, v, textureWidth, textureHeight).next()
                .vertex(x, y).uv(u, v, textureWidth, textureHeight).next()
                .end();
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int uw, int vh, int textureWidth, int textureHeight, Texture tex) {
        ((STexture<BaseTex>) tex).getImage().bind();
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP, GLBuilder.VertexFormat.POS_TEX)
                .vertex(x, y + height).uv(u, v + vh, textureWidth, textureHeight).next()
                .vertex(x + width, y + height).uv(u + uw, v + vh, textureWidth, textureHeight).next()
                .vertex(x + width, y).uv(u + uw, v, textureWidth, textureHeight).next()
                .vertex(x, y).uv(u, v, textureWidth, textureHeight).next()
                .end();
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex, int color) {
        ((STexture<BaseTex>) tex).getImage().bind();
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP, GLBuilder.VertexFormat.POS_COL_TEX)
                .vertex(x, y + height).color(color).uv(u, v + height, textureWidth, textureHeight).next()
                .vertex(x + width, y + height).color(color).uv(u + width, v + height, textureWidth, textureHeight).next()
                .vertex(x + width, y).color(color).uv(u + width, v, textureWidth, textureHeight).next()
                .vertex(x, y).color(color).uv(u, v, textureWidth, textureHeight).next()
                .end();
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int uw, int vh, int textureWidth, int textureHeight, Texture tex, int color) {
        ((STexture<BaseTex>) tex).getImage().bind();
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP, GLBuilder.VertexFormat.POS_COL_TEX)
                .vertex(x, y + height).color(color).uv(u, v + vh, textureWidth, textureHeight).next()
                .vertex(x + width, y + height).color(color).uv(u + uw, v + vh, textureWidth, textureHeight).next()
                .vertex(x + width, y).color(color).uv(u + uw, v, textureWidth, textureHeight).next()
                .vertex(x, y).color(color).uv(u, v, textureWidth, textureHeight).next()
                .end();
    }

    @Override
    public void string(String text, int x, int y, int color) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawString(text, x, y);
    }

    @Override
    public void string(String text, int x, int y, int color, boolean shadow) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawString(text, x, y);
    }

    @Override
    public void centeredString(String text, int x, int y, int color) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawString(text, x - session.font.getWidth(text) / 2, y);
    }

    @Override
    public void centeredString(String text, int x, int y, int color, boolean shadow) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawString(text, x - session.font.getWidth(text) / 2, y);
    }

    @Override
    public void rightString(String text, int x, int y, int color) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawString(text, x - session.font.getWidth(text), y);
    }

    @Override
    public void rightString(String text, int x, int y, int color, boolean shadow) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawString(text, x - session.font.getWidth(text), y);
    }

    @Override
    public int getStringWidth(String text) {
        return (int) session.font.getWidth(text);
    }

    @Override
    public int getStringWidth(ColoredString text) {
        int[] width = {0};
        text.visit((s, c) -> width[0] += getStringWidth(s));
        return width[0];
    }

    @Override
    public int getStringHeight() {
        return session.font.FONT_HEIGHT;
    }

    @Override
    public void stringTrimmed(String text, int x, int y, int width, int color) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawTrimmed(text, x, y, width);
    }

    @Override
    public void stringTrimmed(String text, int x, int y, int width, int color, boolean shadow) {
        GLBuilder.getImmediate().color(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        session.font.drawTrimmed(text, x, y, width);
    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int width, int color) {
        if (getStringWidth(text) > width) {
            stringTrimmed(text, x, y, color, width);
        } else {
            centeredString(text, x, y, color);
        }
    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int width, int color, boolean shadow) {
        if (getStringWidth(text) > width) {
            stringTrimmed(text, x, y, width, color);
        } else {
            centeredString(text, x, y, color);
        }
    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int width, int color) {
        if (getStringWidth(text) > width) {
            stringTrimmed(text, x - width, y, width, color);
        } else {
            rightString(text, x, y, color);
        }
    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int width, int color, boolean shadow) {
        if (getStringWidth(text) > width) {
            stringTrimmed(text, x - width, y, width, color);
        } else {
            rightString(text, x, y, color);
        }
    }

    @Override
    public void coloredString(ColoredString text, int x, int y) {
        int[] curX = {x};
        text.visit((s, c) -> {
            string(s, curX[0], y, c);
            curX[0] += getStringWidth(s);
        });
    }

    @Override
    public void coloredString(ColoredString text, int x, int y, boolean shadow) {
        coloredString(text, x, y);
    }

    @Override
    public void centeredColoredString(ColoredString text, int x, int y) {
        int[] curX = {x - getStringWidth(text) / 2};
        text.visit((s, c) -> {
            string(s, curX[0], y, c);
            curX[0] += getStringWidth(s);
        });
    }

    @Override
    public void centeredColoredString(ColoredString text, int x, int y, boolean shadow) {
        centeredColoredString(text, x, y);
    }

    @Override
    public void rightColoredString(ColoredString text, int x, int y) {
        int[] curX = {x - getStringWidth(text)};
        text.visit((s, c) -> {
            string(s, curX[0], y, c);
            curX[0] += getStringWidth(s);
        });
    }

    @Override
    public void rightColoredString(ColoredString text, int x, int y, boolean shadow) {
        rightColoredString(text, x, y);
    }

    @Override
    public void coloredStringTrimmed(ColoredString text, int x, int y, int width) {
        coloredString(trimToWidth(text, width), x, y);
    }

    @Override
    public void coloredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {
        coloredString(trimToWidth(text, width), x, y);
    }

    @Override
    public void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width) {
        centeredColoredString(trimToWidth(text, width), x, y);
    }

    @Override
    public void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {
        centeredColoredString(trimToWidth(text, width), x, y);
    }

    @Override
    public void rightColoredStringTrimmed(ColoredString text, int x, int y, int width) {
        rightColoredString(trimToWidth(text, width), x, y);
    }

    @Override
    public void rightColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {
        rightColoredString(trimToWidth(text, width), x, y);
    }

    @Override
    public String trimToWidth(String text, int width) {
        return session.font.trimToWidth(text, width);
    }

    @Override
    public List<String> wrapString(String text, int width, boolean wordWrap) {
        List<String> lines = new ArrayList<>();
        int curWidth = 0;
        String remaining = text;
        while (!remaining.isEmpty()) {
            String trim = trimToWidth(remaining, width);
            if (wordWrap) {
                int lastSpace = trim.lastIndexOf(' ');
                if (lastSpace > 0)
                    trim = trim.substring(0, lastSpace);
            }
            lines.add(trim);
            remaining = remaining.substring(trim.length());
        }
        return lines;
    }

    @Override
    public void line(int x1, int y1, int x2, int y2, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x1, y1, 0);
        GL11.glRotatef((float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1)), 0, 0, 1);
        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        GL11.glScaled(length, 1, 1);
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP)
                .vertex(0, 1).color(color).next()
                .vertex(1, 1).color(color).next()
                .vertex(1, 0).color(color).next()
                .vertex(0, 0).color(color).next()
                .end();
        GL11.glPopMatrix();
    }

    @Override
    public void line(int x1, int y1, int x2, int y2, int color, int thickness) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x1, y1, 0);
        GL11.glRotatef((float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1)), 0, 0, 1);
        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        GL11.glScaled(length, thickness, 1);
        if (thickness < 2) {
            GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP)
                    .vertex(0, 1).color(color).next()
                    .vertex(1, 1).color(color).next()
                    .vertex(1, 0).color(color).next()
                    .vertex(0, 0).color(color).next()
                    .end();
        } else {
            GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP)
                    .vertex(0, thickness / 2).color(color).next()
                    .vertex(1, thickness / 2).color(color).next()
                    .vertex(1, - thickness / 2).color(color).next()
                    .vertex(0, - thickness / 2).color(color).next()
                    .end();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void gradientRect(int x, int y, int width, int height, int color1, int color2) {
        GLBuilder.getImmediate().begin(GL11.GL_TRIANGLE_STRIP)
                .vertex(x, y + height).color(color1).next()
                .vertex(x + width, y + height).color(color2).next()
                .vertex(x, y).color(color1).next()
                .vertex(x + width, y).color(color2).next()
                .end();
    }

    @Override
    public STexture<BaseTex> getTexture(String identifier) {
        BaseTex tex = session.textures.get(identifier);
        return new STexture<>(tex, identifier);
    }

    @Override
    public SMutTexture createMutableTexture(String identifier, int width, int height) {
        if (session.textures.containsKey(identifier)) {
            throw new IllegalArgumentException("Texture with identifier " + identifier + " already exists");
        }
        NativeImage image = new NativeImage(width, height, true);
        DynamicTexture tex = new DynamicTexture(image);
        session.textures.put(identifier, tex);
        return new SMutTexture(tex, identifier);
    }

    @Override
    public SMutTexture createMutableTexture(String identifier, Texture image) {
        if (session.textures.containsKey(identifier)) {
            throw new IllegalArgumentException("Texture with identifier " + identifier + " already exists");
        }
        if (image instanceof SMutTexture) {
            NativeImage i = new NativeImage(((SMutTexture) image).getWidth(), ((SMutTexture) image).getHeight(), true);
            i.copyFrom(((SMutTexture) image).getImage().getPixels());
            DynamicTexture tex = new DynamicTexture(i);
            session.textures.put(identifier, tex);
            return new SMutTexture(tex, identifier);
        } else {
            throw new UnsupportedOperationException("Cannot create mutable texture from non-mutable texture yet");
        }
    }

    @Override
    public void openScreen(Screen screen) {
        if (!session.isRunning()) {
            try {
                session.start(screen);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            session.setScreen(screen);
        }
    }

    @Override
    public void closeScreen() {
        session.getScreen().close();
    }

    @Override
    public Screen getCurrentGuestScreen() {
        return session.getScreen();
    }

    @Override
    public Object getCurrentHostScreen() {
        return null;
    }

    @Override
    public boolean isGuestScreen() {
        return true;
    }
}
