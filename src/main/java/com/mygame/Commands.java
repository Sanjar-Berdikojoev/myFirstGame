package com.mygame;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Ray;

import java.util.ArrayList;
import java.util.Random;

public class Commands {
    private static boolean isSelected;
    public static void selectTower() {

        if(Field.getCurrentCell() != null)
            Field.getCurrentCell().model.getChild(0).setMaterial(Field.getCurrentCell().getMaterial());

        Resources resources = Main.getResources();
        Ray ray = Main.getCursorRay();
        int rows = Settings.ROWS;
        int columns = Settings.COLUMNS;

        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell currentCell = Main.getField().getCells()[i][j];

                if(currentCell == null)
                    return;

                BoundingVolume boundingVolume = currentCell.model.getWorldBound();
                if(boundingVolume.intersects(ray) && currentCell.getHeight() > 1) {
                    if(currentCell.getColor() != currentPlayer.getColor())
                        return;
                    switch (currentCell.getColor()) {
                            case RED -> currentCell.model.setMaterial(resources.getMaterial(5));
                            case BLUE -> currentCell.model.setMaterial(resources.getMaterial(6));
                            case YELLOW -> currentCell.model.setMaterial(resources.getMaterial(7));
                            case GREEN -> currentCell.model.setMaterial(resources.getMaterial(8));
                    }
                    isSelected = true;
                    Field.setCurrentCell(currentCell);
                    return;
                }
                else {
                    currentCell.model.setMaterial(currentCell.getMaterial());
                    isSelected = false;
                    for (int k = 0; k < Field.getNeighbourCells().size(); k++)
                        Field.getNeighbourCells().get(k).model.getChild(0).setMaterial(Field.getNeighbourCells().get(k).getMaterial());
                }
            }
        }
    }
    public static void searchNeighbourCells(Cell currentCell) {

        if(currentCell == null)
            return;

        ArrayList <Cell> neighbourCells = Field.getNeighbourCells();
        neighbourCells.removeAll(Field.getNeighbourCells());

        Resources resources = Main.getResources();
        Player currentPlayer = Main.getField().getPlayers()[Field.getCurrentPlayerIndex()];

        if(currentCell.getColor() != currentPlayer.getColor())
            return;

        int i = currentCell.i;
        int j = currentCell.j;

        int rows = Settings.ROWS;
        int columns = Settings.COLUMNS;

        for (int x = i - 1; x <= i + 1; x++) {
            for (int y = j - 1; y <= j + 1; y++) {

                if(x < 0 || y < 0 || x >= rows || y >= columns)
                    continue;

                if((x == i && y == j))
                    continue;

                Cell neighbourCell = Main.getField().getCells()[x][y];

                if(neighbourCell == null)
                    continue;

                if(neighbourCell.getColor() == currentPlayer.getColor())
                    continue;

                float lineLength = currentCell.vector.distance(neighbourCell.vector);

                if(lineLength > 2.5f)
                    continue;

                neighbourCells.add(neighbourCell);

                if(neighbourCell.getHeight() == 0 && isSelected)
                    neighbourCell.model.getChild(0).setMaterial(resources.getMaterial(9));
            }
        }
    }
    public static void setNewTower(ArrayList <Cell> neighbourCells, Cell currentCell) {

        if(currentCell == null)
            return;

        if(currentCell.getHeight() < 2)
            return;

        Resources resources = Main.getResources();
        Ray ray = Main.getCursorRay();

        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        if(currentCell.getColor() != currentPlayer.getColor())
            return;

        ArrayList<Cell> neighbourCellsCopy = new ArrayList<>(neighbourCells);

        for (Cell neighbourCell : neighbourCellsCopy) {

            if (neighbourCell == null)
                continue;

            BoundingVolume boundingVolume = neighbourCell.model.getWorldBound();

            if (boundingVolume.intersects(ray)) {

                if(currentCell.getHeight() > neighbourCell.getHeight()) {

                    currentCell.model.detachChildAt(0);
                    currentCell.model.attachChild(resources.getModel(1).clone());
                    currentCell.model.setMaterial(resources.getMaterial(playerIndex + 1));
                    neighbourCell.model.detachChildAt(0);
                    neighbourCell.setMaterial(resources.getMaterial(playerIndex + 1));

                    if(neighbourCell.getHeight() == 0) {
                        neighbourCell.model.attachChild(resources.getModel(currentCell.getHeight() - 1).clone());
                        neighbourCell.model.getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
                        neighbourCell.setHeight(currentCell.getHeight() - 1);
                        neighbourCell.setColor(currentPlayer.getColor());
                        currentPlayer.setTowers(currentPlayer.getTowers() + 1);
                        currentCell.setHeight(1);
                    }
                    else {
                        neighbourCell.model.attachChild(resources.getModel(currentCell.getHeight() - neighbourCell.getHeight()).clone());
                        neighbourCell.model.getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
                        neighbourCell.setHeight(currentCell.getHeight() - neighbourCell.getHeight());
                        currentPlayer.setTowers(currentPlayer.getTowers() + 1);
                        currentCell.setHeight(1);
                        decreaseTowersNum(neighbourCell);
                        neighbourCell.setColor(currentPlayer.getColor());
                    }
                    checkIfPossibleToContinueSpreading(neighbourCell, neighbourCells);
                    checkIfNoMoves();
                    Main.getField().setTextOverTowers();
                }
                else if(currentCell.getHeight() < neighbourCell.getHeight()) {

                    currentCell.model.detachChildAt(0);
                    currentCell.model.attachChild(resources.getModel(1).clone());
                    currentCell.model.setMaterial(resources.getMaterial(playerIndex + 1));
                    neighbourCell.model.detachChildAt(0);
                    neighbourCell.model.attachChild(resources.getModel(neighbourCell.getHeight() - currentCell.getHeight()).clone());
                    neighbourCell.model.getChild(0).setMaterial(neighbourCell.getMaterial());
                    neighbourCell.setHeight(neighbourCell.getHeight() - currentCell.getHeight());
                    currentCell.setHeight(1);

                    checkIfPossibleToContinueSpreading(neighbourCell, neighbourCells);
                    checkIfNoMoves();
                    Main.getField().setTextOverTowers();

                    break;
                }
                else if(currentCell.getHeight() == neighbourCell.getHeight()) {

                    Random random = new Random();
                    int num = random.nextInt(2);
                    switch(num) {
                        case 0 -> {
                            currentCell.model.detachChildAt(0);
                            currentCell.model.attachChild(resources.getModel(1).clone());
                            currentCell.model.setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.model.detachChildAt(0);
                            neighbourCell.model.attachChild(resources.getModel(1).clone());
                            neighbourCell.model.getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.setHeight(1);
                            currentCell.setHeight(1);
                            currentPlayer.setTowers(currentPlayer.getTowers() + 1);
                            decreaseTowersNum(neighbourCell);
                            neighbourCell.setColor(currentPlayer.getColor());
                        }
                        case 1 -> {
                            currentCell.model.detachChildAt(0);
                            currentCell.model.attachChild(resources.getModel(1).clone());
                            currentCell.model.setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.model.detachChildAt(0);
                            neighbourCell.model.attachChild(resources.getModel(1).clone());
                            neighbourCell.model.getChild(0).setMaterial(neighbourCell.getMaterial());
                            neighbourCell.setHeight(1);
                            currentCell.setHeight(1);
                        }
                    }
                    checkIfPossibleToContinueSpreading(neighbourCell, neighbourCells);
                    checkIfNoMoves();
                    Main.getField().setTextOverTowers();
                }
            }
        }
    }

    public static void checkIfPossibleToContinueSpreading(Cell currentCell, ArrayList<Cell> neighbourCells) {

        for (Cell cell : neighbourCells) {
            if (cell.getHeight() == 0)
                cell.model.setMaterial(Main.getResources().getMaterial(0));
        }

        Field.setCurrentCell(currentCell);
        highlightTower(Field.getCurrentCell());
    }

    public static void addPointForTower(Cell currentCell) {

        if(currentCell == null)
            return;

        Resources resources = Main.getResources();
        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        if(currentPlayer.getPoints() <= 0)
            return;

        if(currentCell.getHeight() == Settings.MAX_HEIGHT)
            return;

        currentPlayer.setPoints(currentPlayer.getPoints() - 1);
        currentCell.setHeight(currentCell.getHeight() + 1);
        currentCell.model.detachChildAt(0);
        currentCell.model.attachChild(resources.getModel(currentCell.getHeight()).clone());
        currentCell.model.getChild(0).setMaterial(currentCell.getMaterial());
        Main.getField().setTextOverTowers();

        if(currentPlayer.getPoints() == 0)
            passMove();
    }
    public static void addMaxPointsForTower(Cell currentCell) {

        if(currentCell == null)
            return;

        while(Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].getPoints() > 0 && currentCell.getHeight() != Settings.MAX_HEIGHT)
            addPointForTower(currentCell);
    }

    public static Cell getTower(){

        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        for (int i = 0; i < Settings.ROWS; i++) {
            for (int j = 0; j < Settings.COLUMNS; j++) {
                Cell currentCell = Main.getField().getCells()[i][j];
                BoundingVolume boundingVolume = currentCell.model.getWorldBound();
                if (boundingVolume.intersects(Main.getCursorRay()) && currentCell.getHeight() > 0) {
                    if (currentCell.getColor() != currentPlayer.getColor())
                        return null;
                    else
                        return currentCell;
                }
            }
        }
        return null;
    }

    public static void highlightTower(Cell currentCell) {

        if (currentCell == null || currentCell.getHeight() < 2)
            return;

        if(currentCell.getColor() != Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].getColor())
            return;

        switch (currentCell.getColor()) {
            case RED -> currentCell.model.setMaterial(Main.getResources().getMaterial(5));
            case BLUE -> currentCell.model.setMaterial(Main.getResources().getMaterial(6));
            case YELLOW -> currentCell.model.setMaterial(Main.getResources().getMaterial(7));
            case GREEN -> currentCell.model.setMaterial(Main.getResources().getMaterial(8));
        }
        searchNeighbourCells(currentCell);
    }

    public static void passMove() {
        int numOfPlayers = Settings.getNumberOfPlayers();
        Main.getController().changeImage(0,0);
        Field.setCurrentPlayerIndex((Field.getCurrentPlayerIndex() + 1) % numOfPlayers);
        while(Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].isActive())
            Field.setCurrentPlayerIndex((Field.getCurrentPlayerIndex() + 1) % numOfPlayers);
        Settings.setCurrentPhase(0);
    }

    public static void decreaseTowersNum(Cell neighbourCell) {
        Player[] players = Main.getField().getPlayers();
        for (Player currentPlayer : players) {
            if (currentPlayer.getColor() == neighbourCell.getColor()) {
                currentPlayer.setTowers(currentPlayer.getTowers() - 1);
                if (currentPlayer.getTowers() == 0) {
                    currentPlayer.setActive(false);
                    Settings.setInactivePlayers(Settings.getInactivePlayers() + 1);
                }
                break;
            }
        }
    }

    public static void checkIfNoMoves() {

        boolean noMoves = true;

        Player currentPlayer = Main.getField().getPlayers()[Field.getCurrentPlayerIndex()];
        Cell[][] cells = Main.getField().getCells();
        for (int i = 0; i < Settings.ROWS; i++) {
            for (int j = 0; j < Settings.COLUMNS; j++) {
                Cell currentCell = cells[i][j];

                if(currentCell.getColor() == currentPlayer.getColor()) {
                    if(currentCell.getHeight() > 1) {
                        for (int x = i - 1; x <= i + 1; x++) {
                            for (int y = j - 1; y <= j + 1; y++) {

                                if(x < 0 || y < 0 || x == Settings.ROWS || y == Settings.COLUMNS)
                                    continue;

                                if((x == i && y == j))
                                    continue;

                                Cell neighbourCell = Main.getField().getCells()[x][y];

                                if(neighbourCell.getColor() == currentPlayer.getColor())
                                    continue;

                                float lineLength = currentCell.vector.distance(neighbourCell.vector);

                                if(lineLength > 2.5f)
                                    continue;

                                if(neighbourCell.getColor() != currentPlayer.getColor()) {
                                    noMoves = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(noMoves) {
            int playerIndex = Field.getCurrentPlayerIndex();
            int playerPoints = Main.getField().getPlayers()[playerIndex].getPoints();
            int numOfTowers = Main.getField().getPlayers()[playerIndex].getTowers();

            if(Field.getCurrentCell() != null)
                Field.getCurrentCell().model.getChild(0).setMaterial(Field.getCurrentCell().getMaterial());

            for (Cell cell : Field.getNeighbourCells()) {
                if(cell != null)
                    cell.model.getChild(0).setMaterial(cell.getMaterial());
            }

            Main.getController().changeImage(1,0);
            Main.getField().getPlayers()[playerIndex].setPoints(playerPoints + numOfTowers);
            Settings.setCurrentCommand(0);
            Settings.setCurrentPhase(1);
        }
    }
    public static void distributePoints() {

        int counter = 0;
        Player currentPlayer = Main.getField().getPlayers()[Field.getCurrentPlayerIndex()];
        Cell[][] cells = Main.getField().getCells();

        while(currentPlayer.getPoints() > 0) {
            for (int i = 0; i < Settings.ROWS; i++) {
                for (int j = 0; j < Settings.COLUMNS; j++) {
                    Cell currentCell = cells[i][j];

                    if(currentPlayer.getPoints() == 0)
                        return;

                    if(currentCell.getHeight() == Settings.MAX_HEIGHT && currentCell.getColor() == currentPlayer.getColor()) {
                        counter++;
                        continue;
                    }

                    if(currentCell.getColor() == currentPlayer.getColor()) {
                        currentPlayer.setPoints(currentPlayer.getPoints() - 1);
                        currentCell.setHeight(currentCell.getHeight() + 1);
                        currentCell.model.detachChildAt(0);
                        currentCell.model.attachChild(Main.getResources().getModel(currentCell.getHeight()).clone());
                        currentCell.model.getChild(0).setMaterial(currentCell.getMaterial());
                        counter++;
                        Main.getField().setTextOverTowers();
                    }
                }
            }
            if(counter >= currentPlayer.getTowers())
                return;
        }
    }
    public static void lowlightCells() {

        if(Field.getCurrentCell() != null)
            Field.getCurrentCell().model.getChild(0).setMaterial(Field.getCurrentCell().getMaterial());

        for (Cell cell : Field.getNeighbourCells()) {
            if(cell != null)
                cell.model.getChild(0).setMaterial(cell.getMaterial());
        }
    }
}