package org.granat.ui.gui.render.gl11.functions;

public class Position {

    public double[] positionDot(double[] xyzPrev, double[] position) {
        return new double[]{
                xyzPrev[0] + position[0],
                xyzPrev[1] + position[1],
                xyzPrev[2] + position[2]
        };
    }
}
