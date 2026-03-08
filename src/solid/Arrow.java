package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;


public class Arrow extends Solid {
    public Arrow() {
        vertexBuffer.add(new Vertex(0, 0, 0)); // v0
        vertexBuffer.add(new Vertex(0.5, 0, 0)); // v1
        vertexBuffer.add(new Vertex(0.5, 0.1, 0)); // v2
        vertexBuffer.add(new Vertex(0.7, 0, 0)); // v3
        vertexBuffer.add(new Vertex(0.5, -0.1, 0)); //v4

        addIndices(0, 1); //line
        addIndices(2, 3, 4); //triangle

        partBuffer.add(new SolidPart(Topology.LINE_LIST, 1, 0));
        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, 1, 2));

    }
}
