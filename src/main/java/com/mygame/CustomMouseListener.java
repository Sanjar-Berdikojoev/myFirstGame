package com.mygame;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.*;

public class CustomMouseListener implements ActionListener{

    private final InputManager inputManager;
    public CustomMouseListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    private int counter = 0;
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("MouseWheelUp")) {
            if (isPressed && counter == Settings.MOUSE_WHEEL_SENSE) {
                switchCommandForward();
                counter = 0;
            }
            counter++;
        } else if (name.equals("MouseWheelDown")) {
            if (isPressed && counter == Settings.MOUSE_WHEEL_SENSE) {
                switchCommandBackward();
                counter = 0;
            }
            counter++;
        }
    }
    public void initInput() {
        inputManager.addMapping("MouseWheelUp", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("MouseWheelDown", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(this, "MouseWheelUp", "MouseWheelDown");
    }
    private void switchCommandForward() {
        Settings.setCurrentCommand(Settings.getCurrentCommand() + 1);

        if(Settings.getCurrentCommand() > 4)
            Settings.setCurrentCommand(1);

        if(Settings.getCurrentPhase() == 0) {
            Main.getController().changeImage(0, Settings.getCurrentCommand());
            if(Settings.getCurrentCommand() == 1)
                Commands.highlightTower(Field.getCurrentCell());
            else
                Commands.lowlightCells();
        }
        else
            Main.getController().changeImage(1,Settings.getCurrentCommand());
    }
    private void switchCommandBackward() {
        Settings.setCurrentCommand(Settings.getCurrentCommand() - 1);

        if(Settings.getCurrentCommand() < 1)
            Settings.setCurrentCommand(4);

        if(Settings.getCurrentPhase() == 0) {
            Main.getController().changeImage(0, Settings.getCurrentCommand());
            if(Settings.getCurrentCommand() == 1)
                Commands.highlightTower(Field.getCurrentCell());
            else
                Commands.lowlightCells();
        }
        else
            Main.getController().changeImage(1,Settings.getCurrentCommand());
    }
    public void addMouseListener() {

        inputManager.addMapping("MouseWheelUp", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("MouseWheelDown", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("RMB", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addRawInputListener(new RawInputListener() {
            @Override
            public void beginInput() {

            }
            @Override
            public void endInput() {

            }
            @Override
            public void onJoyAxisEvent(JoyAxisEvent evt) {

            }
            @Override
            public void onJoyButtonEvent(JoyButtonEvent evt) {

            }
            @Override
            public void onMouseMotionEvent(MouseMotionEvent evt) {

            }
            @Override
            public void onMouseButtonEvent(MouseButtonEvent evt) {
                if (evt.isPressed()) {

                    if(Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].isActive())
                        return;

                    if (evt.getButtonIndex() == MouseInput.BUTTON_LEFT) {
                        switch (Settings.getCurrentCommand()) {
                            case 1 -> {
                                if (Settings.getCurrentPhase() == 0)
                                    Commands.setNewTower(Field.getNeighbourCells(), Field.getCurrentCell());
                                else
                                    Commands.addPointForTower(Commands.getTower());
                            }
                            case 2 -> {
                                if (Settings.getCurrentPhase() == 1)
                                    Commands.addMaxPointsForTower(Commands.getTower());
                            }
                        }
                    } else if (evt.getButtonIndex() == MouseInput.BUTTON_RIGHT) {
                        switch (Settings.getCurrentCommand()) {
                            case 1 -> {
                                if (Settings.getCurrentPhase() == 0) {
                                    Commands.selectTower();
                                    Commands.searchNeighbourCells(Field.getCurrentCell());
                                }
                            }
                            case 3 -> {
                                int playerIndex = Field.getCurrentPlayerIndex();
                                int playerPoints = Main.getField().getPlayers()[playerIndex].getPoints();
                                int numOfTowers = Main.getField().getPlayers()[playerIndex].getTowers();

                                if (Settings.getCurrentPhase() == 0) {
                                    Commands.lowlightCells();

                                    Main.getController().changeImage(1,0);
                                    Main.getField().getPlayers()[playerIndex].setPoints(playerPoints + numOfTowers);
                                    Settings.setCurrentCommand(0);
                                    Settings.setCurrentPhase(1);
                                }
                                else {
                                    if(Main.getField().getPlayers()[playerIndex].getPoints() > 0)
                                        Commands.distributePoints();
                                    Commands.passMove();
                                    Commands.checkIfNoMoves();
                                }
                            }
                            case 4 -> {

                                if(Settings.getInactivePlayers() > 2)
                                    return;

                                Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].setActive(false);
                                Settings.setInactivePlayers(Settings.getInactivePlayers() + 1);

                                while(Main.getField().getPlayers()[Field.getCurrentPlayerIndex()].isActive())
                                    Field.setCurrentPlayerIndex((Field.getCurrentPlayerIndex() + 1) % Settings.getNumberOfPlayers());
                                Main.getController().changeImage(0,0);
                                Settings.setCurrentPhase(0);
                                Settings.setCurrentCommand(0);
                            }
                        }
                    }
                }
            }
            @Override
            public void onKeyEvent(KeyInputEvent evt) {

            }

            @Override
            public void onTouchEvent(TouchEvent evt) {

            }
        });
    }
}
