package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;

public class Plane extends Solid{
    public Plane() {
        vertexBuffer.add(new Vertex(1, 1, 0.5, new Col(0xff00ff)));
        vertexBuffer.add(new Vertex(1, -1, 0.5, new Col(0xff0000)));
        vertexBuffer.add(new Vertex(-1, -1, 0.5, new Col(0xffffff)));
        vertexBuffer.add(new Vertex(-1, 1, 0.5, new Col(0x0000ff)));

        addIndices(0, 1, 2, 3);

        partBuffer.add(new SolidPart(Topology.TRIANGLE_FAN, 2, 0));
    }
}
