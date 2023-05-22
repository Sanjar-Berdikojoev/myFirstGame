package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
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
        setWalls();
    }
    public void setTextsOverTowers(Camera camera) {

        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText[][] texts = new BitmapText[rows][columns];

        for (Spatial child : rootNode.getChildren()) {
            if (child instanceof BitmapText)
                rootNode.detachChild(child);
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

                    switch(currentCell.getColor()) {
                        case RED -> texts[i][j].setColor(ColorRGBA.Red);
                        case BLUE -> texts[i][j].setColor(ColorRGBA.Blue);
                        case YELLOW -> texts[i][j].setColor(ColorRGBA.Yellow);
                        case GREEN -> texts[i][j].setColor(ColorRGBA.Green);
                    }

                    rootNode.attachChild(texts[i][j]);
                }
            }
        }
    }
    private void setFloor() {

        Quad[][] floor = new Quad[Settings.ROWS - 1][Settings.COLUMNS - 1];
        Texture[] floorTextures = new Texture[6];
        Material[] materials = new Material[6];

        for (int i = 0; i < 6; i++) {
            floorTextures[i] = assetManager.loadTexture("Models/Floor/Floor_" + (i + 1) + ".png");
            materials[i] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            materials[i].setTexture("ColorMap", floorTextures[i]);
        }

        for (int i = 0; i < Settings.ROWS - 1; i += 2) {
            for (int j = 0; j < Settings.COLUMNS - 1; j++) {

                floor[i][j] = new Quad(3.7f, 3.7f);
                Geometry floorGeom = new Geometry("Floor", floor[i][j]);
                if(Settings.ROWS % 2 != 0) {
                    if (j == 0)
                        floorGeom.setMaterial(materials[0]);
                    else if (j == Settings.COLUMNS - 2)
                        floorGeom.setMaterial(materials[2]);
                    else
                        floorGeom.setMaterial(materials[1]);
                }
                else {
                    if(i != Settings.ROWS - 2) {
                        if (j == 0)
                            floorGeom.setMaterial(materials[0]);
                        else if (j == Settings.COLUMNS - 2)
                            floorGeom.setMaterial(materials[2]);
                        else
                            floorGeom.setMaterial(materials[1]);
                    }
                    else {
                        if (j == 0)
                            floorGeom.setMaterial(materials[3]);
                        else if (j == Settings.COLUMNS - 2)
                            floorGeom.setMaterial(materials[4]);
                        else
                            floorGeom.setMaterial(materials[5]);
                    }
                }
                floorGeom.rotate(-FastMath.HALF_PI, 0,0);
                floorGeom.setLocalTranslation(cells[i][j].vector.x + 1.05f, 0.92f - (i * 0.0001f) + (j * 0.0001f), cells[i][j].vector.z + 4.4f);
                rootNode.attachChild(floorGeom);
            }
        }
    }
    private void setWalls() {

        Quad[] walls = new Quad[4];
        Material[] materials = new Material[4];
        Geometry[] geometries = new Geometry[4];

        Texture redWall = assetManager.loadTexture("Models/Walls/Red_Wall.png");
        Texture blueWall = assetManager.loadTexture("Models/Walls/Blue_Wall.png");
        Texture yellowWall = assetManager.loadTexture("Models/Walls/Yellow_Wall.png");
        Texture greenWall = assetManager.loadTexture("Models/Walls/Green_Wall.png");

        float wallWidth = 2.4f * cells[rows - 1][0].vector.z;
        float wallHeight = 1.35f * cells[rows - 1][0].vector.z;
        float distanceFromField = 8.0f + rows + columns;

        for (int i = 0; i < 4; i++)
            materials[i] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        materials[0].setTexture("ColorMap", redWall);
        materials[1].setTexture("ColorMap", blueWall);
        materials[2].setTexture("ColorMap", yellowWall);
        materials[3].setTexture("ColorMap", greenWall);

        for (int i = 0; i < 4; i++) {
            walls[i] = new Quad(wallWidth, wallHeight);
            geometries[i] = new Geometry("Wall_" + i, walls[i]);
            geometries[i].setMaterial(materials[i]);
        }

        geometries[0].setLocalTranslation(cells[0][0].vector.x - 0.18f * wallWidth, 0.0f, cells[0][0].vector.z - distanceFromField);
        geometries[1].setLocalTranslation(cells[0][0].vector.x - distanceFromField, 0.0f, cells[0][columns - 1].vector.z + 0.73f * wallWidth);
        geometries[2].setLocalTranslation(cells[0][columns - 1].vector.x + 0.36f * wallWidth, 0.0f, cells[rows - 1 ][0].vector.z + 1.1f * distanceFromField);
        geometries[3].setLocalTranslation(cells[0][columns - 1].vector.x + 1.1f * distanceFromField, 0.0f, cells[0][columns - 1].vector.z - 0.35f * 0.73f * wallWidth);

        geometries[1].rotate(0.0f, FastMath.HALF_PI, 0.0f);
        geometries[2].rotate(0.0f, FastMath.PI, 0.0f);
        geometries[3].rotate(0.0f, -FastMath.HALF_PI, 0.0f);

        rootNode.attachChild(geometries[0]);
        rootNode.attachChild(geometries[1]);
        rootNode.attachChild(geometries[2]);
        rootNode.attachChild(geometries[3]);
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
