package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Cube extends Solid {
    public Cube() {
        Col color = new Col(0xffffff);


        addSide(new Point3D(-1, -1,  1), new Point3D( 1, -1,  1), new Point3D( 1,  1,  1), new Point3D(-1,  1,  1),
                new Vec3D(0, 0, 1), color);

        addSide(new Point3D( 1, -1, -1), new Point3D(-1, -1, -1), new Point3D(-1,  1, -1), new Point3D( 1,  1, -1),
                new Vec3D(0, 0, -1), color);

        addSide(new Point3D(-1,  1,  1), new Point3D( 1,  1,  1), new Point3D( 1,  1, -1), new Point3D(-1,  1, -1),
                new Vec3D(0, 1, 0), color);

        addSide(new Point3D(-1, -1, -1), new Point3D( 1, -1, -1), new Point3D( 1, -1,  1), new Point3D(-1, -1,  1),
                new Vec3D(0, -1, 0), color);

        addSide(new Point3D( 1, -1,  1), new Point3D( 1, -1, -1), new Point3D( 1,  1, -1), new Point3D( 1,  1,  1),
                new Vec3D(1, 0, 0), color);

        addSide(new Point3D(-1, -1, -1), new Point3D(-1, -1,  1), new Point3D(-1,  1,  1), new Point3D(-1,  1, -1),
                new Vec3D(-1, 0, 0), color);

        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, 12, 0));
    }

    private void addSide(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Vec3D normal, Col color) {
        int startIndex = vertexBuffer.size();

        vertexBuffer.add(new Vertex(p1, color, new Vec2D(0, 1), normal));
        vertexBuffer.add(new Vertex(p2, color, new Vec2D(1, 1), normal));
        vertexBuffer.add(new Vertex(p3, color, new Vec2D(1, 0), normal));
        vertexBuffer.add(new Vertex(p4, color, new Vec2D(0, 0), normal));

        addIndices(startIndex, startIndex + 1, startIndex + 2);
        addIndices(startIndex, startIndex + 2, startIndex + 3);
    }
}
