package xyz.wagyourtail.wagyourgui.api.overlay;

import xyz.wagyourtail.wagyourgui.api.element.Button;
import xyz.wagyourtail.wagyourgui.api.render.ColoredString;

import java.util.ArrayList;
import java.util.List;

public class WarningOverlay extends AbstractOverlayElementContainer {
    private final List<Object> messageLines;

    public WarningOverlay(int x, int y, int width, int height, ColoredString message) {
        super(x, y, width, height);
        messageLines = (List) RENDERER.wrapString(message, width - 5, true);
    }

    public WarningOverlay(int x, int y, int width, int height, String message) {
        super(x, y, width, height);
        messageLines = (List) RENDERER.wrapString(message, width - 5, true);
    }


    @Override
    public void onOpened() {

        addElement(new Button(x, y + height - 12, width, 12, "OK", (b) -> {
            shouldClose = true;
        }));

    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        super.onRender(mouseX, mouseY);

        int y = this.y + 5;
        for (Object line : messageLines) {
            if (line instanceof ColoredString) {
                RENDERER.centeredColoredString((ColoredString) line, x + width / 2, y);
            } else {
                RENDERER.centeredString(line.toString(), x + width / 2, y, getTheme().textColor);
            }
            y += RENDERER.getStringHeight() + 1;
        }

    }
}
