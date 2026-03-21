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

        vertexBuffer.add(new Vertex(-0.05,0, 0.5, new Col(0x0000ff)));
        vertexBuffer.add(new Vertex(0, 0, 1, new Col(0x0000ff)));
        vertexBuffer.add(new Vertex( 0.05,0, 0.5, new Col(0x0000ff)));
        addIndices(2, 3, 4);
        partBuffer.add(new SolidPart(Topology.TRIANGLE_LIST, 1, 2));
    }
}
