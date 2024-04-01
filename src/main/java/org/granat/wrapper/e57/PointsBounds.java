package org.granat.wrapper.e57;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({"minX","maxX","minY","maxY","minZ","maxZ"})
public class PointsBounds extends Structure {
    public double minX;
    public double maxX;
    public double minY;
    public double maxY;
    public double minZ;
    public double maxZ;
}
