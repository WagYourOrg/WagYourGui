package xyz.wagyourtail.wagyourgui.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.render.Renderer;
import xyz.wagyourtail.wagyourgui.api.render.Texture;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public class MCRenderer extends GuiComponent implements Renderer<MCTexture<AbstractTexture>, MCMutableTexture> {
    public static MCRenderer INSTANCE;

    public MCRenderer() {
        INSTANCE = this;
    }

    Minecraft mc = Minecraft.getInstance();
    PoseStack pose;

    public void bindMatrixStack(PoseStack pose) {
        this.pose = pose;
    }

    @Override
    public void rect(int x, int y, int width, int height, int color) {
        fill(pose, x, y, x + width, y + height, color);
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex) {
        RenderSystem.setShaderTexture(0, ((MCTexture) tex).location);
        blit(pose, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex, int color) {
        RenderSystem.setShaderTexture(0, ((MCTexture) tex).location);
        blit(pose, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }

    @Override
    public void string(String text, int x, int y, int color) {
        mc.font.draw(pose, text, x, y, color);
    }

    @Override
    public void string(String text, int x, int y, int color, boolean shadow) {
        mc.font.drawShadow(pose, text, x, y, color, shadow);
    }

    @Override
    public void centeredString(String text, int x, int y, int color) {
        mc.font.draw(pose, text, x - mc.font.width(text) / 2f, y, color);
    }

    @Override
    public void centeredString(String text, int x, int y, int color, boolean shadow) {
        mc.font.drawShadow(pose, text, x - mc.font.width(text) / 2f, y, color, shadow);
    }

    @Override
    public void rightString(String text, int x, int y, int color) {
        mc.font.draw(pose, text, x - mc.font.width(text), y, color);
    }

    @Override
    public void rightString(String text, int x, int y, int color, boolean shadow) {
        mc.font.drawShadow(pose, text, x - mc.font.width(text), y, color, shadow);
    }

    @Override
    public int getStringWidth(String text) {
        return mc.font.width(text);
    }

    @Override
    public int getStringWidth(ColoredString text) {
        int[] width = {0};
        text.visit((s, c) -> width[0] += mc.font.width(s));
        return width[0];
    }

    @Override
    public int getStringHeight() {
        return mc.font.lineHeight;
    }

    @Override
    public void stringTrimmed(String text, int x, int y, int color, int width) {
        stringTrimmed(text, x, y, color, width, false);
    }

    @Override
    public void stringTrimmed(String text, int x, int y, int color, int width, boolean shadow) {
        mc.font.drawShadow(pose, mc.font.plainSubstrByWidth(text, width), x, y, color, shadow);
    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int color, int width) {
        centeredStringTrimmed(text, x, y, color, width, false);
    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int color, int width, boolean shadow) {
        mc.font.drawShadow(pose, mc.font.plainSubstrByWidth(text, width), x - mc.font.width(mc.font.plainSubstrByWidth(text, width)) / 2f, y, color, shadow);
    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int color, int width) {
        rightStringTrimmed(text, x, y, color, width, false);
    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int color, int width, boolean shadow) {
        mc.font.drawShadow(pose, mc.font.plainSubstrByWidth(text, width), x - mc.font.width(mc.font.plainSubstrByWidth(text, width)), y, color, shadow);
    }

    @Override
    public void coloredString(ColoredString text, int x, int y) {
        coloredString(text, x, y, false);
    }

    @Override
    public void coloredString(ColoredString text, int x, int y, boolean shadow) {
        float[] curX = {x};
        text.visit((s, c) -> {
            mc.font.drawShadow(pose, s, curX[0], y, c, shadow);
            curX[0] += mc.font.width(s);
        });
    }

    @Override
    public void centeredColoredString(ColoredString text, int x, int y) {
        centeredColoredString(text, x, y, false);
    }

    @Override
    public void centeredColoredString(ColoredString text, int x, int y, boolean shadow) {
        float[] curX = {x - getStringWidth(text) / 2f};
        text.visit((s, c) -> {
            mc.font.drawShadow(pose, s, curX[0], y, c, shadow);
            curX[0] += mc.font.width(s);
        });
    }

    @Override
    public void rightColoredString(ColoredString text, int x, int y) {
        rightColoredString(text, x, y, false);
    }

    @Override
    public void rightColoredString(ColoredString text, int x, int y, boolean shadow) {
        float[] curX = {x - getStringWidth(text)};
        text.visit((s, c) -> {
            mc.font.drawShadow(pose, s, curX[0], y, c, shadow);
            curX[0] += mc.font.width(s);
        });
    }

    @Override
    public void coloredStringTrimmed(ColoredString text, int x, int y, int width) {
        coloredStringTrimmed(text, x, y, width, false);
    }

    @Override
    public void coloredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {
        coloredString(trimToWidth(text, width), x, y, shadow);
    }

    @Override
    public void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width) {
        centeredColoredStringTrimmed(text, x, y, width, false);
    }

    @Override
    public void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {
        centeredColoredString(trimToWidth(text, width), x, y, shadow);
    }

    @Override
    public void rightColoredStringTrimmed(ColoredString text, int x, int y, int width) {
        rightColoredStringTrimmed(text, x, y, width, false);
    }

    @Override
    public void rightColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {
        rightColoredString(trimToWidth(text, width), x, y, shadow);
    }

    public String trimToWidth(String text, int width) {
        return mc.font.plainSubstrByWidth(text, width);
    }

    @Override
    public ColoredString trimToWidth(ColoredString text, int width) {
        ColoredString trimmed = new ColoredString();
        int[] curWidth = {0};
        boolean[] done = {false};
        text.visit((s, c) -> {
            if (done[0]) return;
            if (curWidth[0] + mc.font.width(s) <= width) {
                trimmed.append(s, c);
                curWidth[0] += mc.font.width(s);
            } else {
                String trim = mc.font.plainSubstrByWidth(s, width - curWidth[0]);
                if (trim.length() > 0) {
                    trimmed.append(trim, c);
                    curWidth[0] += mc.font.width(trim);
                    done[0] = true;
                }
            }
        });
        return trimmed;
    }

    @Override
    public void line(int x1, int y1, int x2, int y2, int color) {
        pose.pushPose();
        pose.translate(x1, y1, 0);
        // rotate so that the line is horizontal
        pose.mulPose(new Quaternionf().rotateLocalZ((float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1))));
        // calculate the length of the line
        int length = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        // stretch pose to the length of the line
        pose.scale(length, 1, 1);
        // draw the line
        fill(pose, 0, 0, 1, 1, color);
        pose.popPose();
    }

    @Override
    public void gradientRect(int x, int y, int width, int height, int color1, int color2) {
        fillGradient(pose, x, y, x + width, y + height, color1, color2);
    }

    @Override
    public MCTexture<AbstractTexture> getTexture(String identifier) {
        ResourceLocation l = new ResourceLocation(identifier);
        AbstractTexture t = mc.getTextureManager().getTexture(l);
        return new MCTexture<>(t, l);
    }

    @Override
    public MCMutableTexture createMutableTexture(String identifier, int width, int height) {
        NativeImage i = new NativeImage(width, height, true);
        DynamicTexture t = new DynamicTexture(i);
        mc.getTextureManager().register(identifier, t);
        return new MCMutableTexture(t, new ResourceLocation(identifier));
    }

    @Override
    public MCMutableTexture createMutableTexture(String identifier, Texture image) {
        if (image instanceof MCMutableTexture) {
            ResourceLocation l = new ResourceLocation(identifier);
            NativeImage i = new NativeImage(((MCMutableTexture) image).getWidth(), ((MCMutableTexture) image).getHeight(), false);
            i.copyFrom(((MCMutableTexture) image).getImage().getPixels());
            DynamicTexture t = new DynamicTexture(i);
            mc.getTextureManager().register(identifier, t);
            return new MCMutableTexture(t, l);
        }
        throw new UnsupportedOperationException("Cannot create mutable texture from immutable texture yet!");
    }

    @Override
    public void openScreen(Screen screen) {
        mc.setScreen(new MCScreenWrapper(screen, mc.screen));
    }

    @Override
    public void closeScreen() {
        if (mc.screen != null) {
            mc.screen.onClose();
        }
    }

    @Override
    public Screen getCurrentScreen() {
        if (mc.screen instanceof MCScreenWrapper) {
            return ((MCScreenWrapper) mc.screen).getScreen();
        }
        return null;
    }
}
