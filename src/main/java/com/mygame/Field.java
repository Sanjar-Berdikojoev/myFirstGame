package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import java.util.ArrayList;
import java.util.Random;

public class Field {
    private static Cell currentCell = null;
    public static final ArrayList<Cell> NEIGHBOUR_CELLS = new ArrayList<>();
    private final int rows;
    private final int columns;
    private final AssetManager assetManager;
    private final Resources resources;
    private final Node rootNode;
    private final Player[] PLAYERS = new Player[Settings.getNumberOfPlayers()];
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
                Vector3f cellPos;
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
                rootNode.attachChild(cells[i][j].model);
            }
            xPos = 0.0f;
            zPos += 1.75f;
        }
    }
    public void setPlayers(int numberOfPlayers) {

        Random random = new Random();
        for(int i = 0; i < numberOfPlayers; i++) {
            int index = random.nextInt(100000);
            PLAYERS[i] = new Player("Guest" + index, Color.values()[i], true, 1, 0);
        }
        setTowers();
        setFloor();
    }
    public void setTextsOverTowers(Camera camera) {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText[][] texts = new BitmapText[rows][columns];
        for (Spatial child : rootNode.getChildren()) {
            if (child instanceof BitmapText) {
                rootNode.detachChild(child);
            }
        }
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                Cell currentCell = cells[i][j];
                if(currentCell.getHeight() > 0) {
                    BoundingVolume boundingVolume = currentCell.model.getWorldBound();
                    Vector3f modelCenter = boundingVolume.getCenter().clone();
                    float modelHeight = currentCell.model.getWorldScale().y;
                    Vector3f textPosition = modelCenter.add(-0.065f, modelHeight + 0.3f, 0);

                    texts[i][j] = new BitmapText(font);
                    texts[i][j].setLocalTranslation(textPosition);
                    texts[i][j].setSize(0.25f);
                    texts[i][j].setColor(ColorRGBA.White);
                    texts[i][j].setText(Integer.toString(currentCell.getHeight()));

                    Vector3f cameraPosition = camera.getLocation();
                    Vector3f direction = cameraPosition.subtract(texts[i][j].getLocalTranslation()).multLocal(1, 0, 1).normalizeLocal();
                    Quaternion rotation = new Quaternion();
                    rotation.lookAt(direction, Vector3f.UNIT_Y);

                    texts[i][j].setLocalRotation(rotation);
                    texts[i][j].setColor(ColorRGBA.White);
                    rootNode.attachChild(texts[i][j]);
                }
            }
        }
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
                floorGeom.setLocalTranslation(cells[i][j].vector.x + 1.05f, 0.92f - (i * 0.0001f) + (j * 0.0001f), cells[i][j].vector.z + 4.4f);
                rootNode.attachChild(floorGeom);
            }
        }
    }
    private void setTowers() {

        cells[1][0].model.detachChildAt(0);
        cells[1][0].model.attachChild(resources.getModel(2).clone());
        cells[1][0].model.setMaterial(resources.getMaterial(1));
        cells[1][0].setColor(Color.RED);
        cells[1][0].setHeight(2);
        cells[1][0].setMaterial(resources.getMaterial(1));

        cells[2][columns - 1].model.detachChildAt(0);
        cells[2][columns - 1].model.attachChild(resources.getModel(2).clone());
        cells[2][columns - 1].model.setMaterial(resources.getMaterial(2));
        cells[2][columns - 1].setColor(Color.BLUE);
        cells[2][columns - 1].setHeight(2);
        cells[2][columns - 1].setMaterial(resources.getMaterial(2));

        if (Settings.ROWS % 2 != 0) {
            cells[rows - 2][0].model.detachChildAt(0);
            cells[rows - 2][0].model.attachChild(resources.getModel(2).clone());
            cells[rows - 2][0].model.setMaterial(resources.getMaterial(4));
            cells[rows - 2][0].setColor(Color.GREEN);
            cells[rows - 2][0].setHeight(2);
            cells[rows - 2][0].setMaterial(resources.getMaterial(4));

            cells[rows - 3][columns - 1].model.detachChildAt(0);
            cells[rows - 3][columns - 1].model.attachChild(resources.getModel(2).clone());
            cells[rows - 3][columns - 1].model.setMaterial(resources.getMaterial(3));
            cells[rows - 3][columns - 1].setColor(Color.YELLOW);
            cells[rows - 3][columns - 1].setHeight(2);
            cells[rows - 3][columns - 1].setMaterial(resources.getMaterial(3));
        }
        else {
            cells[rows - 3][0].model.detachChildAt(0);
            cells[rows - 3][0].model.attachChild(resources.getModel(2).clone());
            cells[rows - 3][0].model.setMaterial(resources.getMaterial(4));
            cells[rows - 3][0].setColor(Color.GREEN);
            cells[rows - 3][0].setHeight(2);
            cells[rows - 3][0].setMaterial(resources.getMaterial(4));

            cells[rows - 2][columns - 1].model.detachChildAt(0);
            cells[rows - 2][columns - 1].model.attachChild(resources.getModel(2).clone());
            cells[rows - 2][columns - 1].model.setMaterial(resources.getMaterial(3));
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
        return NEIGHBOUR_CELLS;
    }
    public Player[] getPlayers() {
        return PLAYERS;
    }
    public static int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public static void setCurrentPlayerIndex(int currentPlayerIndex) {
        Field.currentPlayerIndex = currentPlayerIndex;
    }
}
