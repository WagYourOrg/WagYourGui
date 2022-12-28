package xyz.wagyourtail.wagyourgui.standalone.glfw;

import static org.lwjgl.opengl.GL11.*;
import static xyz.wagyourtail.wagyourgui.standalone.glfw.GLBuilder.VertexFormat.*;

public abstract class GLBuilder {
    protected boolean state;

    public static GLBuilder getImmediate() {
        return ImmediateBuilder.getInstance();
    }

    public abstract GLBuilder begin(int mode, VertexFormat format);

    public GLBuilder begin(int mode) {
        begin(mode, POS_COL);
        return this;
    }

    public abstract GLBuilder vertex(float x, float y);

    public abstract GLBuilder vertex(float x, float y, float z);

    public abstract GLBuilder color(int r, int g, int b, int a);

    public abstract GLBuilder color(float r, float g, float b, float a);

    public abstract GLBuilder color(int rgb, float a);

    public abstract GLBuilder color(int rgba);

    public abstract GLBuilder uv(float u, float v);

    public abstract GLBuilder uv(float u, float v, float w, float h);

    public abstract GLBuilder next();

    public abstract void end();

    public enum VertexFormat {
        POS_COL,
        POS_COL_TEX,
        POS_TEX
    }

    private record Pos(float x, float y, float z) {
    }

    private record Col(float r, float g, float b, float a) {
    }

    private record Tex(float u, float v) {
    }

    public static class ImmediateBuilder extends GLBuilder {
        private static ImmediateBuilder instance = new ImmediateBuilder();
        private VertexFormat format;
        private Pos pos;
        private Col col;
        private Tex tex;

        private ImmediateBuilder() {
            super();
        }

        public static ImmediateBuilder getInstance() {
            return instance;
        }

        @Override
        public GLBuilder begin(int mode, VertexFormat format) {
            if (state) {
                throw new RuntimeException("already building");
            }
            if (format == POS_COL) {
                glEnable(GL_COLOR);
                glDisable(GL_TEXTURE_2D);
            } else if (format == POS_COL_TEX) {
                glEnable(GL_COLOR);
                glEnable(GL_TEXTURE_2D);
            } else if (format == POS_TEX) {
                glDisable(GL_COLOR);
                glColor4f(1, 1, 1, 1);
                glEnable(GL_TEXTURE_2D);
            }
            glBegin(mode);
            this.format = format;
            state = true;
            return this;
        }

        @Override
        public GLBuilder vertex(float x, float y) {
            return vertex(x, y, 0);
        }

        @Override
        public GLBuilder vertex(float x, float y, float z) {
            if (!state) {
                throw new RuntimeException("not building");
            }
            if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z)) {
                throw new RuntimeException("invalid position");
            }
            if (pos != null) {
                throw new RuntimeException("already set position");
            }
            pos = new Pos(x, y, z);
            return this;
        }

        @Override
        public GLBuilder color(int r, int g, int b, int a) {
            if (!state) {
                glColor4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
                return this;
            }
            if (col != null) {
                throw new RuntimeException("already set color");
            }
            if (format != POS_TEX) {
                col = new Col(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
            } else {
                throw new RuntimeException("current doesn't support color");
            }
            return this;
        }

        @Override
        public GLBuilder color(float r, float g, float b, float a) {
            if (!state) {
                glColor4f(r, g, b, a);
                return this;
            }
            if (col != null) {
                throw new RuntimeException("already set color");
            }
            if (format != POS_TEX) {
                col = new Col(r, g, b, a);
            } else {
                throw new RuntimeException("current doesn't support color");
            }
            return this;
        }

        @Override
        public GLBuilder color(int rgb, float a) {
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            color(r, g, b, (int) (a * 255));
            return this;
        }

        @Override
        public GLBuilder color(int argb) {
            int a = (argb >> 24) & 0xff;
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            color(r, g, b, a);
            return this;
        }

        @Override
        public GLBuilder uv(float u, float v) {
            if (!state) {
                throw new RuntimeException("not building");
            }
            if (format == POS_COL) {
                throw new RuntimeException("current doesn't support uv");
            }
            if (tex != null) {
                throw new RuntimeException("already set uv");
            }
            tex = new Tex(u, v);
            return this;
        }

        @Override
        public GLBuilder uv(float u, float v, float w, float h) {
            if (!state) {
                throw new RuntimeException("not building");
            }
            if (format == POS_COL) {
                throw new RuntimeException("current doesn't support uv");
            }
            if (tex != null) {
                throw new RuntimeException("already set uv");
            }
            tex = new Tex(u / w, v / h);
            return this;
        }

        @Override
        public GLBuilder next() {
            if (!state) {
                throw new RuntimeException("not building");
            }
            if (pos == null) {
                throw new RuntimeException("no pos");
            }
            if (format == POS_COL) {
                if (col != null) {
                    glColor4f(col.r(), col.g(), col.b(), col.a());
                }
                glVertex2f(pos.x(), pos.y());
            } else if (format == POS_COL_TEX) {
                if (col != null) {
                    glColor4f(col.r(), col.g(), col.b(), col.a());
                }
                glTexCoord2f(tex.u(), tex.v());
                glVertex2f(pos.x(), pos.y());
            } else if (format == POS_TEX) {
                glTexCoord2f(tex.u(), tex.v());
                glVertex2f(pos.x(), pos.y());
            }
            pos = null;
            col = null;
            tex = null;
            return this;
        }

        @Override
        public void end() {
            if (!state) {
                throw new RuntimeException("not building");
            }
            if (pos != null || col != null || tex != null) {
                next();
            }
            glEnd();
            state = false;
        }
    }
}