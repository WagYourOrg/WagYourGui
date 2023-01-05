package xyz.wagyourtail.battleship.client.screen.element;

import xyz.wagyourtail.battleship.client.Board;
import xyz.wagyourtail.wagyourgui.api.element.AbstractElement;
import xyz.wagyourtail.wagyourgui.api.render.Texture;

public class BoardElement extends AbstractElement {
    protected static final Texture boardAtlas = RENDERER.getTexture("battleship:textures/texture_atlas.png");
    protected static final int TEX_W = 80;
    protected static final int TEX_H = 64;
    private static final int BG_X = 0;
    private static final int BG_Y = 0;
    private static final int HIT_X = 48;
    private static final int HIT_Y = 16;
    protected final Board board;

    protected int piece_width;
    protected int piece_height;

    public BoardElement(int x, int y, int width, int height, Board board) {
        super(x, y, width, height);
        this.board = board;
    }



    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        piece_width = width / 10;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        piece_height = height / 10;
    }

    public void renderBackround() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                RENDERER.texturedRect(x + piece_width * i, y + piece_height * j, piece_width, piece_height,
                    boardAtlas,
                    BG_X, BG_Y, 16, 16, TEX_W, TEX_H
                );
            }
        }
    }

    public void renderHit(int x, int y, boolean hit) {
        int color = hit ? 0xFFFF0000 : 0xFFFFFFFF;
        RENDERER.texturedRect(x, y, piece_width, piece_height, boardAtlas, HIT_X, HIT_Y, 16, 16, TEX_W, TEX_H, color);
    }

    @Override
    public void onRender(int mouseX, int mouseY) {
        renderBackround();
    }

}
