package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

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
//    public void optimizeField() {
//        for (int i = 0; i < Settings.ROWS; i+=2)
//            cells[i][Settings.COLUMNS - 1].getModel().detachAllChildren();
//    }
    public void setPlayers(int numberOfPlayers){
        for (int i = 0; i < numberOfPlayers; i++)
            players[i] = new Player(Color.values()[i], true, 1, 0);
        setTowers();
        setFloor();
    }
    private void setFloor() {

        Quad[][] floor = new Quad[Settings.ROWS - 1][Settings.COLUMNS - 1];
        Texture floorTex1 = assetManager.loadTexture("Models/Floor/Floor.png");
        Texture floorTex2 = assetManager.loadTexture("Models/Floor/Floor_2.png");
        Texture floorTex3 = assetManager.loadTexture("Models/Floor/Floor_3.png");
        Texture floorTex4 = assetManager.loadTexture("Models/Floor/Floor_4.png");
        Texture floorTex5 = assetManager.loadTexture("Models/Floor/Floor_5.png");
        Texture floorTex6 = assetManager.loadTexture("Models/Floor/Floor_6.png");
        Material floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material floorMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material floorMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material floorMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material floorMat5 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material floorMat6 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setTexture("ColorMap", floorTex1);
        floorMat2.setTexture("ColorMap", floorTex2);
        floorMat3.setTexture("ColorMap", floorTex3);
        floorMat4.setTexture("ColorMap", floorTex4);
        floorMat5.setTexture("ColorMap", floorTex5);
        floorMat6.setTexture("ColorMap", floorTex6);

        for (int i = 0; i < Settings.ROWS - 1; i+=2) {
            for (int j = 0; j < Settings.COLUMNS - 1; j++) {

                floor[i][j] = new Quad(3.7f, 3.7f);
                Geometry floorGeom = new Geometry("Floor", floor[i][j]);
                if(Settings.ROWS % 2 != 0) {
                    if (j == 0)
                        floorGeom.setMaterial(floorMat);
                    else if (j == Settings.COLUMNS - 2)
                        floorGeom.setMaterial(floorMat3);
                    else
                        floorGeom.setMaterial(floorMat2);
                }
                else {
                    if(i != Settings.ROWS - 2) {
                        if (j == 0)
                            floorGeom.setMaterial(floorMat);
                        else if (j == Settings.COLUMNS - 2)
                            floorGeom.setMaterial(floorMat3);
                        else
                            floorGeom.setMaterial(floorMat2);
                    }
                    else {
                        if (j == 0)
                            floorGeom.setMaterial(floorMat4);
                        else if (j == Settings.COLUMNS - 2)
                            floorGeom.setMaterial(floorMat5);
                        else
                            floorGeom.setMaterial(floorMat6);
                    }
                }
                floorGeom.rotate(-FastMath.HALF_PI, 0,0);
                floorGeom.setLocalTranslation(cells[i][j].getVector().x + 1.05f, 0.92f - (i * 0.001f) + (j * 0.001f), cells[i][j].getVector().z + 4.4f);
                rootNode.attachChild(floorGeom);
            }
        }
    }
    private void setTowers() {

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

        if (Settings.ROWS % 2 != 0) {
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
        else {
            cells[rows - 3][0].getModel().detachChildAt(0);
            cells[rows - 3][0].getModel().attachChild(resources.getModel(2).clone());
            cells[rows - 3][0].getModel().setMaterial(resources.getMaterial(4));
            cells[rows - 3][0].setColor(Color.GREEN);
            cells[rows - 3][0].setHeight(2);
            cells[rows - 3][0].setMaterial(resources.getMaterial(4));

            cells[rows - 2][columns - 1].getModel().detachChildAt(0);
            cells[rows - 2][columns - 1].getModel().attachChild(resources.getModel(2).clone());
            cells[rows - 2][columns - 1].getModel().setMaterial(resources.getMaterial(3));
            cells[rows - 2][columns - 1].setColor(Color.YELLOW);
            cells[rows - 2][columns - 1].setHeight(2);
            cells[rows - 2][columns - 1].setMaterial(resources.getMaterial(3));
        }
    }
    public Cell[][] getCells() {
        return cells;
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
