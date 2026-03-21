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

        addSide(new Point3D(-1, -1,  1), new Point3D( 1, -1,  1), new Point3D( 1,  1,  1), new Point3D(-1,  1,  1),
                new Vec3D(0, 0, 1));

        addSide(new Point3D( 1, -1, -1), new Point3D(-1, -1, -1), new Point3D(-1,  1, -1), new Point3D( 1,  1, -1),
                new Vec3D(0, 0, -1));

        addSide(new Point3D(-1,  1,  1), new Point3D( 1,  1,  1), new Point3D( 1,  1, -1), new Point3D(-1,  1, -1),
                new Vec3D(0, 1, 0));

        addSide(new Point3D(-1, -1, -1), new Point3D( 1, -1, -1), new Point3D( 1, -1,  1), new Point3D(-1, -1,  1),
                new Vec3D(0, -1, 0));

        addSide(new Point3D( 1, -1,  1), new Point3D( 1, -1, -1), new Point3D( 1,  1, -1), new Point3D( 1,  1,  1),
                new Vec3D(1, 0, 0));

        addSide(new Point3D(-1, -1, -1), new Point3D(-1, -1,  1), new Point3D(-1,  1,  1), new Point3D(-1,  1, -1),
                new Vec3D(-1, 0, 0));

        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, 12, 0));
    }

    private void addSide(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Vec3D normal) {
        int startIndex = vertexBuffer.size();

        vertexBuffer.add(new Vertex(p1, new Col(255, 255, 255), new Vec2D(0, 1), normal));
        vertexBuffer.add(new Vertex(p2, new Col(255, 255, 255), new Vec2D(1, 1), normal));
        vertexBuffer.add(new Vertex(p3, new Col(200, 0, 200), new Vec2D(1, 0), normal));
        vertexBuffer.add(new Vertex(p4, new Col(255, 255, 255), new Vec2D(0, 0), normal));

        addIndices(startIndex, startIndex + 1, startIndex + 2);
        addIndices(startIndex, startIndex + 2, startIndex + 3);
    }
}
