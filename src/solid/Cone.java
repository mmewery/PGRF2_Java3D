package solid;

import transforms.Point3D;

public class Cone extends Solid {
    double height = 1.2;
    double radius = 0.5;
    int segments = 16;
    public Cone() {
        vb.add(new Point3D(2, radius, height));

        vb.add(new Point3D(2, radius, 0));

        for (int i = 0; i < segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            vb.add(new Point3D(x + 2, y + radius, 0));
        }

        for (int i = 0; i < segments; i++) {
            int current = 2 + i;
            int next = 2 + (i + 1) % segments;

            addIndices(0, current);

            addIndices(current, next);
        }
    }
}
