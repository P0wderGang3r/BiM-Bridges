package org.granat.ui.gui.render.gl11.utils;

public class GL11Position {
    /**
     *
     * @param point x, y, z
     * @param position addX, addY, addZ
     */
    public static void position(double[] point, double[] position) {
            point[0] += position[0];
            point[1] += position[1];
            point[2] += position[2];
    }
}
