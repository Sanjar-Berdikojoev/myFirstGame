package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.*;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.util.Objects;

public class Main extends SimpleApplication {
    private static Resources resources;
    private Vector2f midDisplayLocation;
    private static Nifty nifty;
    private static Field field;
    private static Ray cursorRay;
    private static GUIController controller;
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public AssetManager getAssetManager() {
        return super.getAssetManager();
    }

    @Override
    public void simpleInitApp() {

        NiftyJmeDisplay niftyJmeDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyJmeDisplay.getNifty();
        nifty.fromXml("Interface/ControlGui.xml", "inventory");
        guiViewPort.addProcessor(niftyJmeDisplay);
        Screen screen = nifty.getCurrentScreen();
        controller = new GUIController(inputManager);
        nifty.registerScreenController(controller);
        controller.bind(nifty, Objects.requireNonNull(screen));

        AudioNode audioNode = new AudioNode(assetManager, "Sounds/StarSummer.ogg", AudioData.DataType.Buffer);
        audioNode.setPositional(false);
        audioNode.setLooping(true);
        audioNode.setVolume(0.5f);
        rootNode.attachChild(audioNode);
        inputManager.addMapping("StartMusic", new KeyTrigger(KeyInput.KEY_M));
        final int[] counter = {0};

        inputManager.addListener((ActionListener) (String name, boolean isPressed, float tpf) -> {
            if ((name.equals("StartMusic") && isPressed && counter[0] == 0)) {
                audioNode.play();
                counter[0]++;
                return;
            } else if(name.equals("StartMusic") && isPressed){
                audioNode.stop();
                counter[0] = 0;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - Settings.getLastWPressTime() < 500)
                flyCam.setMoveSpeed(6.0f);
            Settings.setLastWPressTime(currentTime);
        }, "StartMusic");

        resources = new Resources(assetManager);
        resources.setModelsAndMaterials();
        resources.setHotBarSLots();
        midDisplayLocation = new Vector2f(settings.getWidth() / 2f , settings.getHeight() / 2f);
        field = new Field(assetManager, rootNode, Settings.ROWS, Settings.COLUMNS);
        field.createField();
        field.setPlayers(Settings.getNumberOfPlayers());
        controller.updatePlayerInfo();
        controller.createMappings();
        Settings.setMyConfigurationsForCamera(inputManager, flyCam);

        CustomMouseListener customMouseListener = new CustomMouseListener(inputManager);
        customMouseListener.addMouseListener();
        inputManager.addListener(controller.getActionListener(), "Button_1", "Button_2", "Button_3", "Button_4", "Button_0");
        customMouseListener.initInput();

        setDisplayStatView(false);
        setDisplayFps(true);
        flyCam.setZoomSpeed(0);
    }
    @Override
    public void simpleUpdate(float tpf) {
        controller.updatePlayerInfo();
        Settings.setLimitForCamera(cam);
        Vector3f rayBegin = new Vector3f(cam.getWorldCoordinates(midDisplayLocation, 0.0f));
        cursorRay = new Ray(rayBegin, cam.getDirection());
        field.setTextsOverTowers(cam);
    }
    @Override
    public void simpleRender(RenderManager rm) {
    }
    public static Resources getResources() {
        return resources;
    }
    public static Field getField() {
        return field;
    }
    public static Ray getCursorRay() {
        return cursorRay;
    }
    public static Nifty getNifty() {
        return nifty;
    }
    public static GUIController getController() {
        return controller;
    }
}