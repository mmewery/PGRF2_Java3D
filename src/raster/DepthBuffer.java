package raster;

import java.util.Optional;

public class DepthBuffer implements Raster<Double>{
    private final double[][] buffer;
    private final int width;
    private final int height;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new double[width][height];
        clear();
    }

    @Override
    public void setValue(int x, int y, Double value) {
        buffer[y][x] = value;
    }

    @Override
    public Optional<Double> getValue(int x, int y) {
        return Optional.of(buffer[y][x]);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void clear() {
        for (int y = 0; y < width-1; y++) {
            for (int x = 0; x < height-1; x++) {
                buffer[y][x] = 1.0;
            }
        }
    }
}
