package xyz.wagyourtail.wagyourgui.standalone.glfw;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.system.MemoryStack;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;
import xyz.wagyourtail.wagyourgui.standalone.glfw.image.BaseTex;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClearColor;

public class GLFWSession implements ResizeListener, MouseListener, KeyListener {
    public Window window;
    public Font font;
    public long fps;
    public int GUI_SCALE = 2;
    public final Map<String, BaseTex> textures = new HashMap<>();

    private boolean running = false;

    private Screen screen = new Screen(null);

    public Screen getScreen() {
        return screen;
    }

    public boolean isRunning() {
        return running;
    }

    public void start(Screen first) throws IOException {
        this.screen = first;
        running = true;
        init();
        loop();
        running = false;

        window.shutdown();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() throws IOException {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        window = new Window("Standalone Renderer", 800, 600);
        window.addResizeListener(this);
        window.addKeyListener(this);
        window.addMouseListener(this);

        glfwMakeContextCurrent(window.handle);
        GL.createCapabilities();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window.handle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                window.handle,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        }


        // make the OpenGL context current
        glfwMakeContextCurrent(window.handle);

        // Enable v-sync
        glfwSwapInterval(1);

        font = new Font("UbuntuMono-R.ttf");
        window.setVisible(true);
    }

    public void setScreen(Screen screen) {
        if (screen == null) this.screen = new Screen(null) {
            @Override
            public void onRender(int mouseX, int mouseY) {
                RENDERER.centeredString("No Screen", width / 2, height / 2 - RENDERER.getStringHeight() / 2, 0xFFFFFFFF);
            }
        };
        else this.screen = screen;
        this.screen.onInit(window.getWidth() / GUI_SCALE, window.getHeight() / GUI_SCALE, false);
    }

    public void loop() {
        GL.createCapabilities();

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        onWindowResize(window);

        window.setupFramebuffer();
        long timeNanos = System.nanoTime();
        int frameCount = 0;

        while (!glfwWindowShouldClose(window.handle)) {
            ++frameCount;
            if (frameCount % 10 == 0) {
                fps = frameCount * 1000000000L / (System.nanoTime() - timeNanos);
                timeNanos = System.nanoTime();
                frameCount = 0;
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            GL14.glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

            glPushMatrix();

            GL11.glDisable(GL_TEXTURE_2D);

            glTranslatef(-1, 1, 0f);
            glScalef(2f / window.getWidth() * GUI_SCALE, -2f / window.getHeight() * GUI_SCALE, 1f);

//            glEnable(GL_BLEND);
//            glEnable(GL_POLYGON_SMOOTH);
//            glEnable(GL_LINE_SMOOTH);
//            glEnable(GL_POINT_SMOOTH);
//            glDisable(GL_TEXTURE_2D);
//            glColor3f(1f, 0f, 0f);
//            GL11.glBegin(GL_QUADS);
//            GL11.glVertex2f(0, 0);
//            GL11.glVertex2f(0, 100);
//            GL11.glVertex2f(100, 100);
//            GL11.glVertex2f(100, 0);
//            GL11.glEnd();
//
//            glColor3f(1f, 1f, 0f);
//            glEnable(GL_TEXTURE_2D);
//            f.drawString("Hello, world!", 0, 25);

            double[] cursorX = new double[1];
            double[] cursorY = new double[1];
            glfwGetCursorPos(window.handle, cursorX, cursorY);
            cursorX[0] /= GUI_SCALE;
            cursorY[0] /= GUI_SCALE;

            screen.onRender((int) cursorX[0], (int) cursorY[0]);

            glPopMatrix();

            glfwSwapBuffers(window.handle);
            glfwPollEvents();
        }
    }

    @Override
    public void onWindowResize(Window window) {
        screen.onInit(window.getWidth() / GUI_SCALE, window.getHeight() / GUI_SCALE, false);
    }

    @Override
    public void onKey(int key, int scancode, int action, int mods) {
        switch (action) {
            case 1:
                screen.onKeyPressed(key, scancode, mods);
                break;
            case 0:
                screen.onKeyReleased(key, scancode, mods);
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onChar(int codepoint, int mods) {
        screen.onCharTyped((char) codepoint, mods);
    }

    @Override
    public void onMouseButton(int button, int action, int mods) {
        double[] cursorX = new double[1];
        double[] cursorY = new double[1];
        glfwGetCursorPos(window.handle, cursorX, cursorY);
        cursorX[0] /= GUI_SCALE;
        cursorY[0] /= GUI_SCALE;
        switch (action) {
            case 1:
                screen.onClicked((int) cursorX[0], (int) cursorY[0], button, mods);
                break;
            case 0:
                screen.onReleased((int) cursorX[0], (int) cursorY[0], button, mods);
                break;
            case 2:
                break;
        }
    }

    private final double[] sX = new double[6];
    private final double[] sY = new double[6];

    @Override
    public void onMousePos(double x, double y) {
        x /= GUI_SCALE;
        y /= GUI_SCALE;
        for (int i = 0; i < 6; ++i) {
            if (glfwGetMouseButton(window.handle, i) == GLFW_PRESS) {
                sX[i] = x * GUI_SCALE;
                sY[i] = y * GUI_SCALE;
                screen.onDragged((int) x, (int) y, i, x - sX[i], y - sY[i]);
            }
        }
    }

    @Override
    public void onScroll(double dx, double dy) {
        double[] cursorX = new double[1];
        double[] cursorY = new double[1];
        glfwGetCursorPos(window.handle, cursorX, cursorY);
        cursorX[0] /= GUI_SCALE;
        cursorY[0] /= GUI_SCALE;
        screen.onScrolled((int) cursorX[0], (int) cursorY[0], dy);
    }
}
