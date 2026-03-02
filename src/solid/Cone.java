package solid;

import transforms.Point3D;

public class Cone extends SimpleSolid {
    public Cone() {
        double height = 1.5;
        double radius = 0.5;
        int segments = 20;

        vb.add(new Point3D(0, 0, height));

        vb.add(new Point3D(0, 0, 0));

        for (int i = 0; i < segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            vb.add(new Point3D(x, y, 0));
        }

        int baseStartIndex = 2;
        for (int i = 0; i < segments; i++) {
            int current = baseStartIndex + i;
            int next = baseStartIndex + (i + 1) % segments;

            addIndices(0, current);
            addIndices(current, next);
        }
    }
}