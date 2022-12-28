package xyz.wagyourtail.wagyourgui.standalone.glfw.image;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class DynamicTexture implements BaseTex {
    @Nullable
    private NativeImage pixels;

    private int id;

    public DynamicTexture(NativeImage pixels) {
        this.pixels = pixels;
        id = generateTextureId();
        prepareImage(NativeImage.InternalGlFormat.RGBA, id, 0, this.pixels.getWidth(), this.pixels.getHeight());
        this.upload();
    }

    public DynamicTexture(int width, int height, boolean useCalloc) {
        this.pixels = new NativeImage(width, height, useCalloc);
        id = generateTextureId();
        prepareImage(NativeImage.InternalGlFormat.RGBA, id, 0, this.pixels.getWidth(), this.pixels.getHeight());
    }

    public static void prepareImage(NativeImage.InternalGlFormat pixelFormat, int textureId, int mipmapLevel, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        if (mipmapLevel >= 0) {
            GL11.glTexParameteri(3553, 33085, mipmapLevel);
            GL11.glTexParameteri(3553, 33082, 0);
            GL11.glTexParameteri(3553, 33083, mipmapLevel);
            GL11.glTexParameterf(3553, 34049, 0.0F);
        }

        for (int $$5 = 0; $$5 <= mipmapLevel; ++$$5) {
            GL11.glTexImage2D(3553, $$5, pixelFormat.glFormat(), width >> $$5, height >> $$5, 0, 6408, 5121, (ByteBuffer) null);
        }
    }

    public static int generateTextureId() {
        return GL11.glGenTextures();
    }

    public void upload() {
        if (this.pixels != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            this.pixels.upload(0, 0, 0, false);
        }
    }

    public void bind() {
        if (this.id == -1) {
            prepareImage(NativeImage.InternalGlFormat.RGBA, id, 0, this.pixels.getWidth(), this.pixels.getHeight());
            this.upload();
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    @Nullable
    public NativeImage getPixels() {
        return this.pixels;
    }

    public void setPixels(NativeImage pixels) {
        if (this.pixels != null) {
            this.pixels.close();
        }

        this.pixels = pixels;
    }

    @Override
    public void close() {
        if (this.pixels != null) {
            this.pixels.close();
            this.releaseId();
            this.pixels = null;
        }
    }

    public void releaseId() {
        if (this.id != -1) {
            GL11.glDeleteTextures(this.id);
            this.id = -1;
        }
    }
}
