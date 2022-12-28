package xyz.wagyourtail.battleship.client;

import xyz.wagyourtail.battleship.client.player.Opponent;

import static xyz.wagyourtail.battleship.client.GameStateMachine.State.*;

public class GameStateMachine {
    protected State gameState = PLACE;
    protected Board player;
    protected Opponent opponent;

    public GameStateMachine(Board player, Opponent opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    public void poll() {
        switch (gameState) {
            case PLACE:
            case GAME_OVER:
                break;
            case WAIT_FOR_OPPONENT_PLACE:
                if (opponent.pollPlaceDone()) {
                    transition(Status.WAITING, 0, 0);
                }
                break;
            case PLAYER_TURN:
                if (opponent.surrendered()) {
                    gameState = GAME_OVER;
                }
                break;
            case WAIT_FOR_OPPONENT_TURN: {
                Opponent.ShotStatus ack = opponent.ackDone();
                if (ack != null) {
                    transition(ack.status, ack.x, ack.y);
                }
                break;
            }
            case ACKNOWLEDGE_OPPONENT: {
                if (opponent.surrendered()) {
                    gameState = GAME_OVER;
                }
                Opponent.Shot shot = opponent.pollAttack();
                if (shot != null) {
                    transition(Status.WAITING, shot.x, shot.y);
                }
                break;
            }
        }
    }

    public void transition(Status status, int x, int y) {
        switch (gameState) {
            case PLACE:
                opponent.sendPlaceDone();
                gameState = WAIT_FOR_OPPONENT_PLACE;
                break;
            case WAIT_FOR_OPPONENT_PLACE:
                gameState = opponent.goesFirst() ? ACKNOWLEDGE_OPPONENT : PLAYER_TURN;
                break;
            case PLAYER_TURN:
                opponent.sendAttack(x, y);
                gameState = WAIT_FOR_OPPONENT_TURN;
                break;
            case WAIT_FOR_OPPONENT_TURN:
                if (status == Status.BS_ERROR) {
                    gameState = PLAYER_TURN;
                } else if (status == Status.GAME_END) {
                    gameState = GAME_OVER;
                } else {
                    player.attack(x, y, status);
                    gameState = ACKNOWLEDGE_OPPONENT;
                }
                break;
            case ACKNOWLEDGE_OPPONENT: {
                Status hitStatus = player.attacked(x, y);
                if (hitStatus == Status.GAME_END) {
                    gameState = GAME_OVER;
                } else if (hitStatus != Status.BS_ERROR) {
                    opponent.ackAttack(x, y, hitStatus);
                    gameState = PLAYER_TURN;
                } else {
                    opponent.ackAttack(x, y, hitStatus);
                }
                break;
            }
            case GAME_OVER:
                break;
        }
    }

    public State getState() {
        return gameState;
    }

    public Board getPlayer() {
        return player;
    }

    public Opponent getOpponent() {
        return opponent;
    }

    public enum State {
        PLACE,
        WAIT_FOR_OPPONENT_PLACE,
        PLAYER_TURN,
        WAIT_FOR_OPPONENT_TURN,
        ACKNOWLEDGE_OPPONENT,
        GAME_OVER
    }

    public enum Status {
        WAITING,
        HIT,
        MISS,
        SUNK_2,
        SUNK_3,
        SUNK_4,
        SUNK_5,
        GAME_END,
        BS_ERROR
    }
}
