package xyz.wagyourtail.battleship.client.player;

import xyz.wagyourtail.battleship.client.GameStateMachine;

public abstract class Opponent {
    public abstract boolean pollPlaceDone();

    public abstract void sendPlaceDone();

    public abstract void sendAttack(int x, int y);

    public abstract ShotStatus ackDone();

    public abstract Shot pollAttack();

    public abstract void ackAttack(int x, int y, GameStateMachine.Status status);

    public abstract void surrender();

    public abstract boolean goesFirst();

    public abstract boolean surrendered();

    public static class ShotStatus {
        public final int x;
        public final int y;
        public final GameStateMachine.Status status;

        public ShotStatus(int x, int y, GameStateMachine.Status status) {
            this.x = x;
            this.y = y;
            this.status = status;
        }
    }

    public static class Shot {
        public final int x;
        public final int y;

        public Shot(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
