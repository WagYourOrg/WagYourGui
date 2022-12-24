package xyz.wagyourtail.wagyourgui.standalone.glfw.image;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

public final class NativeImage implements AutoCloseable {
    private static final int OFFSET_A = 24;
    private static final int OFFSET_B = 16;
    private static final int OFFSET_G = 8;
    private static final int OFFSET_R = 0;
    private final NativeImage.Format format;
    private final int width;
    private final int height;
    private final boolean useStbFree;
    private long pixels;
    private final long size;

    private int id = -1;
    public NativeImage(int width, int height, boolean useCalloc) {
        this(NativeImage.Format.RGBA, width, height, useCalloc);
    }

    public NativeImage(NativeImage.Format format, int width, int height, boolean useCalloc) {
        if (width > 0 && height > 0) {
            this.format = format;
            this.width = width;
            this.height = height;
            this.size = (long) width * (long) height * (long) format.components();
            this.useStbFree = false;
            if (useCalloc) {
                this.pixels = MemoryUtil.nmemCalloc(1L, this.size);
            } else {
                this.pixels = MemoryUtil.nmemAlloc(this.size);
            }
        } else {
            throw new IllegalArgumentException("Invalid texture size: " + width + "x" + height);
        }
    }

    private NativeImage(NativeImage.Format format, int width, int height, boolean useStbFree, long pixels) {
        if (width > 0 && height > 0) {
            this.format = format;
            this.width = width;
            this.height = height;
            this.useStbFree = useStbFree;
            this.pixels = pixels;
            this.size = (long) width * (long) height * (long) format.components();
        } else {
            throw new IllegalArgumentException("Invalid texture size: " + width + "x" + height);
        }
    }

    public String toString() {
        return "NativeImage[" + this.format + " " + this.width + "x" + this.height + "@" + this.pixels + (this.useStbFree ? "S" : "N") + "]";
    }

    private boolean isOutsideBounds(int x, int y) {
        return x < 0 || x >= this.width || y < 0 || y >= this.height;
    }

    public static NativeImage read(ByteBuffer textureData) throws IOException {
        return read(NativeImage.Format.RGBA, textureData);
    }

    public static NativeImage read(@Nullable NativeImage.Format format, ByteBuffer textureData) throws IOException {
        if (format != null && !format.supportedByStb()) {
            throw new UnsupportedOperationException("Don't know how to read format " + format);
        } else if (MemoryUtil.memAddress(textureData) == 0L) {
            throw new IllegalArgumentException("Invalid buffer");
        } else {
            MemoryStack $$2 = MemoryStack.stackPush();

            NativeImage var7;
            try {
                IntBuffer $$3 = $$2.mallocInt(1);
                IntBuffer $$4 = $$2.mallocInt(1);
                IntBuffer $$5 = $$2.mallocInt(1);
                ByteBuffer $$6 = STBImage.stbi_load_from_memory(textureData, $$3, $$4, $$5, format == null ? 0 : format.components);
                if ($$6 == null) {
                    throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
                }

                var7 = new NativeImage(
                        format == null ? NativeImage.Format.getStbFormat($$5.get(0)) : format, $$3.get(0), $$4.get(0), true, MemoryUtil.memAddress($$6)
                );
            } catch (Throwable var9) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if ($$2 != null) {
                $$2.close();
            }

            return var7;
        }
    }

    private void checkAllocated() {
        if (this.pixels == 0L) {
            throw new IllegalStateException("Image is not allocated.");
        }
    }

    public void close() {
        if (this.pixels != 0L) {
            if (this.useStbFree) {
                STBImage.nstbi_image_free(this.pixels);
            } else {
                MemoryUtil.nmemFree(this.pixels);
            }
        }

        this.pixels = 0L;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public NativeImage.Format format() {
        return this.format;
    }

    public int getPixelRGBA(int x, int y) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixelRGBA only works on RGBA images; have %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            this.checkAllocated();
            long $$2 = ((long) x + (long) y * (long) this.width) * 4L;
            return MemoryUtil.memGetInt(this.pixels + $$2);
        }
    }

    public void setPixelRGBA(int x, int y, int abgrColor) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "getPixelRGBA only works on RGBA images; have %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            this.checkAllocated();
            long $$3 = ((long) x + (long) y * (long) this.width) * 4L;
            MemoryUtil.memPutInt(this.pixels + $$3, abgrColor);
        }
    }

    public void setPixelLuminance(int x, int y, byte luminance) {
        if (!this.format.hasLuminance()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "setPixelLuminance only works on image with luminance; have %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            this.checkAllocated();
            long $$3 = ((long) x + (long) y * (long) this.width) * (long) this.format.components() + (long) (this.format.luminanceOffset() / 8);
            MemoryUtil.memPutByte(this.pixels + $$3, luminance);
        }
    }

    public byte getRedOrLuminance(int x, int y) {
        if (!this.format.hasLuminanceOrRed()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no red or luminance in %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            int $$2 = (x + y * this.width) * this.format.components() + this.format.luminanceOrRedOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public byte getGreenOrLuminance(int x, int y) {
        if (!this.format.hasLuminanceOrGreen()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no green or luminance in %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            int $$2 = (x + y * this.width) * this.format.components() + this.format.luminanceOrGreenOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public byte getBlueOrLuminance(int x, int y) {
        if (!this.format.hasLuminanceOrBlue()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no blue or luminance in %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            int $$2 = (x + y * this.width) * this.format.components() + this.format.luminanceOrBlueOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public byte getLuminanceOrAlpha(int x, int y) {
        if (!this.format.hasLuminanceOrAlpha()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "no luminance or alpha in %s", this.format));
        } else if (this.isOutsideBounds(x, y)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "(%s, %s) outside of image bounds (%s, %s)", x, y, this.width, this.height));
        } else {
            int $$2 = (x + y * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
            return MemoryUtil.memGetByte(this.pixels + (long) $$2);
        }
    }

    public void blendPixel(int x, int y, int abgrColor) {
        if (this.format != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("Can only call blendPixel with RGBA format");
        } else {
            int $$3 = this.getPixelRGBA(x, y);
            float $$4 = (float) getA(abgrColor) / 255.0F;
            float $$5 = (float) getB(abgrColor) / 255.0F;
            float $$6 = (float) getG(abgrColor) / 255.0F;
            float $$7 = (float) getR(abgrColor) / 255.0F;
            float $$8 = (float) getA($$3) / 255.0F;
            float $$9 = (float) getB($$3) / 255.0F;
            float $$10 = (float) getG($$3) / 255.0F;
            float $$11 = (float) getR($$3) / 255.0F;
            float $$13 = 1.0F - $$4;
            float $$14 = $$4 * $$4 + $$8 * $$13;
            float $$15 = $$5 * $$4 + $$9 * $$13;
            float $$16 = $$6 * $$4 + $$10 * $$13;
            float $$17 = $$7 * $$4 + $$11 * $$13;
            if ($$14 > 1.0F) {
                $$14 = 1.0F;
            }

            if ($$15 > 1.0F) {
                $$15 = 1.0F;
            }

            if ($$16 > 1.0F) {
                $$16 = 1.0F;
            }

            if ($$17 > 1.0F) {
                $$17 = 1.0F;
            }

            int $$18 = (int) ($$14 * 255.0F);
            int $$19 = (int) ($$15 * 255.0F);
            int $$20 = (int) ($$16 * 255.0F);
            int $$21 = (int) ($$17 * 255.0F);
            this.setPixelRGBA(x, y, combine($$18, $$19, $$20, $$21));
        }
    }

    @Deprecated
    public int[] makePixelArray() {
        if (this.format != NativeImage.Format.RGBA) {
            throw new UnsupportedOperationException("can only call makePixelArray for RGBA images.");
        } else {
            this.checkAllocated();
            int[] $$0 = new int[this.getWidth() * this.getHeight()];

            for (int $$1 = 0; $$1 < this.getHeight(); ++$$1) {
                for (int $$2 = 0; $$2 < this.getWidth(); ++$$2) {
                    int $$3 = this.getPixelRGBA($$2, $$1);
                    int $$4 = getA($$3);
                    int $$5 = getB($$3);
                    int $$6 = getG($$3);
                    int $$7 = getR($$3);
                    int $$8 = $$4 << 24 | $$7 << 16 | $$6 << 8 | $$5;
                    $$0[$$2 + $$1 * this.getWidth()] = $$8;
                }
            }

            return $$0;
        }
    }

    public void upload(int level, int xOffset, int yOffset, boolean mipmap) {
        this.upload(level, xOffset, yOffset, 0, 0, this.width, this.height, mipmap, false);
    }

    public void upload(int level, int xOffset, int yOffset, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean mipmap, boolean autoClose) {
        this.upload(level, xOffset, yOffset, unpackSkipPixels, unpackSkipRows, width, height, false, false, mipmap, autoClose);
    }

    public void upload(
            int level,
            int xOffset,
            int yOffset,
            int unpackSkipPixels,
            int unpackSkipRows,
            int width,
            int height,
            boolean blur,
            boolean clamp,
            boolean mipmap,
            boolean autoClose
    ) {
        this._upload(level, xOffset, yOffset, unpackSkipPixels, unpackSkipRows, width, height, blur, clamp, mipmap, autoClose);
    }

    private void _upload(
            int level,
            int xOffset,
            int yOffset,
            int unpackSkipPixels,
            int unpackSkipRows,
            int width,
            int height,
            boolean blur,
            boolean clamp,
            boolean mipmap,
            boolean autoClose
    ) {
        this.checkAllocated();
        if (width == this.getWidth()) {
            GL11.glPixelStorei(3314, 0);
        } else {
            GL11.glPixelStorei(3314, this.getWidth());
        }

        GL11.glPixelStorei(3316, unpackSkipPixels);
        GL11.glPixelStorei(3315, unpackSkipRows);
        this.format.setUnpackPixelStoreState();
        GL11.glTexSubImage2D(3553, level, xOffset, yOffset, width, height, this.format.glFormat(), 5121, this.pixels);
        if (clamp) {
            GL11.glTexParameteri(3553, 10242, 33071);
            GL11.glTexParameteri(3553, 10243, 33071);
        }

        if (autoClose) {
            this.close();
        }
    }

    public void downloadTexture(int level, boolean opaque) {
        this.checkAllocated();
        this.format.setPackPixelStoreState();
        GL11.glGetTexImage(3553, level, this.format.glFormat(), 5121, this.pixels);
        if (opaque && this.format.hasAlpha()) {
            for (int $$2 = 0; $$2 < this.getHeight(); ++$$2) {
                for (int $$3 = 0; $$3 < this.getWidth(); ++$$3) {
                    this.setPixelRGBA($$3, $$2, this.getPixelRGBA($$3, $$2) | 255 << this.format.alphaOffset());
                }
            }
        }
    }

    public void downloadDepthBuffer(float unused) {
        if (this.format.components() != 1) {
            throw new IllegalStateException("Depth buffer must be stored in NativeImage with 1 component.");
        } else {
            this.checkAllocated();
            this.format.setPackPixelStoreState();
            GL11.glReadPixels(0, 0, this.width, this.height, 6402, 5121, this.pixels);
        }
    }

    public void drawPixels() {
        this.format.setUnpackPixelStoreState();
        GL11.glDrawPixels(this.width, this.height, this.format.glFormat(), 5121, this.pixels);
    }

    public void copyFrom(NativeImage other) {
        if (other.format() != this.format) {
            throw new UnsupportedOperationException("Image formats don't match.");
        } else {
            int $$1 = this.format.components();
            this.checkAllocated();
            other.checkAllocated();
            if (this.width == other.width) {
                MemoryUtil.memCopy(other.pixels, this.pixels, Math.min(this.size, other.size));
            } else {
                int $$2 = Math.min(this.getWidth(), other.getWidth());
                int $$3 = Math.min(this.getHeight(), other.getHeight());

                for(int $$4 = 0; $$4 < $$3; ++$$4) {
                    int $$5 = $$4 * other.getWidth() * $$1;
                    int $$6 = $$4 * this.getWidth() * $$1;
                    MemoryUtil.memCopy(other.pixels + (long)$$5, this.pixels + (long)$$6, (long)$$2);
                }
            }
        }
    }

    public void copyFromFont(STBTTFontinfo info, int glyphIndex, int width, int height, float scaleX, float scaleY, float shiftX, float shiftY, int x, int y) {
        if (x < 0 || x + width > this.getWidth() || y < 0 || y + height > this.getHeight()) {
            throw new IllegalArgumentException(
                    String.format(Locale.ROOT, "Out of bounds: start: (%s, %s) (size: %sx%s); size: %sx%s", x, y, width, height, this.getWidth(), this.getHeight())
            );
        } else if (this.format.components() != 1) {
            throw new IllegalArgumentException("Can only write fonts into 1-component images.");
        } else {
            STBTruetype.nstbtt_MakeGlyphBitmapSubpixel(
                    info.address(), this.pixels + (long) x + (long) (y * this.getWidth()), width, height, this.getWidth(), scaleX, scaleY, shiftX, shiftY, glyphIndex
            );
        }
    }

    public void fillRect(int x, int y, int width, int height, int value) {
        for (int $$5 = y; $$5 < y + height; ++$$5) {
            for (int $$6 = x; $$6 < x + width; ++$$6) {
                this.setPixelRGBA($$6, $$5, value);
            }
        }
    }

    public void copyRect(int xFrom, int yFrom, int xToDelta, int yToDelta, int width, int height, boolean mirrorX, boolean mirrorY) {
        this.copyRect(this, xFrom, yFrom, xFrom + xToDelta, yFrom + yToDelta, width, height, mirrorX, mirrorY);
    }

    public void copyRect(NativeImage $$0, int $$1, int $$2, int $$3, int $$4, int $$5, int $$6, boolean $$7, boolean $$8) {
        for (int $$9 = 0; $$9 < $$6; ++$$9) {
            for (int $$10 = 0; $$10 < $$5; ++$$10) {
                int $$11 = $$7 ? $$5 - 1 - $$10 : $$10;
                int $$12 = $$8 ? $$6 - 1 - $$9 : $$9;
                int $$13 = this.getPixelRGBA($$1 + $$10, $$2 + $$9);
                $$0.setPixelRGBA($$3 + $$11, $$4 + $$12, $$13);
            }
        }
    }

    public void flipY() {
        this.checkAllocated();
        MemoryStack $$0 = MemoryStack.stackPush();

        try {
            int $$1 = this.format.components();
            int $$2 = this.getWidth() * $$1;
            long $$3 = $$0.nmalloc($$2);

            for (int $$4 = 0; $$4 < this.getHeight() / 2; ++$$4) {
                int $$5 = $$4 * this.getWidth() * $$1;
                int $$6 = (this.getHeight() - 1 - $$4) * this.getWidth() * $$1;
                MemoryUtil.memCopy(this.pixels + (long) $$5, $$3, (long) $$2);
                MemoryUtil.memCopy(this.pixels + (long) $$6, this.pixels + (long) $$5, (long) $$2);
                MemoryUtil.memCopy($$3, this.pixels + (long) $$6, (long) $$2);
            }
        } catch (Throwable var10) {
            if ($$0 != null) {
                try {
                    $$0.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }
            }

            throw var10;
        }

        if ($$0 != null) {
            $$0.close();
        }
    }


    public static NativeImage fromBase64(String string) throws IOException {
        byte[] $$1 = Base64.getDecoder().decode(string.replaceAll("\n", "").getBytes(StandardCharsets.UTF_8));
        MemoryStack $$2 = MemoryStack.stackPush();

        NativeImage var4;
        try {
            ByteBuffer $$3 = $$2.malloc($$1.length);
            $$3.put($$1);
            $$3.rewind();
            var4 = read($$3);
        } catch (Throwable var6) {
            if ($$2 != null) {
                try {
                    $$2.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }

            throw var6;
        }

        if ($$2 != null) {
            $$2.close();
        }

        return var4;
    }

    public static int getA(int abgrColor) {
        return abgrColor >> 24 & 0xFF;
    }

    public static int getR(int abgrColor) {
        return abgrColor >> 0 & 0xFF;
    }

    public static int getG(int abgrColor) {
        return abgrColor >> 8 & 0xFF;
    }

    public static int getB(int abgrColor) {
        return abgrColor >> 16 & 0xFF;
    }

    public static int combine(int alpha, int blue, int green, int red) {
        return (alpha & 0xFF) << 24 | (blue & 0xFF) << 16 | (green & 0xFF) << 8 | (red & 0xFF) << 0;
    }

    public static enum Format {
        RGBA(4, 6408, true, true, true, false, true, 0, 8, 16, 255, 24, true),
        RGB(3, 6407, true, true, true, false, false, 0, 8, 16, 255, 255, true),
        LUMINANCE_ALPHA(2, 33319, false, false, false, true, true, 255, 255, 255, 0, 8, true),
        LUMINANCE(1, 6403, false, false, false, true, false, 0, 0, 0, 0, 255, true);

        final int components;
        private final int glFormat;
        private final boolean hasRed;
        private final boolean hasGreen;
        private final boolean hasBlue;
        private final boolean hasLuminance;
        private final boolean hasAlpha;
        private final int redOffset;
        private final int greenOffset;
        private final int blueOffset;
        private final int luminanceOffset;
        private final int alphaOffset;
        private final boolean supportedByStb;

        private Format(
                int components,
                int glFormat,
                boolean hasRed,
                boolean hasGreen,
                boolean hasBlue,
                boolean hasLuminance,
                boolean hasAlpha,
                int redOffset,
                int greenOffset,
                int blueOffset,
                int luminanceOffset,
                int alphaOffset,
                boolean supportedByStb
        ) {
            this.components = components;
            this.glFormat = glFormat;
            this.hasRed = hasRed;
            this.hasGreen = hasGreen;
            this.hasBlue = hasBlue;
            this.hasLuminance = hasLuminance;
            this.hasAlpha = hasAlpha;
            this.redOffset = redOffset;
            this.greenOffset = greenOffset;
            this.blueOffset = blueOffset;
            this.luminanceOffset = luminanceOffset;
            this.alphaOffset = alphaOffset;
            this.supportedByStb = supportedByStb;
        }

        public int components() {
            return this.components;
        }

        public void setPackPixelStoreState() {
            GL11.glPixelStorei(3333, this.components());
        }

        public void setUnpackPixelStoreState() {
            GL11.glPixelStorei(3317, this.components());
        }

        public int glFormat() {
            return this.glFormat;
        }

        public boolean hasRed() {
            return this.hasRed;
        }

        public boolean hasGreen() {
            return this.hasGreen;
        }

        public boolean hasBlue() {
            return this.hasBlue;
        }

        public boolean hasLuminance() {
            return this.hasLuminance;
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }

        public int redOffset() {
            return this.redOffset;
        }

        public int greenOffset() {
            return this.greenOffset;
        }

        public int blueOffset() {
            return this.blueOffset;
        }

        public int luminanceOffset() {
            return this.luminanceOffset;
        }

        public int alphaOffset() {
            return this.alphaOffset;
        }

        public boolean hasLuminanceOrRed() {
            return this.hasLuminance || this.hasRed;
        }

        public boolean hasLuminanceOrGreen() {
            return this.hasLuminance || this.hasGreen;
        }

        public boolean hasLuminanceOrBlue() {
            return this.hasLuminance || this.hasBlue;
        }

        public boolean hasLuminanceOrAlpha() {
            return this.hasLuminance || this.hasAlpha;
        }

        public int luminanceOrRedOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.redOffset;
        }

        public int luminanceOrGreenOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.greenOffset;
        }

        public int luminanceOrBlueOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.blueOffset;
        }

        public int luminanceOrAlphaOffset() {
            return this.hasLuminance ? this.luminanceOffset : this.alphaOffset;
        }

        public boolean supportedByStb() {
            return this.supportedByStb;
        }

        static NativeImage.Format getStbFormat(int channels) {
            switch (channels) {
                case 1:
                    return LUMINANCE;
                case 2:
                    return LUMINANCE_ALPHA;
                case 3:
                    return RGB;
                case 4:
                default:
                    return RGBA;
            }
        }
    }

    public static enum InternalGlFormat {
        RGBA(6408),
        RGB(6407),
        RG(33319),
        RED(6403);

        private final int glFormat;

        private InternalGlFormat(int glFormat) {
            this.glFormat = glFormat;
        }

        public int glFormat() {
            return this.glFormat;
        }
    }
}
