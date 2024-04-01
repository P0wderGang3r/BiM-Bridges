package org.granat.scene.utils;

public class Position {
    public static void position(double[] point, double[] position) {
            point[0] += position[0];
            point[1] += position[1];
            point[2] += position[2];
    }
}
