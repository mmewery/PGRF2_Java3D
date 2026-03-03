package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;


public class Arrow extends Solid {
    public Arrow() {
        vertexBuffer.add(new Vertex(200, 300, 0.5)); //v0
        vertexBuffer.add(new Vertex(250, 300, 0.5)); //v1
        vertexBuffer.add(new Vertex(250, 320, 0.5)); //v2
        vertexBuffer.add(new Vertex(270, 300, 0.5)); //v3
        vertexBuffer.add(new Vertex(250, 280, 0.5)); //v4

        addIndices(0, 1); //lines
        addIndices(2, 3, 4);

        partBuffer.add(new SolidPart(Topology.LINE_LIST, 1, 0));
        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, 1, 2));

    }
}
