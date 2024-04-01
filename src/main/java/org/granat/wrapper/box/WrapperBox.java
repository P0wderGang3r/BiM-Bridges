package org.granat.wrapper.box;

import org.granat.wrapper.IWrapper;

public class WrapperBox implements IWrapper {
    @Override
    public boolean canBeOpened() {
        return true;
    }

    @Override
    public long getPointsNum() {
        return 8;
    }

    @Override
    public double[] getPointsBounds() {
        return new double[] {
                1, 1,
                1, 1,
                1, 1
        };
    }

    @Override
    public double[][] getData() {
        double[][] box = new double[8][4];

        for (int coordX = 0; coordX <= 1; coordX++) {
            for (int coordY = 0; coordY <= 1; coordY++) {
                for (int coordZ = 0; coordZ <= 1; coordZ++) {
                    double[] lBox = box[coordX * 4 + coordY * 2 + coordZ];
                    lBox[0] = (coordX * 2 - 1) * 0.5;
                    lBox[1] = (coordY * 2 - 1) * 0.5;
                    lBox[2] = (coordZ * 2 - 1) * 0.5;
                    lBox[3] = 1.0;
                }
            }
        }

        return box;
    }
}
