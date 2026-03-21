package rasterize;

import model.Vertex;
import raster.ZBuffer;
import util.Lerp;

public class LineRasterizerTrivial extends LineRasterizer {

    public LineRasterizerTrivial(ZBuffer zBuffer) {
        super(zBuffer);
    }

    @Override
    public void rasterize(Vertex a, Vertex b) {
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;

        Lerp<Vertex> lerp = new Lerp<>();

        if (Math.abs(dy) > Math.abs(dx)) {
            if (y1 > y2) {
                Vertex temp = a; a = b; b = temp;
            }

            for (int y = (int) Math.round(a.getY()); y <= (int) Math.round(b.getY()); y++) {
                double t = (y - a.getY()) / (b.getY() - a.getY());
                Vertex interpolated = lerp.lerp(a, b, t);
                zBuffer.setPixelWithZTest((int) Math.round(interpolated.getX()), y, interpolated.getZ(), interpolated.getColor());

            }
        } else {
            if (x1 > x2) {
                Vertex temp = a; a = b; b = temp;
            }

            for (int x = (int) Math.round(a.getX()); x <= (int) Math.round(b.getX()); x++) {
                double t = (x - a.getX()) / (b.getX() - a.getX());
                Vertex interpolated = lerp.lerp(a, b, t);

                zBuffer.setPixelWithZTest(x, (int) Math.round(interpolated.getY()), interpolated.getZ(), interpolated.getColor());
            }
        }
    }
}
