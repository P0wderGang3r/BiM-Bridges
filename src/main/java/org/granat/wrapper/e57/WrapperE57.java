package org.granat.wrapper.e57;

import com.sun.jna.Native;
import lombok.Getter;
import lombok.Setter;
import org.granat.Globals;
import org.granat.wrapper.IWrapper;

public class WrapperE57 implements IWrapper {
    @Getter
    @Setter
    String filePath;

    public WrapperE57(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean canBeOpened() {
        return NativeE57.instance.canBeOpened(filePath);
    }

    @Override
    public long getPointsNum() {
        return NativeE57.instance.getPointsNum(filePath);
    }

    @Override
    public double[] getPointsBounds() {
        PointsBounds pointsBounds = NativeE57.instance.getPointsBounds(filePath);
        double[] bounds = new double[] {
                pointsBounds.minX,
                pointsBounds.maxX,
                pointsBounds.minY,
                pointsBounds.maxY,
                pointsBounds.minZ,
                pointsBounds.maxZ
        };
        return bounds;
    }

    @Override
    public double[][] getData() {
        long pointsNum = getPointsNum();
        PointsData pointsData = NativeE57.instance.getPointsData(filePath);
        double[][] data = new double[(int) pointsNum / Globals.wrapperInputSkip][4];
        for (int index = 0; index < (int) pointsNum / Globals.wrapperInputSkip; index += Globals.wrapperInputSkip) {
            data[index ][0] = pointsData.coordsX.getDouble((long) (index * Globals.wrapperInputSkip) * Native.getNativeSize(Double.TYPE));
            data[index][1] = pointsData.coordsY.getDouble((long) (index * Globals.wrapperInputSkip) * Native.getNativeSize(Double.TYPE));
            data[index][2] = pointsData.coordsZ.getDouble((long) (index * Globals.wrapperInputSkip) * Native.getNativeSize(Double.TYPE));
            data[index][3] = pointsData.intensity.getDouble((long) (index * Globals.wrapperInputSkip) * Native.getNativeSize(Double.TYPE));
        }
        return data;
    }
}
