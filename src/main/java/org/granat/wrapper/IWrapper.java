package org.granat.wrapper;

public interface IWrapper {
    boolean canBeOpened();

    long getPointsNum();

    double[] getPointsBounds();

    double[][] getData();
}
