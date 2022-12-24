package xyz.wagyourtail.wagyourgui.api.theme;

import xyz.wagyourtail.wagyourgui.api.render.Texture;

public class Theme {
    public static final Theme DEFAULT = new Theme();

    public static Theme currentTheme = DEFAULT;

    public ButtonTheme[] button = { ButtonTheme.DEFAULT };

    public ScrollbarTheme[] scrollbar = { ScrollbarTheme.DEFAULT };

    public ScreenTheme[] screen = { ScreenTheme.DEFAULT };

    public static class ButtonTheme {
        public static final ButtonTheme DEFAULT = new ButtonTheme();

        public int textColor = 0xFFFFFFFF;
        public int bgColor = 0xFF3F3F3F;
        public int hoverBgColor = 0xFFAAAAAA;
        public int disabledBgColor = 0xFFAA3F3F;
        public int borderColor = 0xFF000000;
        public int borderWidth = 1;

        public Texture bgTexture = null;
    }

    public static class ScrollbarTheme {
        public static final ScrollbarTheme DEFAULT = new ScrollbarTheme();

        public int sbColor = 0xFFFFFFFF;
        public int bgColor = 0xFF000000;
        public int hoverBgColor = 0xFFAAAAAA;
        public int disabledBgColor = 0xFF555555;
        public int borderColor = 0xFF000000;
        public int borderWidth = 1;

        public Texture texture = null;
    }

    public static class ScreenTheme {
        public static final ScreenTheme DEFAULT = new ScreenTheme();

        public int bgColor = 0x4F2b2b2b;

        public Texture bgTexture = null;
    }
}
