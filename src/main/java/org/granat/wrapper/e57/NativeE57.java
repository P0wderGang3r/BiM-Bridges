package org.granat.wrapper.e57;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface NativeE57 extends Library {
    NativeE57 instance = Native.load("./lib/NativeE57.so", NativeE57.class);

    boolean canBeOpened(String filePath);

    long getPointsNum(String filePath);

    PointsBounds getPointsBounds(String filePath);

    PointsData getPointsData(String filePath);
}
