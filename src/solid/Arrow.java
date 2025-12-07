package solid;

import transforms.Point3D;

public class Arrow extends Solid{

    public Arrow() {
        // Naplnit vb
        vb.add(new Point3D(0, 0, 0)); // v0
        vb.add(new Point3D(0.4, 0, 0)); // v1
        vb.add(new Point3D(0.4, -0.1, 0)); // v2
        vb.add(new Point3D(0.5, 0, 0)); // v3
        vb.add(new Point3D(0.4, 0.1, 0)); // v4

        // naplnit ib
        addIndices(0, 1);
        addIndices(2, 3);
        addIndices(3, 4);
        addIndices(4, 2);
    }
}
