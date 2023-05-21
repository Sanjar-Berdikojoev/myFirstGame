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
//    public void createFont() throws IOException, FontFormatException {
//        RenderFont customFont = nifty.createFont("Materials/ChunkFiveRegular.otf");
//
//        Element playerNameText = screen.findElementById("playerNameText");
//        Element towerCountText = screen.findElementById("towerCountText");
//        Element playerColorText = screen.findElementById("playerColorText");
//        Element scoreText = screen.findElementById("playerPointsText");
//
//        playerNameText.getRenderer(TextRenderer.class).setFont(customFont);
//        towerCountText.getRenderer(TextRenderer.class).setFont(customFont);
//        playerColorText.getRenderer(TextRenderer.class).setFont(customFont);
//        scoreText.getRenderer(TextRenderer.class).setFont(customFont);
//    }
    public void updatePlayerInfo() {
        Player currentPlayer = Main.getField().getPlayers()[Field.getCurrentPlayerIndex()];

        String playerName = currentPlayer.getName();
        int playerTowerCount = currentPlayer.getTowers();
        String playerColor = currentPlayer.getColor().toString();
        int playerPoints = currentPlayer.getPoints();

        Element playerNameText = screen.findElementById("playerNameText");
        Element towerCountText = screen.findElementById("towerCountText");
        Element playerColorText = screen.findElementById("playerColorText");
        Element scoreText = screen.findElementById("playerPointsText");

        assert playerNameText != null;
        Objects.requireNonNull(playerNameText.getRenderer(TextRenderer.class)).setText("Player: " + playerName);
        assert towerCountText != null;
        Objects.requireNonNull(towerCountText.getRenderer(TextRenderer.class)).setText("Towers: " + playerTowerCount);
        assert playerColorText != null;
        Objects.requireNonNull(playerColorText.getRenderer(TextRenderer.class)).setText("Color: " + playerColor);
        assert scoreText != null;
        Objects.requireNonNull(scoreText.getRenderer(TextRenderer.class)).setText("Points: " + playerPoints);
    }
    public void changeImage(int phaseIndex, int commandIndex) {

        for(int i = 0; i < Settings.NUMBER_OF_SLOTS; i++) {
            Element currentSlot = screen.findElementById("slot" + (i + 1));
            if(i == commandIndex - 1) {
                NiftyImage image = Main.getResources().getChosenHotBarSlot(phaseIndex, i);
                Objects.requireNonNull(Objects.requireNonNull(currentSlot).getRenderer(ImageRenderer.class)).setImage(image);
            }
            else {
                NiftyImage image = Main.getResources().getHotBarSlot(phaseIndex, i);
                Objects.requireNonNull(Objects.requireNonNull(currentSlot).getRenderer(ImageRenderer.class)).setImage(image);
            }
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
                    Command.selectTower(Field.getCurrentCell());
                }
                else
                    changeImage(1,1);
            }
            case "Button_2" -> {
                Settings.setCurrentCommand(2);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,2);

                    if(Field.getCurrentCell() != null ) {
                        Field.getCurrentCell().model.getChild(0).setMaterial(Field.getCurrentCell().getMaterial());
                        for (Cell cell : Field.getNeighbourCells())
                            cell.model.getChild(0).setMaterial(cell.getMaterial());
                    }
                }
                else
                    changeImage(1,2);
            }
            case "Button_3" -> {
                Settings.setCurrentCommand(3);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,3);
                    Command.deselectCells();
                }
                else if(Settings.getCurrentPhase() == 1)
                    changeImage(1,3);
            }
            case "Button_4" -> {
                Settings.setCurrentCommand(4);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,4);
                    Command.deselectCells();
                }
                else
                    changeImage(1,4);
            }
            default -> {
                Settings.setCurrentCommand(0);
                if (Settings.getCurrentPhase() == 0) {
                    changeImage(0,0);
                    Command.deselectCells();
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
