package solid;

import transforms.Cubic;
import transforms.Mat4;
import transforms.Point3D;

public class Curve extends Solid {


    public Curve(Mat4 type, Point3D p1, Point3D p2, Point3D p3, Point3D p4, int steps) {
        Cubic cubic = new Cubic(type, p1, p2, p3, p4);

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            Point3D point = cubic.compute(t);
            vb.add(point);

            if (i > 0) {
                addIndices(i - 1, i);
            }
        }
    }
}