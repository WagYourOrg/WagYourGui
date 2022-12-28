package xyz.wagyourtail.battleship.client.player;

import xyz.wagyourtail.battleship.client.Board;
import xyz.wagyourtail.battleship.client.GameStateMachine;

public class BotOpponent extends Opponent {
    public Board board;
    private int lastShotX;
    private int lastShotY;
    private GameStateMachine.Status lastShotResult = GameStateMachine.Status.WAITING;

    @Override
    public boolean pollPlaceDone() {
        for (Board.Ship ship : Board.Ship.values()) {
            placeRandomShip(ship);
        }
        board.freeze();
        return true;
    }

    public void placeRandomShip(Board.Ship ship) {
        int x, y;
        boolean horizontal;
        do {
            x = (int) (Math.random() * 10);
            y = (int) (Math.random() * 10);
            horizontal = Math.random() > 0.5;
        } while (!board.placeShip(ship, x, y, horizontal));
    }

    @Override
    public void sendPlaceDone() {
    }

    @Override
    public void sendAttack(int x, int y) {
        if (lastShotResult != GameStateMachine.Status.WAITING) {
            throw new IllegalStateException("last shot not acknowledged");
        }
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            lastShotResult = GameStateMachine.Status.BS_ERROR;
        } else {
            lastShotResult = board.attacked(x, y);
            lastShotX = x;
            lastShotY = y;
        }
    }

    @Override
    public ShotStatus ackDone() {
        if (lastShotResult == GameStateMachine.Status.WAITING) {
            throw new IllegalStateException("last shot not sent");
        }
        ShotStatus status = new ShotStatus(lastShotX, lastShotY, lastShotResult);
        lastShotResult = GameStateMachine.Status.WAITING;
        return status;
    }

    @Override
    public Shot pollAttack() {
        int x, y;
        do {
            x = (int) (Math.random() * 10);
            y = (int) (Math.random() * 10);
        } while (board.hasAttacked(x, y));
        return new Shot(x, y);
    }

    @Override
    public void ackAttack(int x, int y, GameStateMachine.Status status) {
        board.attack(x, y, status);
    }

    @Override
    public void surrender() {
    }

    @Override
    public boolean goesFirst() {
        return Math.random() < 0.5;
    }

    @Override
    public boolean surrendered() {
        return false;
    }
}
