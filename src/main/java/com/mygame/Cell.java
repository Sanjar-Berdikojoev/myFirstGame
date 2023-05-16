package com.mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Cell {
    private int height;
    private Vector3f vector;
    private Color color;
    private Material material;
    private Node model;
    private int i;
    private int j;

    public Cell(int height, Vector3f vector, Color color, Material material, Node model, int i, int j) {
        this.height = height;
        this.vector = vector;
        this.color = color;
        this.material = material;
        this.model = model;
        this.i = i;
        this.j = j;
    }
    public Cell() {

    }
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector3f getVector() {
        return vector;
    }

    public void setVector(Vector3f vector) {
        this.vector = vector;
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

    public Node getModel() {
        return model;
    }

    public void setModel(Node model) {
        this.model = model;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
