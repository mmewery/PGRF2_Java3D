package rasterize;

import model.Vertex;
import raster.RasterBufferedImage;
import raster.ZBuffer;
import transforms.Col;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c, Col color){
        //TODO seradit vse body dle souradnic Y; Ay<By<Cy

        int ax = (int)Math.round(a.getX());
        int ay = (int)Math.round(a.getY());
        double az = a.getZ();

        int bx = (int)Math.round(b.getX());
        int by = (int)Math.round(b.getY());
        double bz = b.getZ();

        int cx = (int)Math.round(c.getX());
        int cy = (int)Math.round(c.getY());
        double cz = c.getZ();

        // 1. cast trojuhelniku
        for(int y = ay; y < by; y++){
            //hrana AB
            double tAB = (y - ay)/ (double)(by - ay);
            int xAB = (int)Math.round((1-tAB)*ax+tAB*bx);
            double zAB = (1-tAB)*az+tAB*bz;

            //hrana AC
            double tAC = (y - ay)/ (double)(cy - ay);
            int xAC = (int)Math.round((1-tAC)*ax+tAC*cx);
            double zAC = (1-tAC)*az+tAC*cz;

            //todo kontrola ze xAB<xAC, pokud ne - prohazuji
            for (int x = xAB; x <= xAC; x++){
                double t = (x - xAB)/(double)(xAC - xAB);
                double z = (1-t)*zAB+t*zAC;

                zBuffer.setPixelWithZTest(x, y, z, color);
            }
            //todo 2. cast trojuhelniku

        }
    }
}
