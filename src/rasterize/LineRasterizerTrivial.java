package rasterize;

import raster.RasterBufferedImage;

import java.awt.*;

public class LineRasterizerTrivial extends LineRasterizer {

    /**
     * výhody:
     *   jednoduchá implementace, snadno pochopitelná;
     * nevýhody:
     *   používá operace s float a zaokrouhlování;
     *   méně přesné kreslení;
     */

    public LineRasterizerTrivial(RasterBufferedImage raster) {
        super(raster);
    }



    @Override
    public void rasterize(int x1, int y1, int x2, int y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;

        if (dx == 0) {
            if (y1 > y2) {
                int temp = y1; y1 = y2; y2 = temp;
            }
            for (int y = y1; y <= y2; y++) {
                raster.setPixel(x1, y, color.getRGB());
            }
            return;
        }

        float k = dy / dx;
        float q = y1 - k * x1;

        if (Math.abs(k) > 1) {
            if (y1 > y2) {
                int tempX = x1, tempY = y1;
                x1 = x2; y1 = y2;
                x2 = tempX; y2 = tempY;
            }
            for (int y = y1; y <= y2; y++) {
                int x = Math.round((y - q) / k);
                raster.setPixel(x, y, color.getRGB());
            }
        }

        else {
            if (x1 > x2) {
                int tempX = x1, tempY = y1;
                x1 = x2; y1 = y2;
                x2 = tempX; y2 = tempY;
            }
            for (int x = x1; x <= x2; x++) {
                int y = Math.round(k * x + q);
                raster.setPixel(x, y, color.getRGB());
            }
        }

    }
}
