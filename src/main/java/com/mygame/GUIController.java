package com.mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class GUIController implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private final InputManager inputManager;
    GUIController(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    @Override
    public void bind(@NotNull Nifty nifty, @NotNull Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }
    @Override
    public void onStartScreen() {

    }
    @Override
    public void onEndScreen() {

    }

    public void updatePlayerInfo() {
        Player currentPlayer = Main.getField().getPlayers()[Field.getCurrentPlayerIndex()];

        String playerName = currentPlayer.getName();
        int playerTowerCount = currentPlayer.getTowers();
        String playerColor = currentPlayer.getColor().toString();
        int playerPoints = currentPlayer.getPoints();

        Element playerNameText = screen.findElementById("playerNameText");
        Element towerCountText = screen.findElementById("towerCountText");
        Element playerColorText = screen.findElementById("playerColorText");
        Element playerPointsText = screen.findElementById("playerPointsText");

        switch(currentPlayer.getColor()) {
            case RED -> {
                assert playerNameText != null;
                Objects.requireNonNull(playerNameText.getRenderer(TextRenderer.class)).setColor(new Color("#FF1414"));
                assert towerCountText != null;
                Objects.requireNonNull(towerCountText.getRenderer(TextRenderer.class)).setColor(new Color("#FF1414"));
                assert playerColorText != null;
                Objects.requireNonNull(playerColorText.getRenderer(TextRenderer.class)).setColor(new Color("#FF1414"));
                assert playerPointsText != null;
                Objects.requireNonNull(playerPointsText.getRenderer(TextRenderer.class)).setColor(new Color("#FF1414"));
            }
            case BLUE -> {
                assert playerNameText != null;
                Objects.requireNonNull(playerNameText.getRenderer(TextRenderer.class)).setColor(new Color("#1414FF"));
                assert towerCountText != null;
                Objects.requireNonNull(towerCountText.getRenderer(TextRenderer.class)).setColor(new Color("#1414FF"));
                assert playerColorText != null;
                Objects.requireNonNull(playerColorText.getRenderer(TextRenderer.class)).setColor(new Color("#1414FF"));
                assert playerPointsText != null;
                Objects.requireNonNull(playerPointsText.getRenderer(TextRenderer.class)).setColor(new Color("#1414FF"));
            }
            case YELLOW -> {
                assert playerNameText != null;
                Objects.requireNonNull(playerNameText.getRenderer(TextRenderer.class)).setColor(new Color("#FFFF14"));
                assert towerCountText != null;
                Objects.requireNonNull(towerCountText.getRenderer(TextRenderer.class)).setColor(new Color("#FFFF14"));
                assert playerColorText != null;
                Objects.requireNonNull(playerColorText.getRenderer(TextRenderer.class)).setColor(new Color("#FFFF14"));
                assert playerPointsText != null;
                Objects.requireNonNull(playerPointsText.getRenderer(TextRenderer.class)).setColor(new Color("#FFFF14"));
            }
            case GREEN -> {
                assert playerNameText != null;
                Objects.requireNonNull(playerNameText.getRenderer(TextRenderer.class)).setColor(new Color("#14FF14"));
                assert towerCountText != null;
                Objects.requireNonNull(towerCountText.getRenderer(TextRenderer.class)).setColor(new Color("#14FF14"));
                assert playerColorText != null;
                Objects.requireNonNull(playerColorText.getRenderer(TextRenderer.class)).setColor(new Color("#14FF14"));
                assert playerPointsText != null;
                Objects.requireNonNull(playerPointsText.getRenderer(TextRenderer.class)).setColor(new Color("#14FF14"));
            }
        }

        assert playerNameText != null;
        Objects.requireNonNull(playerNameText.getRenderer(TextRenderer.class)).setText("Player: " + playerName);
        assert towerCountText != null;
        Objects.requireNonNull(towerCountText.getRenderer(TextRenderer.class)).setText("Towers: " + playerTowerCount);
        assert playerColorText != null;
        Objects.requireNonNull(playerColorText.getRenderer(TextRenderer.class)).setText("Color: " + playerColor);
        assert playerPointsText != null;
        Objects.requireNonNull(playerPointsText.getRenderer(TextRenderer.class)).setText("Points: " + playerPoints);
    }
    public void changeImage(int phaseIndex, int commandIndex) {

        for(int i = 0; i < Settings.NUMBER_OF_SLOTS; i++) {
            Element currentSlot = screen.findElementById("slot" + (i + 1));
            NiftyImage image;

            if(i == commandIndex - 1)
                image = Main.getResources().getChosenHotBarSlot(phaseIndex, i);
            else
                image = Main.getResources().getHotBarSlot(phaseIndex, i);

            Objects.requireNonNull(Objects.requireNonNull(currentSlot).getRenderer(ImageRenderer.class)).setImage(image);
        }
    }
    public void createMappings(){
        inputManager.addMapping("Button_1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Button_2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Button_3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Button_4", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Button_0", new KeyTrigger(KeyInput.KEY_0));
    }
    private final ActionListener actionListener = (name, isPressed, tpf) -> {
        switch (name) {
            case "Button_1" -> {
                Settings.setCurrentCommand(1);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,1);
                    Commands.highlightTower(Field.getCurrentCell());
                }
                else
                    changeImage(1,1);
            }
            case "Button_2" -> {
                Settings.setCurrentCommand(2);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,2);
                    Commands.lowlightCells();
                }
                else
                    changeImage(1,2);
            }
            case "Button_3" -> {
                Settings.setCurrentCommand(3);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,3);
                    Commands.lowlightCells();
                }
                else if(Settings.getCurrentPhase() == 1)
                    changeImage(1,3);
            }
            case "Button_4" -> {
                Settings.setCurrentCommand(4);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,4);
                    Commands.lowlightCells();
                }
                else
                    changeImage(1,4);
            }
            default -> {
                Settings.setCurrentCommand(0);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,0);
                    Commands.lowlightCells();
                }
                else
                    changeImage(1,0);
            }
        }
    };
    public ActionListener getActionListener() {
        return actionListener;
    }
}
