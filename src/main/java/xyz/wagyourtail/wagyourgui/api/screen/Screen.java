package xyz.wagyourtail.wagyourgui.api.screen;

import org.jetbrains.annotations.ApiStatus;
import xyz.wagyourtail.wagyourgui.api.container.AbstractLayeredElementContainer;
import xyz.wagyourtail.wagyourgui.api.element.*;
import xyz.wagyourtail.wagyourgui.api.keys.Keyboard;
import xyz.wagyourtail.wagyourgui.api.theme.Theme;

import java.util.Arrays;

public class Screen extends AbstractLayeredElementContainer implements Interactable, Ticking, Renderable, Themeable<Theme.ScreenTheme> {
    private Object parent;
    private Runnable openParent;

    protected int width;
    protected int height;

    private Theme.ScreenTheme theme = Theme.currentTheme.screen[0];
    private boolean transparentBg;

    public Screen(Screen parent) {
        setParent(parent);
        setOpenParent(() -> RENDERER.openScreen((Screen) this.parent));
    }

    /**
     * make sure to use a host parent screen here... ie. a minecraft screen.
     * @param parent
     */
    public Screen(Object parent) {
        setParent(parent);
    }

    @ApiStatus.Internal
    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Screen getParentAsGuest() {
        if (parent instanceof Screen) {
            return (Screen) parent;
        } else {
            return null;
        }
    }

    @ApiStatus.Internal
    public Object getParentHost() {
        return parent;
    }

    public boolean isParentGuest() {
        return parent instanceof Screen;
    }

    public void onInit(int width, int height, boolean transparentBg) {
        this.width = width;
        this.height = height;
        this.transparentBg = transparentBg;
        elements = new ElementLayer[1];
        focusedElement = null;
        currentLayer = 0;
    }

    public void close() {
        openParent.run();
    }

    @ApiStatus.Internal
    public void setOpenParent(Runnable openParent) {
        this.openParent = openParent;
    }

    public Interactable getFocusedElement() {
        return focusedElement;
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        // draw background
        if (getTheme().bgTexture == null) {
            int bgColor = getTheme().bgColor;
            if (!transparentBg) {
                bgColor = bgColor | 0xFF000000;
            }
            Renderable.RENDERER.rect(0, 0, width, height, bgColor);
        } else {
            Renderable.RENDERER.texturedRect(0, 0, width, height, 0, 0, 16, 16, theme.bgTexture);
        }
        // draw elements
        super.onRender(mouseX, mouseY);
    }

    @Override
    public void setVisible(boolean visible) {
        // NO-OP
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setThemeIndex(int index) {
        theme = Theme.currentTheme.screen[index];
    }

    @Override
    public int getThemeIndex() {
        return Arrays.asList(Theme.currentTheme.screen).indexOf(theme);
    }

    @Override
    public Theme.ScreenTheme getTheme() {
        return theme;
    }

    @Override
    public void setEnabled(boolean enabled) {
        //NO-OP
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        if (Keyboard.getKey(keyCode) == Keyboard.ESCAPE) {
            close();
            return true;
        }
        return super.onKeyPressed(keyCode, scanCode, modifiers);
    }

    public boolean closeOnESC() {
        return true;
    }
}
