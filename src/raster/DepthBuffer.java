package raster;

import java.util.Optional;

public class DepthBuffer implements Raster<Double>{
    private final double[][] buffer;
    private final int width;
    private final int height;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new double[height][width];
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
        // Loop through Y first (rows), then X (columns)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buffer[y][x] = 1.0;
            }
        }
    }
}
