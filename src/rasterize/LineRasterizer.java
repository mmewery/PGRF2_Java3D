package rasterize;


import model.Vertex;
import raster.ZBuffer;

public abstract class LineRasterizer {
    protected final ZBuffer zBuffer;

    public LineRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;

    }

    public void rasterize(Vertex a, Vertex b) {

    }

}
