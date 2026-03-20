package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Mat4Transl;
import transforms.Vec3D;

public class Cube extends Solid {
    public Cube() {
        vertexBuffer.add(new Vertex(-1, -1, -1, new Vec3D(0, 0, 1))); // 0
        vertexBuffer.add(new Vertex( 1, -1, -1, new Vec3D(0, 0, 1))); // 1
        vertexBuffer.add(new Vertex(-1,  1, -1, new Vec3D(0, 0, 1))); // 2
        vertexBuffer.add(new Vertex( 1,  1, -1, new Vec3D(0, 0, 1))); // 3
        vertexBuffer.add(new Vertex(-1, -1,  1, new Vec3D(0, 0, 1))); // 4
        vertexBuffer.add(new Vertex( 1, -1,  1, new Vec3D(0, 0, 1))); // 5
        vertexBuffer.add(new Vertex(-1,  1,  1, new Vec3D(0, 0, 1))); // 6
        vertexBuffer.add(new Vertex( 1,  1,  1, new Vec3D(0, 0, 1))); // 7

        addIndices(
                // Задняя грань
                0, 1, 2,  1, 3, 2,
                // Передняя грань
                4, 6, 5,  5, 6, 7,
                // Нижняя грань
                0, 4, 1,  1, 4, 5,
                // Верхняя грань
                2, 3, 6,  3, 7, 6,
                // Левая грань
                0, 2, 4,  2, 6, 4,
                // Правая грань
                1, 5, 3,  3, 5, 7
        );

        // У нас ровно 12 треугольников в массиве, начиная с 0 индекса
        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, 12, 0));


    }
}
