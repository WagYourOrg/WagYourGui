package xyz.wagyourtail.battleship.client.screen.element;

import xyz.wagyourtail.battleship.client.Board;

public abstract class ShipBoardElement extends BoardElement {

    public ShipBoardElement(int x, int y, int width, int height, Board board) {
        super(x, y, width, height, board);
    }

    public void renderShip(Board.ShipLocation ship) {
        // detect if ship stays within bounds
        int color = 0xFFFFFFFF;
        if (ship.x < 0 || ship.y < 0 || ship.x > 9 || ship.y > 9) {
            color = 0xFFFF0000;
        }
        if (ship.horizontal && ship.x + ship.getShip().getLength() > 10) {
            color = 0xFFFF0000;
        }
        if (!ship.horizontal && ship.y + ship.getShip().getLength() > 10) {
            color = 0xFFFF0000;
        }
        // render ship
        if (ship.horizontal) {
            if (ship.y < 0 || ship.y > 9) return;
            for (int i = 0, sX = ship.x; i < ship.getShip().getLength() && sX < 10; ++sX, ++i) {
                if (sX < 0) continue;
                int texX = ship.getShip().getTiles()[i][0];
                int texY = ship.getShip().getTiles()[i][1];
                RENDERER.texturedRect(x + piece_width * sX, y + piece_height * ship.y, piece_width, piece_height,
                    boardAtlas,
                    texX, texY, 16, 16, TEX_W, TEX_H,
                    color);
            }
        } else {
            if (ship.x < 0 || ship.x > 9) return;
            for (int i = 0, sY = ship.y; i < ship.getShip().getLength() && sY < 10; ++sY, ++i) {
                if (sY < 0) continue;
                int texX = ship.getShip().getTiles()[i][0];
                int texY = ship.getShip().getTiles()[i][1];
                RENDERER.rotatedTextureRect(
                x + piece_width * ship.x,
                y + piece_height * sY,
                piece_width,
                piece_height,
                    boardAtlas,
                    texX,
                    texY + 16,
                    texX,
                    texY,
                    texX + 16,
                    texY + 16,
                    TEX_W,
                    TEX_H,
                    color);
            }
        }
    }

    public void renderShips() {
        for (Board.ShipLocation ship : board.getShipLocations()) {
            renderShip(ship);
        }
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        renderBackround();
        renderShips();
        board.forBoard(this::renderHit);
    }

}
