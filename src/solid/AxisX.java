package solid;

import model.SolidPart;
import model.Topology;
import model.Vertex;
import transforms.Col;

public class AxisX extends Solid {
    public AxisX() {
        vertexBuffer.add(new Vertex(0, 0, 0, new Col(0xff0000)));
        vertexBuffer.add(new Vertex(1, 0, 0, new Col(0xff00)));

        addIndices(0, 1);

        partBuffer.add(new SolidPart(Topology.LINE_LIST, 1, 0));
    }
}

