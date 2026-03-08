package rasterize;


import model.Vertex;
import raster.RasterBufferedImage;
import transforms.Col;

public abstract class LineRasterizer {
    protected RasterBufferedImage raster;

    public LineRasterizer(RasterBufferedImage raster) {
        this.raster = raster;

    }

    public void rasterize(Vertex a, Vertex b) {

    }

}
