package com.mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Cell {
    private int height;
    public final Vector3f vector;
    private Color color;
    public Material material;
    public final Node model;
    public final int i;
    public final int j;
    public Cell(int height, Vector3f vector, Color color, Material material, Node model, int i, int j) {
        this.height = height;
        this.vector = vector;
        this.color = color;
        this.material = material;
        this.model = model;
        this.i = i;
        this.j = j;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
}
