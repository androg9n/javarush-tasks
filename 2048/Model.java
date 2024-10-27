package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;
    public int score;
    public int maxTile;

    public Model() {
        score = 0;
        maxTile = 0;
        resetGameTiles();
    }

    void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<MoveEfficiency>(4, Collections.reverseOrder());
        priorityQueue.add(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                Model.this.up();
            }
        }));
        priorityQueue.add(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                Model.this.down();
            }
        }));
        priorityQueue.add(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                Model.this.left();
            }
        }));
        priorityQueue.add(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                Model.this.right();
            }
        }));
        priorityQueue.peek().getMove().move();
    }

    boolean hasBoardChanged() {
        Tile[][] previousTiles = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].value != previousTiles[i][j].value) return true;
            }
        }
        return false;
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency result;
        move.move();
        if (!hasBoardChanged()) {
            result = new MoveEfficiency(-1, 0, move);
        } else {
            result = new MoveEfficiency(getNumberOfEmptyTiles(), score, move);
        }
        rollback();
        return result;
    }

    private int getNumberOfEmptyTiles() {
        int result = 0;
        for (Tile[] tile1 : gameTiles) {
            for (Tile tile2 : tile1) if (tile2.isEmpty()) result++;
        }
        return result;
    }


    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0: left();
                break;
            case 1: right();
                break;
            case 2: up();
                break;
            case 3: down();
                break;
        }
    }

    private Tile[][] copyTiles(Tile[][] tiles) {
        Tile[][] result = new Tile[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) { result[i][j] = new Tile(tiles[i][j].value); }
        }
        return result;
    }

    private void saveState(Tile[][] tiles) {
        previousStates.push(copyTiles(tiles));
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (previousStates.empty() || previousScores.empty()) return;
        gameTiles = previousStates.pop();
        score = previousScores.pop();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public boolean canMove() {
        boolean result = false;
        if (!getEmptyTiles().isEmpty()) result = true;
        if (!result && getMergeTiles()) result = true;
        rotateGameTiles();
        if (!result && getMergeTiles()) result = true;
        for (int i = 0; i < 3; i++) rotateGameTiles();
        return result;
    }

    private boolean getMergeTiles() {
        for (Tile[] tiles : gameTiles) {
            for (int i = 0; i < tiles.length - 1; i++) {
                if (!tiles[i].isEmpty() && tiles[i].value == tiles[i + 1].value) {
                    return true;
                }
            }
        }
        return false;
    }

    public void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (emptyTiles.isEmpty()) return;
        int number = (int) (emptyTiles.size() * Math.random());
        emptyTiles.get(number).value = (int) (Math.random() < 0.9 ? 2 : 4);
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> result = new ArrayList<>();
        for (Tile[] tile1 : gameTiles) {
            for (Tile tile2 : tile1) if (tile2.isEmpty()) result.add(tile2);
        }
        return result;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean change = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].isEmpty() && !tiles[i + 1].isEmpty()) {
                change = true;
                moveLeft(tiles, i + 1);
            }
        }
        return change;
    }
    private void moveLeft(Tile[] tiles, int i) {
        if (i > 0) {
             tiles[i - 1] = tiles[i];
             tiles[i] = new Tile();
             if (i > 1 && tiles[i - 2].isEmpty()) moveLeft(tiles, i - 1);
        }
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean change = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (!tiles[i].isEmpty() && tiles[i].value == tiles[i + 1].value) {
                change = true;
                tiles[i].value *= 2;
                tiles[i + 1] = new Tile();
                if (maxTile < tiles[i].value) maxTile = tiles[i].value;
                score += tiles[i].value;
                compressTiles(tiles);
            }
        }
        return change;
    }

    public void left() {
        if (isSaveNeeded) saveState(gameTiles);
        boolean change = false;
        for (Tile[] tiles : gameTiles) {
            if (compressTiles(tiles)) change = true;
            if (mergeTiles(tiles)) change = true;
        }
        if (change) isSaveNeeded = true;
        if (change) addTile();
    }

    private void rotateGameTiles() {
        Tile[][] newGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
            for (int i = 0; i < FIELD_WIDTH; i++) {
                for (int j = 0; j < FIELD_WIDTH; j++) {
                    newGameTiles[j][FIELD_WIDTH - i - 1] = gameTiles[i][j];
                }
            }
            gameTiles = newGameTiles;
    }


    public void down() {
        if (isSaveNeeded) saveState(gameTiles);
        rotateGameTiles();
        left();
        for (int i = 0; i < 3; i++) rotateGameTiles();
    }

    public void right() {
        if (isSaveNeeded) saveState(gameTiles);
        for (int i = 0; i < 2; i++) rotateGameTiles();
        left();
        for (int i = 0; i < 2; i++) rotateGameTiles();
    }

    public void up() {
        if (isSaveNeeded) saveState(gameTiles);
        for (int i = 0; i < 3; i++) rotateGameTiles();
        left();
        rotateGameTiles();
    }







}
