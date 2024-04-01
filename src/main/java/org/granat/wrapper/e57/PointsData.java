package org.granat.wrapper.e57;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

@FieldOrder({"coordsX","coordsY","coordsZ","intensity"})
public class PointsData extends Structure {
    public Pointer coordsX;
    public Pointer coordsY;
    public Pointer coordsZ;
    public Pointer intensity;
}
