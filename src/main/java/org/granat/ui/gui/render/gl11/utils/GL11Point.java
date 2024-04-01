package org.granat.ui.gui.render.gl11.utils;

public class GL11Point {

    double[][] coords = new double[3][];

    public GL11Point() {
        coords[0] = new double[]{0.01, 0.0, 0.0};
        coords[1] = new double[]{0.0, 0.02, 0.0};
        coords[2] = new double[]{-0.01, 0.0, 0.0};
    }

    //?--------------------------------------------------------------------------------------------СЛЕДУЮЩИЙ ТРЕУГОЛЬНИК

    public double[][] getTriangle() {
        return coords;
    }
}
