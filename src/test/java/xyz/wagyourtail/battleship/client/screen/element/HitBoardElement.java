package xyz.wagyourtail.battleship.client.screen.element;

import xyz.wagyourtail.battleship.client.Board;

public class HitBoardElement extends BoardElement {
    public HitBoardElement(int x, int y, int width, int height, Board board) {
        super(x, y, width, height, board);
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        super.onRender(mouseX, mouseY);
        board.forAttackBoard(this::renderHit);
    }

}
