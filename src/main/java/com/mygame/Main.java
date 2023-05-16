package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private static Resources resources;
    private Vector2f midDisplayLocation;
    private static Nifty nifty;
    private Node guiNode;
    private static Field field;
    private static Ray cursorRay;
    private GUI_Controller guiController;
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
        nifty.fromXml("Interface/ControlGui_FirstPhase.xml", "inventory");
        guiViewPort.addProcessor(niftyJmeDisplay);
        guiNode = new Node("guiNode");
        guiNode.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.setCullHint(Spatial.CullHint.Never);

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

        MyMouseListener myMouseListener = new MyMouseListener(inputManager);
        myMouseListener.addMouseListener();

        guiController = new GUI_Controller(inputManager, nifty);
        guiController.createMappings();
        inputManager.addListener(guiController.getActionListener(), "Button_1", "Button_2", "Button_3", "Button_4", "Button_0");

        viewPort.setBackgroundColor(new ColorRGBA(0.1f, 0.2f, 0.3f, 1.0f));
        resources = new Resources(assetManager);
        resources.setModelsAndMaterials();
        midDisplayLocation = new Vector2f(settings.getWidth() / 2f , settings.getHeight() / 2f);
        field = new Field(assetManager, rootNode, Settings.ROWS, Settings.COLUMNS);
        setMyConfigurationsForCamera(inputManager);
        field.createField();
        field.setPlayers(Settings.getNumberOfPlayers());
    }
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        setLimitForCamera(cam);
        Vector3f rayBegin = new Vector3f(cam.getWorldCoordinates(midDisplayLocation, 0.0f));
        cursorRay = new Ray(rayBegin, cam.getDirection());
        /*if(Settings.getCurrentPhase() == 1)
            Command.selectTowerForPhase2();*/
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    private void setMyConfigurationsForCamera(InputManager inputManager){
        inputManager.addMapping(CameraInput.FLYCAM_RISE, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(CameraInput.FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Run", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener((ActionListener) (String name, boolean isPressed, float tpf) -> {
            if (!(name.equals("Run") && isPressed)) {
                flyCam.setMoveSpeed(3.0f);
                return;
            }
            long currentTime = System.currentTimeMillis();
            if (currentTime - Settings.getLastWPressTime() < 500)
                flyCam.setMoveSpeed(6.0f);
            Settings.setLastWPressTime(currentTime);
        }, "Run");
    }

    private void setLimitForCamera(Camera camera){
        Vector3f cameraLocation = camera.getLocation();

        float groundHeight = 4.5f;
        if(cameraLocation.y < groundHeight){
            cameraLocation.y = groundHeight;
            camera.setLocation(cameraLocation);
        }

        float ceilingHeight = 20f;
        if(cameraLocation.y > ceilingHeight){
            cameraLocation.y = ceilingHeight;
            camera.setLocation(cameraLocation);
        }
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
}