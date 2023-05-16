package com.mygame;

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
