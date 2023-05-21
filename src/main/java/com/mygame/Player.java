package com.mygame;

public class Player {
    private String name;
    private final Color color;
    private boolean isActive;
    private int towers;
    private int points;
    public Player(String name, Color color, boolean isActive, int towers, int points) {
        this.name = name;
        this.color = color;
        this.isActive = isActive;
        this.towers = towers;
        this.points = points;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Color getColor() {
        return color;
    }
    public boolean isActive() {
        return !isActive;
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
