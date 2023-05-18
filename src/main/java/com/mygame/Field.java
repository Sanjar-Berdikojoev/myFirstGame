package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import java.util.ArrayList;

public class Field {
    private static Cell currentCell = null;
    private static ArrayList<Cell> neighbourCells = new ArrayList<>();
    private final int rows;
    private final int columns;
    private final AssetManager assetManager;
    private final Resources resources;
    private final Node rootNode;
    private Vector3f cellPos;
    private Player[] players = new Player[Settings.getNumberOfPlayers()];
    private Cell[][] cells;
    private static int currentPlayerIndex = 0;
    Field(AssetManager assetManager, Node rootNode, int rows, int columns) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.rows = rows;
        this.columns = columns;
        resources = new Resources(assetManager);
    }
    public void createField(){
        cells = new Cell[rows][columns];

        resources.setModelsAndMaterials();
        float xPos = 0.0f;
        float yPos = 0.0f;
        float zPos = 0.0f;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(i % 2 != 0)
                    cellPos = new Vector3f(xPos - 1.0f, yPos, zPos);
                else
                    cellPos = new Vector3f(xPos, yPos, zPos);
                xPos += 2.0f;
                Node node = new Node();
                node.attachChild(resources.getModel(0).clone());
                node.setMaterial(resources.getMaterial(0));
                node.setLocalTranslation(cellPos);
                cells[i][j] = new Cell(0, cellPos, Color.GRAY, resources.getMaterial(0), node, i, j);
                rootNode.attachChild(cells[i][j].getModel());
            }
            xPos = 0.0f;
            zPos += 1.75f;
        }
    }

    public void optimizeField() {
        for (int i = 0; i < Settings.COLUMNS; i+=2)
            cells[Settings.ROWS - 1][i] = null;
    }

    public void setPlayers(int numberOfPlayers){
        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(Color.values()[i], true, 1, 0);
        setTowers();
    }
    public void setTowers(){
        cells[1][0].getModel().detachChildAt(0);
        cells[1][0].getModel().attachChild(resources.getModel(2).clone());
        cells[1][0].getModel().setMaterial(resources.getMaterial(1));
        cells[1][0].setColor(Color.RED);
        cells[1][0].setHeight(2);
        cells[1][0].setMaterial(resources.getMaterial(1));

        cells[2][columns - 1].getModel().detachChildAt(0);
        cells[2][columns - 1].getModel().attachChild(resources.getModel(2).clone());
        cells[2][columns - 1].getModel().setMaterial(resources.getMaterial(2));
        cells[2][columns - 1].setColor(Color.BLUE);
        cells[2][columns - 1].setHeight(2);
        cells[2][columns - 1].setMaterial(resources.getMaterial(2));

        cells[rows - 2][0].getModel().detachChildAt(0);
        cells[rows - 2][0].getModel().attachChild(resources.getModel(2).clone());
        cells[rows - 2][0].getModel().setMaterial(resources.getMaterial(4));
        cells[rows - 2][0].setColor(Color.GREEN);
        cells[rows - 2][0].setHeight(2);
        cells[rows - 2][0].setMaterial(resources.getMaterial(4));

        cells[rows - 3][columns - 1].getModel().detachChildAt(0);
        cells[rows - 3][columns - 1].getModel().attachChild(resources.getModel(2).clone());
        cells[rows - 3][columns - 1].getModel().setMaterial(resources.getMaterial(3));
        cells[rows - 3][columns - 1].setColor(Color.YELLOW);
        cells[rows - 3][columns - 1].setHeight(2);
        cells[rows - 3][columns - 1].setMaterial(resources.getMaterial(3));
    }
    public Cell[][] getCells() {
        return cells;
    }
    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }
    public static Cell getCurrentCell() {
        return currentCell;
    }
    public static void setCurrentCell(Cell currentCell) {
        Field.currentCell = currentCell;
    }
    public static ArrayList<Cell> getNeighbourCells() {
        return neighbourCells;
    }
    public static void setNeighbourCells(ArrayList<Cell> neighbourCells) {
        Field.neighbourCells = neighbourCells;
    }
    public Player[] getPlayers() {
        return players;
    }
    public static int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public static void setCurrentPlayerIndex(int currentPlayerIndex) {
        Field.currentPlayerIndex = currentPlayerIndex;
    }
}
