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
        if (a.getY() > b.getY()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (a.getY() > c.getY()) {
            Vertex temp = a;
            a = c;
            c = temp;
        }
        if (b.getY() > c.getY()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }

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

            if (xAB > xAC) {
                int tempx = xAB;
                xAB = xAC;
                xAC = tempx;
                double tempz = zAB;
                zAB = zAC;
                zAC = tempz;
            }
            for (int x = xAB; x <= xAC; x++){
                double t = (x - xAB)/(double)(xAC - xAB);
                double z = (1-t)*zAB+t*zAC;

                zBuffer.setPixelWithZTest(x, y, z, color);
            }
        }
        for(int y = by; y < cy; y++){
            //hrana AC
            double tAC = (y - ay)/ (double)(cy - ay);
            int xAC = (int)Math.round((1-tAC)*ax+tAC*cx);
            double zAC = (1-tAC)*az+tAC*cz;

            //hrana BC
            double tBC = (y - by)/ (double)(cy - by);
            int xBC = (int)Math.round((1-tBC)*bx+tBC*cx);
            double zBC = (1-tBC)*bz+tBC*cz;

            //2. polovina
            if (xBC > xAC) {
                int tempx = xBC;
                xBC = xAC;
                xAC = tempx;
                double tempz = zBC;
                zBC = zAC;
                zAC = tempz;
            }
            for (int x = xBC; x <= xAC; x++){
                double t = (x - xBC)/(double)(xAC - xBC);
                double z = (1-t)*zBC+t*zAC;

                zBuffer.setPixelWithZTest(x, y, z, color);
            }
        }
    }
}
