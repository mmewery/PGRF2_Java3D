package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisZ extends SimpleSolid {
    public AxisZ() {
        color = new Col(0x0000ff);

        vb.add(new Point3D(0, 0, 0));
        vb.add(new Point3D(0, 0, 1));

        addIndices(0, 1);
    }
}
