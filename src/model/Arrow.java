package model;

import transforms.Mat4;
import transforms.Mat4Identity;

import java.util.List;

public class Arrow extends Solid{
    public Arrow() {
        super(List.of(new Vertex(0, 0, 0),
                    new Vertex(0.8, 0, 0),
                    new Vertex(0.6, 0.1, 0),
                    new Vertex(0.6, -0.1, 0),
                    new Vertex(1.0, 0, 0)),
                List.of(0, 1,
                        4, 2,
                        4, 3,
                        2, 3),
                List.of(new SolidPart(Topology.LINE_LIST, 4, 0),
                        new SolidPart(Topology.TRIANGLE_LIST, 1, 4)),
                        new Mat4Identity());

    }
}
