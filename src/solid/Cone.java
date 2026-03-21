package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Cone extends Solid {

    public Cone() {
        int slices = 8;
        double radius = 1.0;
        double height = 2.0;

        int baseCenterIndex = 0;
        int tipIndex = 1;

        vertexBuffer.add(new Vertex(new Point3D(0, 0, 0), new Col(0x555555), new Vec2D(0.5, 0.5), new Vec3D(0, 0, -1)));
        vertexBuffer.add(new Vertex(new Point3D(0, 0, height), new Col(0xffffff), new Vec2D(0.5, 1.0), new Vec3D(0, 0, 1)));

        int offset = 2;
        for (int i = 0; i < slices; i++) {
            double theta = 2 * Math.PI * i / slices;

            double x = radius * Math.cos(theta);
            double y = radius * Math.sin(theta);

            Vec3D normal = new Vec3D(x, y, (radius * radius) / height).normalized().orElse(new Vec3D(1, 0, 0));

            double u = (Math.cos(theta) + 1.0) / 2.0;
            double v = (Math.sin(theta) + 1.0) / 2.0;

            vertexBuffer.add(new Vertex(new Point3D(x, y, 0), new Col(v, 100, 255), new Vec2D(u, v), normal));
        }

        for (int i = 0; i < slices; i++) {
            int current = offset + i;
            int next = offset + ((i + 1) % slices);

            indexBuffer.add(tipIndex);
            indexBuffer.add(current);
            indexBuffer.add(next);

            indexBuffer.add(baseCenterIndex);
            indexBuffer.add(next);
            indexBuffer.add(current);
        }

        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, slices * 2, 0));
    }
}