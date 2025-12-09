package solid;

import transforms.Col;
import transforms.Point3D;

public class Cube extends Solid {
    public Cube() {
        color = new Col(0x00ffff); // Cyan

        vb.add(new Point3D(-0.5, -0.5, -0.5)); // 0
        vb.add(new Point3D( 0.5, -0.5, -0.5)); // 1
        vb.add(new Point3D( 0.5,  0.5, -0.5)); // 2
        vb.add(new Point3D(-0.5,  0.5, -0.5)); // 3

        vb.add(new Point3D(-0.5, -0.5,  0.5)); // 4
        vb.add(new Point3D( 0.5, -0.5,  0.5)); // 5
        vb.add(new Point3D( 0.5,  0.5,  0.5)); // 6
        vb.add(new Point3D(-0.5,  0.5,  0.5)); // 7

        addIndices(0, 1); addIndices(1, 2); addIndices(2, 3); addIndices(3, 0);
        addIndices(4, 5); addIndices(5, 6); addIndices(6, 7); addIndices(7, 4);
        addIndices(0, 4); addIndices(1, 5); addIndices(2, 6); addIndices(3, 7);
    }
}