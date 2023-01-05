package xyz.wagyourtail.battleship.client.screen;

import xyz.wagyourtail.battleship.client.Board;
import xyz.wagyourtail.battleship.client.GameStateMachine;
import xyz.wagyourtail.battleship.client.screen.element.PlacingBoardElement;
import xyz.wagyourtail.wagyourgui.api.container.impl.StringSelector;
import xyz.wagyourtail.wagyourgui.api.element.impl.Button;
import xyz.wagyourtail.wagyourgui.api.screen.Screen;
import xyz.wagyourtail.wagyourgui.api.screen.ScreenWithOverlays;

import java.util.stream.Collectors;

public class PlaceShipScreen extends ScreenWithOverlays {
    private final GameStateMachine game;
    private PlacingBoardElement board;
    private StringSelector shipSelector;

    public PlaceShipScreen(Screen parent, GameStateMachine game) {
        super(parent);
        this.game = game;
    }

    private void onPlacedShip(Board.ShipLocation ship) {
        Board.Ship current = board.getCurrentlyPlacing();
        if (current != null) {
            shipSelector.setOptions(current.getName(), board.getRemainingShips().stream().map(Board.Ship::getName).collect(Collectors.toList()));
        } else {
            removeElement(shipSelector);
        }
    }

    private void changeSelectedShip(StringSelector selector) {
        board.setCurrentlyPlacing(Board.Ship.valueOf((String) selector.getCurrent()));
    }

    private void undoPlacement(Button undo) {
        board.undoPlace();
        if (!elements[1].getElements().contains(shipSelector)) {
            setCurrentLayer(1);
            addElement(shipSelector);
        }
    }

    @Override
    public void onInit(int width, int height, boolean transparentBg) {
        super.onInit(width, height, transparentBg);

        board = addElement(new PlacingBoardElement(10, 30, 400, 400, game.getPlayer(), this::onPlacedShip));
        setCurrentLayer(1);
        shipSelector = addElement(new StringSelector(10, 10, 400, 20, height - 20, board.getCurrentlyPlacing().getName(), board.getRemainingShips().stream().map(Board.Ship::getName).collect(Collectors.toList()), this::changeSelectedShip));
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        super.onRender(mouseX, mouseY);
    }

}
