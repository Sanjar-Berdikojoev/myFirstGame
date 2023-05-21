package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.render.NiftyImage;

public class Resources {
    private final AssetManager assetManager;
    Resources(AssetManager assetManager){
        this.assetManager = assetManager;
    }
    private final Spatial[] MODELS = new Spatial[Settings.MAX_HEIGHT + 1];
    private final Material[] MATERIALS = new Material[Settings.NUMBER_OF_MATERIALS];
    private final NiftyImage[][] HOT_BAR_SLOTS = new NiftyImage[Settings.NUMBER_OF_PHASES][Settings.NUMBER_OF_SLOTS];
    private final NiftyImage[][] CHOSEN_HOT_BAR_SLOTS = new NiftyImage[Settings.NUMBER_OF_PHASES][Settings.NUMBER_OF_SLOTS];
    public void setHotBarSLots() {
        HOT_BAR_SLOTS[0][0] = Main.getNifty().createImage("Models/HotBars/Selector.png", false);
        HOT_BAR_SLOTS[0][1] = Main.getNifty().createImage("Models/HotBars/Empty_Slot.png", false);
        HOT_BAR_SLOTS[0][2] = Main.getNifty().createImage("Models/HotBars/Next_Phase.png", false);
        HOT_BAR_SLOTS[0][3] = Main.getNifty().createImage("Models/HotBars/Resign.png", false);
        HOT_BAR_SLOTS[1][0] = Main.getNifty().createImage("Models/HotBars/Add_Point.png", false);
        HOT_BAR_SLOTS[1][1] = Main.getNifty().createImage("Models/HotBars/Add_Max_Point.png", false);
        HOT_BAR_SLOTS[1][2] = Main.getNifty().createImage("Models/HotBars/Skip.png", false);
        HOT_BAR_SLOTS[1][3] = Main.getNifty().createImage("Models/HotBars/Resign.png", false);

        CHOSEN_HOT_BAR_SLOTS[0][0] = Main.getNifty().createImage("Models/HotBars/Selector(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[0][1] = Main.getNifty().createImage("Models/HotBars/Empty_Slot(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[0][2] = Main.getNifty().createImage("Models/HotBars/Next_Phase(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[0][3] = Main.getNifty().createImage("Models/HotBars/Resign(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[1][0] = Main.getNifty().createImage("Models/HotBars/Add_Point(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[1][1] = Main.getNifty().createImage("Models/HotBars/Add_Max_Point(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[1][2] = Main.getNifty().createImage("Models/HotBars/Skip(chosen).png", false);
        CHOSEN_HOT_BAR_SLOTS[1][3] = Main.getNifty().createImage("Models/HotBars/Resign(chosen).png", false);
    }
    public void setModelsAndMaterials() {
        for (int i = 0; i < 10; i++)
            MATERIALS[i] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        MATERIALS[0].setColor("Color", ColorRGBA.Gray);
        MATERIALS[1].setColor("Color", ColorRGBA.fromRGBA255(150, 0,0,0));
        MATERIALS[2].setColor("Color", ColorRGBA.fromRGBA255(0, 0, 150,0));
        MATERIALS[3].setColor("Color", ColorRGBA.fromRGBA255(180, 180, 0, 0));
        MATERIALS[4].setColor("Color", ColorRGBA.fromRGBA255(0,150,0,0));
        MATERIALS[5].setColor("Color", ColorRGBA.fromRGBA255(255,20,20,0));
        MATERIALS[6].setColor("Color", ColorRGBA.fromRGBA255(20,20,255,0));
        MATERIALS[7].setColor("Color", ColorRGBA.fromRGBA255(255,255,20,0));
        MATERIALS[8].setColor("Color", ColorRGBA.fromRGBA255(20,255,20,0));
        MATERIALS[9].setColor("Color", ColorRGBA.Orange);

        MODELS[0] = assetManager.loadModel("Models/Towers/Cell.j3o").scale(Settings.SCALE_COEFFICIENT, Settings.SCALE_COEFFICIENT, Settings.SCALE_COEFFICIENT);
        for (int i = 1; i < Settings.MAX_HEIGHT + 1; i++){
            MODELS[i] = assetManager.loadModel("Models/Towers/Tower_H" + i + ".j3o").scale(Settings.SCALE_COEFFICIENT, Settings.SCALE_COEFFICIENT, Settings.SCALE_COEFFICIENT);
        }
    }
    public Material getMaterial(int i) {
        return MATERIALS[i];
    }
    public Spatial getModel(int i) {
        return MODELS[i];
    }
    public NiftyImage getHotBarSlot(int i, int j) {
        return HOT_BAR_SLOTS[i][j];
    }
    public NiftyImage getChosenHotBarSlot(int i, int j) {
        return CHOSEN_HOT_BAR_SLOTS[i][j];
    }
}
