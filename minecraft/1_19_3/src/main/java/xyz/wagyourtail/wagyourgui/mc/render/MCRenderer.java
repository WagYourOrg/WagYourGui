package xyz.wagyourtail.wagyourgui.mc.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.render.Renderer;
import xyz.wagyourtail.wagyourgui.api.render.Texture;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public class MCRenderer extends GuiComponent implements Renderer<MCTexture<AbstractTexture>, MCMutableTexture> {
    public static MCRenderer INSTANCE;
    Minecraft mc = Minecraft.getInstance();
    PoseStack pose;

    public MCRenderer() {
        INSTANCE = this;
    }

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
    public void texturedRect(int x, int y, int width, int height, int u, int v, int uw, int vh, int textureWidth, int textureHeight, Texture tex) {
        RenderSystem.setShaderTexture(0, ((MCTexture) tex).location);
        blit(pose, x, y, width, height, u, v, uw, vh, textureWidth, textureHeight);
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, Texture tex, int color) {
        RenderSystem.setShaderTexture(0, ((MCTexture) tex).location);

        Matrix4f matrix = pose.last().pose();
        int x1 = x;
        int y1 = y;
        int x2 = x + width;
        int y2 = y + height;

        float u1 = (float) u / (float) textureWidth;
        float v1 = (float) v / (float) textureHeight;
        float u2 = (float) (u + width) / (float) textureWidth;
        float v2 = (float) (v + height) / (float) textureHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder $$10 = Tesselator.getInstance().getBuilder();
        $$10.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        $$10.vertex(matrix, (float) x1, (float) y2, 0).color(color).uv(u1, v2).endVertex();
        $$10.vertex(matrix, (float) x2, (float) y2, 0).color(color).uv(u2, v2).endVertex();
        $$10.vertex(matrix, (float) x2, (float) y1, 0).color(color).uv(u2, v1).endVertex();
        $$10.vertex(matrix, (float) x1, (float) y1, 0).color(color).uv(u1, v1).endVertex();
        BufferUploader.drawWithShader($$10.end());
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int uw, int vh, int textureWidth, int textureHeight, Texture tex, int color) {
        RenderSystem.setShaderTexture(0, ((MCTexture) tex).location);

        Matrix4f matrix = pose.last().pose();

        int x1 = x;
        int y1 = y;
        int x2 = x + width;
        int y2 = y + height;

        float u1 = (float) u / (float) textureWidth;
        float v1 = (float) v / (float) textureHeight;
        float u2 = (float) (u + uw) / (float) textureWidth;
        float v2 = (float) (v + vh) / (float) textureHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder $$10 = Tesselator.getInstance().getBuilder();
        $$10.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        $$10.vertex(matrix, (float) x1, (float) y2, 0).color(color).uv(u1, v2).endVertex();
        $$10.vertex(matrix, (float) x2, (float) y2, 0).color(color).uv(u2, v2).endVertex();
        $$10.vertex(matrix, (float) x2, (float) y1, 0).color(color).uv(u2, v1).endVertex();
        $$10.vertex(matrix, (float) x1, (float) y1, 0).color(color).uv(u1, v1).endVertex();
        BufferUploader.drawWithShader($$10.end());
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
    public void stringTrimmed(String text, int x, int y, int width, int color) {
        stringTrimmed(text, x, y, width, color, false);
    }

    @Override
    public void stringTrimmed(String text, int x, int y, int width, int color, boolean shadow) {
        mc.font.drawShadow(pose, mc.font.plainSubstrByWidth(text, width), x, y, color, shadow);
    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int width, int color) {
        centeredStringTrimmed(text, x, y, width, color, false);
    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int width, int color, boolean shadow) {
        mc.font.drawShadow(pose, mc.font.plainSubstrByWidth(text, width), x - mc.font.width(mc.font.plainSubstrByWidth(text, width)) / 2f, y, color, shadow);
    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int width, int color) {
        rightStringTrimmed(text, x, y, width, color, false);
    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int width, int color, boolean shadow) {
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

    @Override
    public String trimToWidth(String text, int width) {
        return mc.font.plainSubstrByWidth(text, width);
    }

    @Override
    public void line(int x1, int y1, int x2, int y2, int color) {
    }

    @Override
    public void line(int x1, int y1, int x2, int y2, int color, int thickness) {
        pose.pushPose();
        pose.translate(x1, y1, 0);
        // rotate so that the line is horizontal
        pose.mulPose(new Quaternionf().rotateLocalZ((float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1))));
        // calculate the length of the line
        float length = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        // stretch pose to the length of the line
        pose.scale(length, 1, 1);
        // draw the line
        if (thickness < 2) {
            // draw a single line
            fill(pose, 0, 0, 1, 1, color);
        } else {
            // draw a rectangle
            fill(pose, 0, -thickness / 2, 1, thickness / 2, color);
        }
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
        mc.setScreen(new MCScreenWrapper(screen));
    }

    @Override
    public void closeScreen() {
        if (mc.screen != null) {
            mc.screen.onClose();
        }
    }

    @Override
    public Screen getCurrentGuestScreen() {
        if (mc.screen instanceof MCScreenWrapper) {
            return ((MCScreenWrapper) mc.screen).getScreen();
        }
        return null;
    }

    @Override
    public Object getCurrentHostScreen() {
        return mc.screen;
    }

    @Override
    public boolean isGuestScreen() {
        return mc.screen instanceof MCScreenWrapper;
    }
}
