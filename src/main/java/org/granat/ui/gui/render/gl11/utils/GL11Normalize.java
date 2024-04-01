package org.granat.ui.gui.render.gl11.utils;

public class GL11Normalize {
    /**
     *
     * @param coords x, y, z
     * @param status 1/max, 1/max, 1/max
     */
    public static void normalize(double[] coords, double[] status) {
        coords[0] = coords[0] * status[0];
        coords[1] = coords[1] * status[0];
        coords[2] = coords[2] * status[0];
    }
}
