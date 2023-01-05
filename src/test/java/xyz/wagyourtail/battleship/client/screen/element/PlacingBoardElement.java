package xyz.wagyourtail.battleship.client.screen.element;

import org.jetbrains.annotations.Nullable;
import xyz.wagyourtail.battleship.client.Board;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlacingBoardElement extends ShipBoardElement {
    @Nullable
    private Board.ShipLocation currentlyPlacing;

    private final Consumer<Board.ShipLocation> onPlace;

    public PlacingBoardElement(int x, int y, int width, int height, Board board, Consumer<Board.ShipLocation> onPlace) {
        super(x, y, width, height, board);
        this.onPlace = onPlace;
        setCurrentlyPlacing(getRemainingShips().iterator().next());
    }

    public void setCurrentlyPlacing(Board.Ship ship) {
        currentlyPlacing = new Board.ShipLocation(ship, 0, 0, true);
    }

    @Override
    public boolean onClicked(int mouseX, int mouseY, int button, int mods) {
        if (currentlyPlacing != null) {
            if (button == 1) {
                currentlyPlacing = new Board.ShipLocation(
                    currentlyPlacing.getShip(),
                    currentlyPlacing.x,
                    currentlyPlacing.y,
                    !currentlyPlacing.horizontal
                );
            } else if (board.placeShip(currentlyPlacing.getShip(), currentlyPlacing.x, currentlyPlacing.y, currentlyPlacing.horizontal)) {
                onPlace.accept(currentlyPlacing);
                Set<Board.Ship> remaining = getRemainingShips();
                if (remaining.isEmpty()) {
                    currentlyPlacing = null;
                } else {
                    setCurrentlyPlacing(remaining.iterator().next());
                }
            }
        }
        return true;
    }

    public Set<Board.Ship> getRemainingShips() {
        Set<Board.Ship> placed = board.getPlacedShips();
        return Arrays.stream(Board.Ship.values()).filter(e -> !placed.contains(e)).collect(Collectors.toSet());
    }

    public boolean done() {
        return getRemainingShips().isEmpty();
    }

    @Nullable
    public Board.Ship getCurrentlyPlacing() {
        if (currentlyPlacing == null) return null;
        return currentlyPlacing.getShip();
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        renderBackround();
        renderShips();

        // move ship to mouse
        if (currentlyPlacing != null) {
            // calculate board position of mouse
            int boardX = (mouseX - x) / piece_width;
            int boardY = (mouseY - y) / piece_height;

            currentlyPlacing = new Board.ShipLocation(
                currentlyPlacing.getShip(),
                boardX,
                boardY,
                currentlyPlacing.horizontal
            );

            // render ship
            renderShip(currentlyPlacing);
        }
    }

    public void undoPlace() {
        board.undoPlace();
        setCurrentlyPlacing(getRemainingShips().iterator().next());
    }

}
