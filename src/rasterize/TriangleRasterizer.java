package rasterize;

import model.Vertex;
import raster.RasterBufferedImage;
import raster.ZBuffer;
import transforms.Col;
import util.Lerp;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;
    private final Lerp<Vertex> vertexLerp = new Lerp<>();

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c){
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
            Vertex vAB = vertexLerp.lerp(a, b, tAB);

            //hrana AC
            double tAC = (y - ay)/ (double)(cy - ay);
            Vertex vAC = vertexLerp.lerp(a, c, tAC);

            //todo spocitat normala
            //todo spocitat u v textura

            Vertex vStart = vAB;
            Vertex vEnd = vAC;

            if (vStart.getX() > vEnd.getX()) {
                Vertex temp = vStart;
                vStart = vEnd;
                vEnd = temp;
            }

            int xMin = (int) Math.round(vStart.getX());
            int xMax = (int) Math.round(vEnd.getX());
            for (int x = xMin; x <= xMax; x++){
                double t = (x - xMin) / (double)(xMax - xMin);
                Vertex vFinal = vertexLerp.lerp(vStart, vEnd, t);
                zBuffer.setPixelWithZTest(x, y, vFinal.getZ(), vFinal.getColor());
            }
        }
        for(int y = by; y < cy; y++){
            //hrana AC
            double tAC = (y - ay)/ (double)(cy - ay);
            Vertex vAC = vertexLerp.lerp(a, c, tAC);

            //hrana BC
            double tBC = (y - by)/ (double)(cy - by);
            Vertex vBC = vertexLerp.lerp(b, c, tBC);

            //todo spocitat normala
            //todo spocitat u v textura

            //2. polovina
            Vertex vStart = vAC;
            Vertex vEnd = vBC;

            if (vStart.getX() > vEnd.getX()) {
                Vertex temp = vStart;
                vStart = vEnd;
                vEnd = temp;
            }

            int xMin = (int) Math.round(vStart.getX());
            int xMax = (int) Math.round(vEnd.getX());

            for (int x = xMin; x <= xMax; x++) {
                double t = (xMax == xMin) ? 0 : (x - xMin) / (double) (xMax - xMin);
                Vertex vFinal = vertexLerp.lerp(vStart, vEnd, t);
                zBuffer.setPixelWithZTest(x, y, vFinal.getZ(), vFinal.getColor());
            }
        }
    }
}
