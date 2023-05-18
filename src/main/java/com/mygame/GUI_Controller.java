package com.mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;

import java.util.Arrays;
import java.util.Collections;

public class GUI_Controller {
    private final InputManager inputManager;
    private final Nifty nifty;
    GUI_Controller(InputManager inputManager, Nifty nifty){
        this.inputManager = inputManager;
        this.nifty = nifty;
    }
    public void createMappings(){
        inputManager.addMapping("Button_1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Button_2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Button_3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Button_4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Button_0", new KeyTrigger(KeyInput.KEY_0));
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            switch (name) {
                case "Button_1" -> {
                    Settings.setCurrentCommand(1);
                    if (Settings.getCurrentPhase() == 0) {
                        nifty.fromXml("Interface/ControlGui_FirstPhase_1.xml", "inventory");
                        Command.selectTower(Field.getCurrentCell());
                    }
                    else
                        nifty.fromXml("Interface/ControlGui_SecondPhase_1.xml", "inventory");
                }
                case "Button_2" -> {
                    Settings.setCurrentCommand(2);
                    if (Settings.getCurrentPhase() == 0) {
                        nifty.fromXml("Interface/ControlGui_FirstPhase_2.xml", "inventory");

                        if(Field.getCurrentCell() != null ) {
                            Field.getCurrentCell().getModel().getChild(0).setMaterial(Field.getCurrentCell().getMaterial());
                            for (Cell cell : Field.getNeighbourCells())
                                cell.getModel().getChild(0).setMaterial(cell.getMaterial());
                        }
                    }
                    else
                        nifty.fromXml("Interface/ControlGui_SecondPhase_2.xml", "inventory");
                }
                case "Button_3" -> {
                    Settings.setCurrentCommand(3);
                    if (Settings.getCurrentPhase() == 0) {
                        nifty.fromXml("Interface/ControlGui_FirstPhase_3.xml", "inventory");
                        if(Field.getCurrentCell() != null ) {
                            Field.getCurrentCell().getModel().getChild(0).setMaterial(Field.getCurrentCell().getMaterial());
                            for (Cell cell : Field.getNeighbourCells())
                                cell.getModel().getChild(0).setMaterial(cell.getMaterial());
                        }
                    }
                    else if(Settings.getCurrentPhase() == 1) {
                        nifty.fromXml("Interface/ControlGui_SecondPhase_3.xml", "inventory");
                    }
                }
                case "Button_4" -> {
                    Settings.setCurrentCommand(4);
                    if (Settings.getCurrentPhase() == 0) {
                        nifty.fromXml("Interface/ControlGui_FirstPhase_4.xml", "inventory");
                        if(Field.getCurrentCell() != null ) {
                            Field.getCurrentCell().getModel().getChild(0).setMaterial(Field.getCurrentCell().getMaterial());
                            for (Cell cell : Field.getNeighbourCells())
                                cell.getModel().getChild(0).setMaterial(cell.getMaterial());
                        }
                    }
                    else
                        nifty.fromXml("Interface/ControlGui_SecondPhase_4.xml", "inventory");
                }
                default -> {
                    Settings.setCurrentCommand(0);
                    if (Settings.getCurrentPhase() == 0) {
                        nifty.fromXml("Interface/ControlGui_FirstPhase.xml", "inventory");
                        if(Field.getCurrentCell() != null ) {
                            Field.getCurrentCell().getModel().getChild(0).setMaterial(Field.getCurrentCell().getMaterial());
                            for (Cell cell : Field.getNeighbourCells())
                                cell.getModel().getChild(0).setMaterial(cell.getMaterial());
                        }
                    }
                    else
                        nifty.fromXml("Interface/ControlGui_SecondPhase.xml", "inventory");
                }
            }
        }
    };

    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}