package com.mygame;

public class Player {
    private Color color;
    private boolean isActive;
    private int towers;
    private int points;
    public Player(Color color, boolean isActive, int towers, int points) {
        this.color = color;
        this.isActive = isActive;
        this.towers = towers;
        this.points = points;
    }

    public Color getColor() {
        return color;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTowers() {
        return towers;
    }

    public void setTowers(int towers) {
        this.towers = towers;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
