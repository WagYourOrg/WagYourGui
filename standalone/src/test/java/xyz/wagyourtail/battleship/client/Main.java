package xyz.wagyourtail.battleship.client;

import xyz.wagyourtail.battleship.client.screen.MainMenuScreen;
import xyz.wagyourtail.wagyourgui.api.element.Renderable;

public class Main {
    public static void main(String[] args) {
        Renderable.RENDERER.openScreen(new MainMenuScreen());
    }
}
