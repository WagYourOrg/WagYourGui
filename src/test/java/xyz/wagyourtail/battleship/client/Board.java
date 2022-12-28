package xyz.wagyourtail.battleship.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {
    protected int[][] board = new int[10][10];
    protected int[][] attackBoard = new int[10][10];
    protected int shipTilesAlive = 0;
    protected ShipLocation[] ships = new ShipLocation[5];
    private boolean frozen = false;
    private int shipsPlaced = 0;

    public void attack(int x, int y, GameStateMachine.Status status) {
        switch (status) {
            case HIT:
            case SUNK_2:
            case SUNK_3:
            case SUNK_4:
            case SUNK_5:
            case GAME_END:
                attackBoard[y][x] |= 2;
                break;
            case MISS:
                attackBoard[y][x] |= 1;
                break;
            case BS_ERROR:
            case WAITING:
                break;
        }
    }

    public boolean placeShip(Ship ship, int x, int y, boolean horizontal) {
        if (frozen) throw new IllegalStateException("Cannot place ships after game start.");
        if (Arrays.stream(ships).anyMatch(e -> ship.equals(e.getShip())))
            throw new IllegalArgumentException("Ship already placed.");
        if (x < 0 || x > 9 || y < 0 || y > 9) throw new IllegalArgumentException("Invalid coordinates.");
        if (horizontal) {
            for (int i = x; i < x + ship.getLength(); i++) {
                if (i > 9) return false;
                if (board[y][i] != 0) return false;
            }
            for (int i = x; i < x + ship.getLength(); i++) {
                board[y][i] = ship.getId() << 2 | 2;
            }
            ships[shipsPlaced++] = new ShipLocation(ship, x, y, horizontal);
        } else {
            for (int i = y; i < y + ship.getLength(); i++) {
                if (i > 9) return false;
                if (board[i][x] != 0) return false;
            }
            for (int i = y; i < y + ship.getLength(); i++) {
                board[i][x] = ship.getId() << 2 | 2;
            }
            ships[shipsPlaced++] = new ShipLocation(ship, x, y, horizontal);
        }
        shipTilesAlive += ship.getLength();
        return true;
    }

    public boolean attackBoardHit(int x, int y) {
        return (attackBoard[x][y] & 2) != 0;
    }

    public boolean hasAttacked(int x, int y) {
        return attackBoard[x][y] != 0;
    }

    public int undoPlace() {
        if (frozen) throw new IllegalStateException("Cannot place ships after game start.");
        if (shipsPlaced == 0) throw new IllegalStateException("No ships placed.");
        ShipLocation ship = ships[--shipsPlaced];
        ships[shipsPlaced] = null;
        shipTilesAlive -= ship.getShip().getLength();
        if (ship.horizontal) {
            for (int i = ship.x; i < ship.x + ship.getShip().getLength(); i++) {
                board[ship.y][i] = 0;
            }
        } else {
            for (int i = ship.y; i < ship.y + ship.getShip().getLength(); i++) {
                board[i][ship.x] = 0;
            }
        }
        return ship.getShip().getId();
    }

    public void freeze() {
        frozen = true;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public GameStateMachine.Status attacked(int x, int y) {
        if ((board[y][x] & 1) != 0) {
            return GameStateMachine.Status.BS_ERROR;
        }
        if ((board[y][x] & 2) == 0) {
            board[y][x] |= 1;
            return GameStateMachine.Status.MISS;
        }
        board[y][x] |= 1;
        shipTilesAlive--;
        if (shipTilesAlive == 0) {
            return GameStateMachine.Status.GAME_END;
        }
        int shipId = board[y][x] >> 2;
        Ship ship = Ship.idMap.get(shipId);
        ShipLocation location = Arrays.stream(ships).filter(e -> e.getShip().equals(ship)).findFirst().orElse(null);
        if (location == null) {
            throw new IllegalStateException("Ship not found.");
        }
        int tilesAlive = 0;
        if (location.horizontal) {
            for (int i = location.x; i < location.x + location.getShip().getLength(); i++) {
                if ((board[location.y][i] & 1) == 0) {
                    tilesAlive++;
                }
            }
        } else {
            for (int i = location.y; i < location.y + location.getShip().getLength(); i++) {
                if ((board[i][location.x] & 1) == 0) {
                    tilesAlive++;
                }
            }
        }
        if (tilesAlive == 0) {
            return GameStateMachine.Status.valueOf("SUNK_" + location.getShip().getLength());
        }
        return GameStateMachine.Status.HIT;
    }

    public boolean isLost() {
        return shipTilesAlive == 0;
    }

    public Set<Ship> getPlacedShips() {
        return Arrays.stream(ships).map(ShipLocation::getShip).collect(Collectors.toSet());
    }

    public enum Ship {
        AircraftCarrier("Aircraft Carrier", new int[][]{{0, 48}, {16, 48}, {32, 48}, {48, 48}, {64, 48}}),
        Battleship("Battleship", new int[][]{{0, 32}, {16, 32}, {32, 32}, {48, 32}}),
        Submarine("Submarine", new int[][]{{64, 0}, {64, 16}, {64, 32}}),
        Destroyer("Destroyer", new int[][]{{0, 16}, {16, 16}, {32, 16}}),
        PatrolBoat("Patrol Boat", new int[][]{{32, 0}, {48, 0}});

        private static final Map<Integer, Ship> idMap = new HashMap<>();

        static {
            for (Ship ship : Ship.values()) {
                idMap.put(ship.getId(), ship);
            }
        }

        private final String name;
        private final int[][] tiles;

        Ship(String name, int[][] tiles) {
            this.name = name;
            this.tiles = tiles;
        }

        public String getName() {
            return name;
        }

        public int getLength() {
            return tiles.length;
        }

        public int[][] getTiles() {
            return tiles;
        }

        public int getId() {
            return 1 << ordinal();
        }
    }

    public static class ShipLocation {
        private final Ship ship;
        private final int x;
        private final int y;
        private final boolean horizontal;

        public ShipLocation(Ship ship, int x, int y, boolean horizontal) {
            this.ship = ship;
            this.x = x;
            this.y = y;
            this.horizontal = horizontal;
        }

        public Ship getShip() {
            return ship;
        }
    }
}
