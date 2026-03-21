package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;

public class Arrow extends Solid {
    public Arrow() {
        vertexBuffer.add(new Vertex(0, -1, 0, new Col(0xffffff)));
        vertexBuffer.add(new Vertex(0, 0, 0, new Col(0xffffff)));
        addIndices(0, 1);
        partBuffer.add(new SolidPart(Topology.LINE_LIST, 1, 0));

        vertexBuffer.add(new Vertex(-0.5, 0, 0, new Col(0xffffff)));
        vertexBuffer.add(new Vertex(0, 1, 0, new Col(0xffffff)));
        vertexBuffer.add(new Vertex(0.5, 0, 0, new Col(0xffffff)));
        addIndices(2, 3, 4);
        partBuffer.add(new SolidPart(Topology.TRIANGLE_FAN, 1, 2));

    }
}


