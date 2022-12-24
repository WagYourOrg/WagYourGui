package xyz.wagyourtail.wagyourgui.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;
import xyz.wagyourtail.wagyourgui.api.render.Renderer;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public class MCRenderer implements Renderer<MCTexture, MCMutableTexture> {
    Minecraft mc = Minecraft.getInstance();
    PoseStack pose;

    public void bindMatrixStack(PoseStack pose) {
        this.pose = pose;
    }

    @Override
    public void rect(int x, int y, int width, int height, int color) {
        GuiComponent.fill(pose, x, y, x + width, y + height, color);
    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, MCTexture tex) {

    }

    @Override
    public void texturedRect(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, MCTexture tex, int color) {

    }

    @Override
    public void string(String text, int x, int y, int color) {

    }

    @Override
    public void string(String text, int x, int y, int color, boolean shadow) {

    }

    @Override
    public void centeredString(String text, int x, int y, int color) {

    }

    @Override
    public void centeredString(String text, int x, int y, int color, boolean shadow) {

    }

    @Override
    public void rightString(String text, int x, int y, int color) {

    }

    @Override
    public void rightString(String text, int x, int y, int color, boolean shadow) {

    }

    @Override
    public int getStringWidth(String text) {
        return 0;
    }

    @Override
    public int getStringHeight() {
        return 0;
    }

    @Override
    public void stringTrimmed(String text, int x, int y, int color, int width) {

    }

    @Override
    public void stringTrimmed(String text, int x, int y, int color, int width, boolean shadow) {

    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int color, int width) {

    }

    @Override
    public void centeredStringTrimmed(String text, int x, int y, int color, int width, boolean shadow) {

    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int color, int width) {

    }

    @Override
    public void rightStringTrimmed(String text, int x, int y, int color, int width, boolean shadow) {

    }

    @Override
    public void coloredString(ColoredString text, int x, int y) {

    }

    @Override
    public void coloredString(ColoredString text, int x, int y, boolean shadow) {

    }

    @Override
    public void centeredColoredString(ColoredString text, int x, int y) {

    }

    @Override
    public void centeredColoredString(ColoredString text, int x, int y, boolean shadow) {

    }

    @Override
    public void rightColoredString(ColoredString text, int x, int y) {

    }

    @Override
    public void rightColoredString(ColoredString text, int x, int y, boolean shadow) {

    }

    @Override
    public void coloredStringTrimmed(ColoredString text, int x, int y, int width) {

    }

    @Override
    public void coloredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {

    }

    @Override
    public void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width) {

    }

    @Override
    public void centeredColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {

    }

    @Override
    public void rightColoredStringTrimmed(ColoredString text, int x, int y, int width) {

    }

    @Override
    public void rightColoredStringTrimmed(ColoredString text, int x, int y, int width, boolean shadow) {

    }

    @Override
    public void line(int x1, int y1, int x2, int y2, int color) {

    }

    @Override
    public void gradientRect(int x, int y, int width, int height, int color1, int color2) {

    }

    @Override
    public void triangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {

    }

    @Override
    public void texturedTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int u1, int v1, int u2, int v2, int u3, int v3, int textureWidth, int textureHeight, MCTexture tex) {

    }

    @Override
    public void circle(int x, int y, int radius, int color) {

    }

    @Override
    public void texturedCircle(int x, int y, int radius, int u, int v, int tRadius, int textureWidth, int textureHeight, MCTexture tex) {

    }

    @Override
    public MCTexture getTexture(String identifier) {
        ResourceLocation l = new ResourceLocation(identifier);
        AbstractTexture t = mc.getTextureManager().getTexture(l);
        return new MCTexture(t, l);
    }

    @Override
    public MCMutableTexture createMutableTexture(String identifier, int width, int height) {
        NativeImage i = new NativeImage(width, height, true);
        DynamicTexture t = new DynamicTexture(i);
        mc.getTextureManager().register(identifier, t);
        return new MCMutableTexture(t, new ResourceLocation(identifier));
    }

    @Override
    public MCMutableTexture createMutableTexture(String identifier, MCTexture image) {
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

    }

    @Override
    public void closeScreen() {

    }

    @Override
    public Screen getCurrentScreen() {
        return null;
    }
}
