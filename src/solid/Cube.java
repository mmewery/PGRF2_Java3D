package solid;

import transforms.Col;
import transforms.Point3D;

import java.awt.*;

public class Cube extends Solid {
    public Cube() {
        color = new Col(0x00ffff);
        vb.add(new Point3D(0.2, 0.2, 0.2)); // v0
        vb.add(new Point3D(1.2, 0.2, 0.2)); // v1
        vb.add(new Point3D(1.2, 1.2, 0.2)); // v2
        vb.add(new Point3D(0.2, 1.2, 0.2)); // v3

        vb.add(new Point3D(0.2, 0.2, 1.2)); // v4
        vb.add(new Point3D(1.2, 0.2, 1.2)); // v5
        vb.add(new Point3D(1.2, 1.2, 1.2)); // v6
        vb.add(new Point3D(0.2, 1.2, 1.2)); // v7


        addIndices(0, 1);
        addIndices(1, 2);
        addIndices(2, 3);
        addIndices(3, 0);

        addIndices(4, 5);
        addIndices(5, 6);
        addIndices(6, 7);
        addIndices(7, 4);

        addIndices(0, 4);
        addIndices(1, 5);
        addIndices(2, 6);
        addIndices(3, 7);
    }
}
