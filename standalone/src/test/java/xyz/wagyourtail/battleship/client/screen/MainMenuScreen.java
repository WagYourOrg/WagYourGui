package xyz.wagyourtail.battleship.client.screen;

import xyz.wagyourtail.battleship.client.GameStateMachine;
import xyz.wagyourtail.wagyourgui.api.element.Button;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;

public class MainMenuScreen extends Screen {

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
            RENDERER.openScreen(new MultiplayerScreen(this, new GameStateMachine()));
        }));
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        super.onRender(mouseX, mouseY);
        RENDERER.centeredString("Battleship", width / 2, height / 2 - 100, 0xFFFFFFFF);
    }
}
