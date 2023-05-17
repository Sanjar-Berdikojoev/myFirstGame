package com.mygame;

import com.jme3.input.CameraInput;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class Settings {
    public final static int MAX_HEIGHT = 8;
    //public final static int MIN_HEIGHT = 1;
    public final static float SCALE_COEFFICIENT = 0.05f;
    public final static int NUMBER_OF_MATERIALS = 10;
    //public final static int NUMBER_OF_PHASES = 2;
    public final static int ROWS = 21;
    public final static int COLUMNS = 21;
    private static long lastWPressTime = 0;
    private static int inactivePlayers = 0;
    private static int numberOfPlayers = 4;
    private static int currentCommand = 0;
    private static int currentPhase = 0;
    public static void setMyConfigurationsForCamera(InputManager inputManager, FlyByCamera flyCam){
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

    public static void setLimitForCamera(Camera camera){
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

    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public static long getLastWPressTime() {
        return lastWPressTime;
    }
    public static void setLastWPressTime(long lastWPressTime) {
        Settings.lastWPressTime = lastWPressTime;
    }
    public static int getCurrentCommand() {
        return currentCommand;
    }
    public static void setCurrentCommand(int currentCommand) {
        Settings.currentCommand = currentCommand;
    }
    public static int getCurrentPhase() {
        return currentPhase;
    }
    public static void setCurrentPhase(int currentPhase) {
        Settings.currentPhase = currentPhase;
    }
    public static int getInactivePlayers() {
        return inactivePlayers;
    }
    public static void setInactivePlayers(int inactivePlayers) {
        Settings.inactivePlayers = inactivePlayers;
    }
}
