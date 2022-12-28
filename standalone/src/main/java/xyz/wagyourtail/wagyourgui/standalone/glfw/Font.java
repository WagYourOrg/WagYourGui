package xyz.wagyourtail.wagyourgui.standalone.glfw;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memSlice;

public class Font {

    public final FontData data;

    public final ByteBuffer fontBuffer;
    public final STBTTFontinfo fontInfo;
    public final int FONT_HEIGHT = 9;
    private final float scale;
    private final int ascent;
    private final int descent;
    private final int lineGap;
    private STBTTBakedChar.Buffer cdata;

    public Font(ResourceLocation fontResource) throws IOException {

        data = FontData.fromJson(new JsonParser().parse(new InputStreamReader(fontResource.getResource())).getAsJsonObject());
        ResourceLocation loc = ResourceLocation.of(data.file);

        this.fontBuffer = inputStreamToByteBuffer(loc.getResource(), 512 * 1024);
        fontInfo = STBTTFontinfo.create();
        if (!stbtt_InitFont(fontInfo, fontBuffer)) {
            throw new IOException("Failed to load font: " + fontResource);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pAscent = stack.mallocInt(1);
            IntBuffer pDescent = stack.mallocInt(1);
            IntBuffer pLineGap = stack.mallocInt(1);

            stbtt_GetFontVMetrics(fontInfo, pAscent, pDescent, pLineGap);

            ascent = pAscent.get(0);
            descent = pDescent.get(0);
            lineGap = pLineGap.get(0);
        }

        scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);
    }

    private static int getCP(String text, int to, int i, IntBuffer cpOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                cpOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        cpOut.put(0, c1);
        return 1;
    }

    private static float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer inputStreamToByteBuffer(InputStream source, int bufferSize) throws IOException {
        ByteBuffer buffer;

        try (
                ReadableByteChannel rbc = Channels.newChannel(source)
        ) {
            buffer = createByteBuffer(bufferSize);

            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1) {
                    break;
                }
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                }
            }
        }

        buffer.flip();
        return memSlice(buffer);
    }

    private STBTTBakedChar.Buffer init(int BITMAP_W, int BITMAP_H) {
        int texID = glGenTextures();
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(96);

        ByteBuffer bitmap = createByteBuffer(BITMAP_W * BITMAP_H);
        stbtt_BakeFontBitmap(fontBuffer, 32, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f); // BG color
        glColor3f(169f / 255f, 183f / 255f, 198f / 255f); // Text color

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        return cdata;
    }

    public float drawString(String text, float x, float y) {
        // move y to top instead of bottom
        y += FONT_HEIGHT;

        if (cdata == null) {
            cdata = init(1024, 1024);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer xP = stack.floats(0f);
            FloatBuffer yP = stack.floats(0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            int lineStart = 0;
            float lineY = 0;

            glBegin(GL_QUADS);
            for (int i = 0, to = text.length(); i < to; ) {
                i += getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0);
                if (cp == '\n') {
                    throw new IllegalStateException("Newline not supported");
                }
                float cpX = xP.get(0);
                float cpY = yP.get(0);
                stbtt_GetBakedQuad(cdata, 1024, 1024, cp - 32, xP, yP, q, true);
                xP.put(0, scale(cpX, xP.get(0), FONT_HEIGHT / data.native_size));
                // kerning
                if (i < to) {
                    getCP(text, to, i, pCodePoint);
                    xP.put(0, xP.get(0) + stbtt_GetCodepointKernAdvance(fontInfo, cp, pCodePoint.get(0)) * FONT_HEIGHT / data.native_size);
                }
                float x0 = scale(cpX, q.x0(), FONT_HEIGHT / data.native_size);
                float x1 = scale(cpX, q.x1(), FONT_HEIGHT / data.native_size);
                float y0 = scale(cpY, q.y0(), FONT_HEIGHT / data.native_size);
                float y1 = scale(cpY, q.y1(), FONT_HEIGHT / data.native_size);

                glTexCoord2f(q.s0(), q.t0());
                glVertex2f(x0 + x, y0 + y);

                glTexCoord2f(q.s1(), q.t0());
                glVertex2f(x1 + x, y0 + y);

                glTexCoord2f(q.s1(), q.t1());
                glVertex2f(x1 + x, y1 + y);

                glTexCoord2f(q.s0(), q.t1());
                glVertex2f(x0 + x, y1 + y);
            }
            glEnd();
            return q.x1();
        }
    }

    public float getWidth(String text) {
        int width = 0;

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);
            IntBuffer pAdvancedWidth = stack.mallocInt(1);
            IntBuffer pLeftSideBearing = stack.mallocInt(1);

            int i = 0;
            while (i < text.length()) {
                i += getCP(text, text.length(), i, pCodePoint);
                int cp = pCodePoint.get(0);

                stbtt_GetCodepointHMetrics(fontInfo, cp, pAdvancedWidth, pLeftSideBearing);
                width += pAdvancedWidth.get(0) * scale;

                if (i < text.length()) {
                    getCP(text, text.length(), i, pCodePoint);
                    width += stbtt_GetCodepointKernAdvance(fontInfo, cp, pCodePoint.get(0)) * scale;
                }
            }
        }

        return width * data.width_scale_factor;
    }

    public float drawTrimmed(String text, float x, float y, float width) {
        // move y to top instead of bottom
        y += FONT_HEIGHT;

        if (cdata == null) {
            cdata = init(1024, 1024);
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer xP = stack.floats(0f);
            FloatBuffer yP = stack.floats(0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.malloc(stack);

            int lineStart = 0;
            float lineY = 0;

            glBegin(GL_QUADS);
            for (int i = 0, to = text.length(); i < to; ) {
                i += getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0);
                if (cp == '\n') {
                    throw new IllegalStateException("Newline not supported");
                }
                float cpX = xP.get(0);
                float cpY = yP.get(0);
                stbtt_GetBakedQuad(cdata, 1024, 1024, cp - 32, xP, yP, q, true);
                xP.put(0, scale(cpX, xP.get(0), FONT_HEIGHT / data.native_size));
                // kerning
                if (i < to) {
                    getCP(text, to, i, pCodePoint);
                    xP.put(0, xP.get(0) + stbtt_GetCodepointKernAdvance(fontInfo, cp, pCodePoint.get(0)) * FONT_HEIGHT / data.native_size);
                }
                float x0 = scale(cpX, q.x0(), FONT_HEIGHT / data.native_size);
                float x1 = scale(cpX, q.x1(), FONT_HEIGHT / data.native_size);
                float y0 = scale(cpY, q.y0(), FONT_HEIGHT / data.native_size);
                float y1 = scale(cpY, q.y1(), FONT_HEIGHT / data.native_size);

                if (q.x1() - x > width) {
                    glEnd();
                    return q.x0();
                }

                glTexCoord2f(q.s0(), q.t0());
                glVertex2f(x0 + x, y0 + y);

                glTexCoord2f(q.s1(), q.t0());
                glVertex2f(x1 + x, y0 + y);

                glTexCoord2f(q.s1(), q.t1());
                glVertex2f(x1 + x, y1 + y);

                glTexCoord2f(q.s0(), q.t1());
                glVertex2f(x0 + x, y1 + y);
            }
            glEnd();
            return q.x1();
        }
    }

    public String trimToWidth(String text, float width) {
        // get width of initial string
        float width0 = getWidth(text);
        if (width0 <= width) {
            return text;
        }
        // estimate based on ratio
        int estimate = (int) (width / width0 * text.length());
        // get width of estimated string
        String textEst = text.substring(0, estimate);
        float widthEst = getWidth(textEst);
        // test size
        if (widthEst > width) {
            // trim
            int i = estimate - 1;
            while (i > 0) {
                textEst = text.substring(0, i);
                widthEst = getWidth(textEst);
                if (widthEst <= width) {
                    break;
                }
                i--;
            }
        } else {
            // add to reach
            int i = estimate + 1;
            while (i < text.length()) {
                String newEst = text.substring(0, i);
                widthEst = getWidth(textEst);
                if (widthEst > width) {
                    break;
                }
                // use std::move to avoid copying
                textEst = newEst;
                i++;
            }
        }
        return textEst;
    }

    public void free() {
        if (cdata != null) {
            cdata.free();
        }
    }

    private record FontData(String file, float native_size, float width_scale_factor) {
        public static FontData fromJson(JsonObject json) {
            return new FontData(json.get("file").getAsString(), json.get("native_size").getAsFloat(), json.get("width_scale_factor").getAsFloat());
        }
    }
}
