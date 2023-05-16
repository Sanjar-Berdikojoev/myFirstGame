package com.mygame;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Ray;

import java.util.ArrayList;
import java.util.Random;

public class Command {
    private static boolean isSelected;
    public static void onRightMouseButtonClick() {

        if(Field.getCurrentCell() != null)
            Field.getCurrentCell().getModel().getChild(0).setMaterial(Field.getCurrentCell().getMaterial());

        Resources resources = Main.getResources();
        Ray ray = Main.getCursorRay();
        int rows = Settings.ROWS;
        int columns = Settings.COLUMNS;

        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell currentCell = Main.getField().getCells()[i][j];
                BoundingVolume boundingVolume = currentCell.getModel().getWorldBound();
                if(boundingVolume.intersects(ray) && currentCell.getHeight() > 1) {
                    if(currentCell.getColor() != currentPlayer.getColor())
                        return;
                    switch (currentCell.getColor()) {
                            case RED -> currentCell.getModel().setMaterial(resources.getMaterial(5));
                            case BLUE -> currentCell.getModel().setMaterial(resources.getMaterial(6));
                            case YELLOW -> currentCell.getModel().setMaterial(resources.getMaterial(7));
                            case GREEN -> currentCell.getModel().setMaterial(resources.getMaterial(8));
                    }
                    isSelected = true;
                    Field.setCurrentCell(currentCell);
                    return;
                }
                else {
                    currentCell.getModel().setMaterial(currentCell.getMaterial());
                    isSelected = false;
                    for (int k = 0; k < Field.getNeighbourCells().size(); k++)
                        Field.getNeighbourCells().get(k).getModel().getChild(0).setMaterial(Field.getNeighbourCells().get(k).getMaterial());
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

        int i = currentCell.getI();
        int j = currentCell.getJ();

        int rows = Settings.ROWS;
        int columns = Settings.COLUMNS;

        for (int x = i - 1; x <= i + 1; x++) {
            for (int y = j - 1; y <= j + 1; y++) {

                if(x < 0 || y < 0 || x >= rows || y >= columns)
                    continue;

                if((x == i && y == j))
                    continue;

                Cell neighbourCell = Main.getField().getCells()[x][y];

                if(neighbourCell.getColor() == currentPlayer.getColor())
                    continue;

                float lineLength = currentCell.getVector().distance(neighbourCell.getVector());

                if(lineLength > 2.5f)
                    continue;

                neighbourCells.add(neighbourCell);

                if(neighbourCell.getHeight() == 0 && isSelected)
                    neighbourCell.getModel().getChild(0).setMaterial(resources.getMaterial(9));
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

        ArrayList<Cell> neighbourCellsCopy = new ArrayList<>(neighbourCells); //Conca....

        for (Cell neighbourCell : neighbourCellsCopy) {

            if (neighbourCell == null)
                continue;

            BoundingVolume boundingVolume = neighbourCell.getModel().getWorldBound();

            if (boundingVolume.intersects(ray)) {

                if(currentCell.getHeight() > neighbourCell.getHeight()) {

                    currentCell.getModel().detachChildAt(0);
                    currentCell.getModel().attachChild(resources.getModel(1).clone());
                    currentCell.getModel().setMaterial(resources.getMaterial(playerIndex + 1));
                    neighbourCell.getModel().detachChildAt(0);
                    neighbourCell.setMaterial(resources.getMaterial(playerIndex + 1));

                    if(neighbourCell.getHeight() == 0) {
                        neighbourCell.getModel().attachChild(resources.getModel(currentCell.getHeight() - 1).clone());
                        neighbourCell.getModel().getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
                        neighbourCell.setHeight(currentCell.getHeight() - 1);
                        neighbourCell.setColor(currentPlayer.getColor());
                        currentPlayer.setTowers(currentPlayer.getTowers() + 1);
                        currentCell.setHeight(1);

                        for (Cell cell : neighbourCells) {
                            if (cell.getHeight() == 0)
                                cell.getModel().setMaterial(resources.getMaterial(0));
                        }

                        if (neighbourCell.getHeight() < 2)
                            return;
                        else {
                            Field.setCurrentCell(neighbourCell);
                            selectTower(Field.getCurrentCell());
                        }
                    }
                    else {
                        neighbourCell.getModel().attachChild(resources.getModel(currentCell.getHeight() - neighbourCell.getHeight()).clone());
                        neighbourCell.getModel().getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
                        neighbourCell.setHeight(currentCell.getHeight() - neighbourCell.getHeight());
                        neighbourCell.setColor(currentPlayer.getColor());
                        currentPlayer.setTowers(currentPlayer.getTowers() + 1);
                        currentCell.setHeight(1);

                        for (int i = 0; i < Main.getField().getPlayers().length; i++) {
                            if(Main.getField().getPlayers()[i].getColor() == neighbourCell.getColor()) {
                                Main.getField().getPlayers()[i].setTowers(Main.getField().getPlayers()[i].getTowers() - 1);
                                if(Main.getField().getPlayers()[i].getTowers() == 0) {
                                    Main.getField().getPlayers()[i].setActive(false);
                                    Settings.setInactivePlayers(Settings.getInactivePlayers() + 1);
                                }
                                break;
                            }
                        }

                        for (Cell cell : neighbourCells) {
                            if (cell.getHeight() == 0)
                                cell.getModel().setMaterial(resources.getMaterial(0));
                        }

                        if (neighbourCell.getHeight() < 2)
                            return;
                        else {
                            Field.setCurrentCell(neighbourCell);
                            selectTower(Field.getCurrentCell());
                        }
                    }
                }
                else if(currentCell.getHeight() < neighbourCell.getHeight()) {

                    currentCell.getModel().detachChildAt(0);
                    currentCell.getModel().attachChild(resources.getModel(1).clone());
                    currentCell.getModel().setMaterial(resources.getMaterial(playerIndex + 1));
                    neighbourCell.getModel().detachChildAt(0);
                    neighbourCell.getModel().attachChild(resources.getModel(neighbourCell.getHeight() - currentCell.getHeight()).clone()); // OutOfBound
                    neighbourCell.getModel().getChild(0).setMaterial(neighbourCell.getMaterial());
                    neighbourCell.setHeight(neighbourCell.getHeight() - currentCell.getHeight()); // add +1 for height ?
                    currentCell.setHeight(1);

                    break;
                }
                else if(currentCell.getHeight() == neighbourCell.getHeight()) {

                    Random random = new Random();
                    int num = random.nextInt(2);
                    switch(num){
                        case 0 -> {
                            currentCell.getModel().detachChildAt(0);
                            currentCell.getModel().attachChild(resources.getModel(1).clone());
                            currentCell.getModel().setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.getModel().detachChildAt(0);
                            neighbourCell.getModel().attachChild(resources.getModel(1).clone());
                            neighbourCell.getModel().getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.setHeight(1); // add +1 for height ?
                            currentCell.setHeight(1);
                            neighbourCell.setColor(currentPlayer.getColor());
                            currentPlayer.setTowers(currentPlayer.getTowers() + 1);

                            for (int i = 0; i < Main.getField().getPlayers().length; i++) {
                                if(Main.getField().getPlayers()[i].getColor() == neighbourCell.getColor()) {
                                    Main.getField().getPlayers()[i].setTowers(Main.getField().getPlayers()[i].getTowers() - 1);
                                    if(Main.getField().getPlayers()[i].getTowers() == 0) {
                                        Main.getField().getPlayers()[i].setActive(false);
                                        Settings.setInactivePlayers(Settings.getInactivePlayers() + 1);
                                    }
                                    break;
                                }
                            }
                        }
                        case 1 -> {
                            currentCell.getModel().detachChildAt(0);
                            currentCell.getModel().attachChild(resources.getModel(1).clone());
                            currentCell.getModel().setMaterial(resources.getMaterial(playerIndex + 1));
                            neighbourCell.getModel().detachChildAt(0);
                            neighbourCell.getModel().attachChild(resources.getModel(1).clone());
                            neighbourCell.getModel().getChild(0).setMaterial(neighbourCell.getMaterial());
                            neighbourCell.setHeight(1); // add +1 for height ?
                            currentCell.setHeight(1);
                        }
                    }
                }
            }
        }
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
        currentCell.getModel().detachChildAt(0);
        currentCell.getModel().attachChild(resources.getModel(currentCell.getHeight()).clone());
        currentCell.getModel().getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
        currentCell.setMaterial(resources.getMaterial(playerIndex + 1));
    }

    public static void addMaxPointsForTower(Cell currentCell) {

        if(currentCell == null)
            return;

        Resources resources = Main.getResources();
        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        if(currentPlayer.getPoints() <= 0)
            return;

        if(currentCell.getHeight() == Settings.MAX_HEIGHT)
            return;

        while(currentPlayer.getPoints() > 0 && currentCell.getHeight() != Settings.MAX_HEIGHT) {
            currentPlayer.setPoints(currentPlayer.getPoints() - 1);
            currentCell.setHeight(currentCell.getHeight() + 1);
            currentCell.getModel().detachChildAt(0);
            currentCell.getModel().attachChild(resources.getModel(currentCell.getHeight()).clone());
            currentCell.getModel().getChild(0).setMaterial(resources.getMaterial(playerIndex + 1));
            currentCell.setMaterial(resources.getMaterial(playerIndex + 1));
        }
    }

    public static Cell getTower(){

        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        for (int i = 0; i < Settings.ROWS; i++) {
            for (int j = 0; j < Settings.COLUMNS; j++) {
                Cell currentCell = Main.getField().getCells()[i][j];
                BoundingVolume boundingVolume = currentCell.getModel().getWorldBound();
                if (boundingVolume.intersects(Main.getCursorRay()) && currentCell.getHeight() > 0) {
                    if (currentCell.getColor() != currentPlayer.getColor())
                        return null;
                    else {
                        return currentCell;
                    }
                }
            }
        }
        return null;
    }

    public static void selectTower(Cell currentCell) {

        if (currentCell == null || currentCell.getHeight() < 2)
            return;

        if(currentCell.getColor() != Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].getColor())
            return;

        switch (currentCell.getColor()) {
            case RED -> currentCell.getModel().setMaterial(Main.getResources().getMaterial(5));
            case BLUE -> currentCell.getModel().setMaterial(Main.getResources().getMaterial(6));
            case YELLOW -> currentCell.getModel().setMaterial(Main.getResources().getMaterial(7));
            case GREEN -> currentCell.getModel().setMaterial(Main.getResources().getMaterial(8));
        }
        searchNeighbourCells(currentCell);
    }

    public static void selectTowerForPhase2() {

        Resources resources = Main.getResources();
        Ray ray = Main.getCursorRay();
        int rows = Settings.ROWS;
        int columns = Settings.COLUMNS;

        int playerIndex = Field.getCurrentPlayerIndex();
        Player currentPlayer = Main.getField().getPlayers()[playerIndex];

        if(currentPlayer.getPoints() == 0)
            return;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell currentCell = Main.getField().getCells()[i][j];
                BoundingVolume boundingVolume = currentCell.getModel().getWorldBound();
                if(boundingVolume.intersects(ray) && currentCell.getHeight() > 0) {
                    if(currentCell.getColor() != currentPlayer.getColor())
                        return;
                    switch (currentCell.getColor()) {
                        case RED -> currentCell.getModel().setMaterial(resources.getMaterial(5));
                        case BLUE -> currentCell.getModel().setMaterial(resources.getMaterial(6));
                        case YELLOW -> currentCell.getModel().setMaterial(resources.getMaterial(7));
                        case GREEN -> currentCell.getModel().setMaterial(resources.getMaterial(8));
                    }
                    return;
                }
                else
                    currentCell.getModel().setMaterial(currentCell.getMaterial());
            }
        }
    }
}