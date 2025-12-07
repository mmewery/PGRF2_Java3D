package solid;

import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected List<Point3D> vb = new ArrayList<>();
    protected List<Integer> ib = new ArrayList<>();
    protected Mat4 model = new Mat4Identity();
    protected Col color = new Col(0xffffff);

    protected void addIndices(Integer... indices) {
        ib.addAll(Arrays.asList(indices));
    }

    public List<Point3D> getVb() {
        return vb;
    }

    public List<Integer> getIb() {
        return ib;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public Mat4 getModel() {
        return model;
    }

    public Col getColor() {
        return color;
    }
}
