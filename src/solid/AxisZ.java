package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;

public class AxisZ extends Solid {
    public AxisZ() {
        vertexBuffer.add(new Vertex(0, 0, 0, new Col(0x0000ff)));
        vertexBuffer.add(new Vertex(0, 0, 1, new Col(0x0000ff)));

        addIndices(0, 1);

        partBuffer.add(new SolidPart(Topology.LINE_LIST, 1, 0));
    }
}
