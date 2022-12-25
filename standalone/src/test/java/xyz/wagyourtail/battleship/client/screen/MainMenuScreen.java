package xyz.wagyourtail.battleship.client.screen;

import xyz.wagyourtail.battleship.client.GameStateMachine;
import xyz.wagyourtail.wagyourgui.api.element.Button;
import xyz.wagyourtail.wagyourgui.api.overlay.WarningOverlay;
import xyz.wagyourtail.wagyourgui.api.screen.ScreenWithOverlays;

public class MainMenuScreen extends ScreenWithOverlays {

    public MainMenuScreen() {
        super(null);
    }

    @Override
    public void onInit(int width, int height, boolean transparentBg) {
        super.onInit(width, height, transparentBg);
        addElement(new Button(width / 2 - 400, height / 2 - 14, 800, 28, "SinglePlayer", (btn) -> {
            RENDERER.openScreen(new PlaceShipScreen(this, new GameStateMachine()));
        }));
        addElement(new Button(width / 2 - 400, height / 2 + 16, 800, 28, "MultiPlayer", (btn) -> {
            openOverlay(new WarningOverlay(width / 2 - 400, height / 2 - 14, 800, 100, "Multiplayer is not yet implemented."));
        }));
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        super.onRender(mouseX, mouseY);
        double w = RENDERER.getStringWidth("Lots Of Text In ORder To Test Text Rendering");
        RENDERER.rect((int) (width / 2 - w / 2 - 2), height / 2 - 102, (int) (w + 4), RENDERER.getStringHeight() + 4, 0xFF151515);
        RENDERER.centeredString("Lots Of Text In ORder To Test Text Rendering", width / 2, height / 2 - 100, 0xFFFFFFFF);

    }
}
