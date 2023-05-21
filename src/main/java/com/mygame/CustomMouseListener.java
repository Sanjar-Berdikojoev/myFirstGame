package com.mygame;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.*;

public class CustomMouseListener {
    private final InputManager inputManager;
    public CustomMouseListener(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    public void addMouseListener() {
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
                                    Command.setNewTower(Field.getNeighbourCells(), Field.getCurrentCell());
                                else
                                    Command.addPointForTower(Command.getTower());
                            }
                            case 2 -> {
                                if (Settings.getCurrentPhase() == 1)
                                    Command.addMaxPointsForTower(Command.getTower());
                            }
                        }
                    } else if (evt.getButtonIndex() == MouseInput.BUTTON_RIGHT) {
                        switch (Settings.getCurrentCommand()) {
                            case 1 -> {
                                if (Settings.getCurrentPhase() == 0) {
                                    Command.onRightMouseButtonClick();
                                    Command.searchNeighbourCells(Field.getCurrentCell());
                                }
                            }
                            case 3 -> {
                                int playerIndex = Field.getCurrentPlayerIndex();
                                int playerPoints = Main.getField().getPlayers()[playerIndex].getPoints();
                                int numOfTowers = Main.getField().getPlayers()[playerIndex].getTowers();

                                if (Settings.getCurrentPhase() == 0) {
                                    Command.deselectCells();

                                    Main.getController().changeImage(1,0);
                                    Main.getField().getPlayers()[playerIndex].setPoints(playerPoints + numOfTowers);
                                    Settings.setCurrentCommand(5);
                                    Settings.setCurrentPhase(1);
                                }
                                else {
                                    if(Main.getField().getPlayers()[playerIndex].getPoints() > 0)
                                        Command.distributePoints();
                                    Command.passMove();
                                    Command.checkIfNoMoves();
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
                                Settings.setCurrentCommand(5);
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
