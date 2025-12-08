package solid;

import transforms.Cubic;
import transforms.Point3D;

public class Curve extends Solid{
    Point3D p1 = new Point3D(3.8,1,0);
    Point3D p2 = new Point3D(4,0,0.3);
    Point3D p3 = new Point3D(4.2,0,0.6);
    Point3D p4 = new Point3D(4.4,1,1);
    int segments = 50;

    public Curve() {
            Cubic cubic = new Cubic(Cubic.BEZIER, p1, p2, p3, p4);

            for (int i = 0; i <= segments; i++) {
                double t = (double) i / segments;
                Point3D point = cubic.compute(t);
                vb.add(point);

                if (i > 0) {
                    addIndices(i - 1, i);
                }
            }
    }
}
