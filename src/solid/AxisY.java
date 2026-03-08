package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;

public class AxisY extends Solid {
    public AxisY() {
        vertexBuffer.add(new Vertex(0, 0, 0, new Col(0x00ff00)));
        vertexBuffer.add(new Vertex(0, 1, 0, new Col(0x00ff00)));

        addIndices(0, 1);

        partBuffer.add(new SolidPart(Topology.LINE_LIST, 1, 0));
    }
}
