package raster;

import transforms.Col;

import java.util.Optional;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer) {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getWidth(), imageBuffer.getHeight());
    }

    public void setPixelWithZTest(int x, int y, double z, Col col) {
        if(depthBuffer.getValue(x, y).isPresent() && z <= depthBuffer.getValue(x, y).get()){
            imageBuffer.setValue(x, y, col);
            depthBuffer.setValue(x, y, Double.valueOf(z));
        }
    }
}
