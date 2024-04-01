package org.granat.ui.gui.render.gl11.utils;

public class GL11Perspective {
    /**
     *
     * @param coords x, y, z
     * @param status width, height, near, far, fov
     */
    public static void perspective(double[] coords, double[] status) {
        double aspect = status[0] / status[1];

        double dist = (Math.abs(status[2]) + Math.abs(status[3])) / 2;
        double perspectiveFunc = (dist - coords[2]) / (dist) * 1 / (Math.tan(status[4] * Math.PI / (2 * 180)));
        coords[0] *= perspectiveFunc;
        coords[1] *= perspectiveFunc * aspect;
    }
}

/*

public void perspective(double[] coords, int width, int height) {
    double aspect = ((double) width) / ((double) height);

    double dist = (Math.abs(near) + Math.abs(far)) / 2;
    double perspectiveFunc = (dist - coords[2]) / (dist) * 1 / (Math.tan(fov * Math.PI / (2 * 180)));
    coords[0] *= perspectiveFunc;
    coords[1] *= perspectiveFunc * aspect;
}

*/