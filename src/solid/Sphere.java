package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Sphere extends Solid {

    public Sphere() {
        int stacks = 6;
        int slices = 6;
        double radius = 1.0;

        for (int i = 0; i <= stacks; i++) {
            double phi = Math.PI / 2 - Math.PI * i / stacks;
            double v = (double) i / stacks;

            for (int j = 0; j <= slices; j++) {
                double theta = 2 * Math.PI * j / slices;
                double u = (double) j / slices;

                double x = radius * Math.cos(phi) * Math.cos(theta);
                double y = radius * Math.cos(phi) * Math.sin(theta);
                double z = radius * Math.sin(phi);

                Vec3D normal = new Vec3D(x, y, z).normalized().orElse(new Vec3D(0, 0, 1));

                vertexBuffer.add(new Vertex(new Point3D(x, y, z), new Col(0xffffff), new Vec2D(u, v), normal));
            }
        }

        for (int i = 0; i < stacks; i++) {
            for (int j = 0; j < slices; j++) {
                int p0 = i * (slices + 1) + j;
                int p1 = p0 + 1;
                int p2 = (i + 1) * (slices + 1) + j;
                int p3 = p2 + 1;

                indexBuffer.add(p0);
                indexBuffer.add(p2);
                indexBuffer.add(p1);

                indexBuffer.add(p1);
                indexBuffer.add(p2);
                indexBuffer.add(p3);
            }
        }

        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, stacks * slices * 2, 0));
    }
}