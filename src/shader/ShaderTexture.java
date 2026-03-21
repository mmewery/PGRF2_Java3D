package shader;

import model.Vertex;
import transforms.Col;

import java.awt.image.BufferedImage;

public class ShaderTexture implements Shader {
    private final BufferedImage image;

    public ShaderTexture(BufferedImage image) { this.image = image; }

    @Override
    public Col getColor(Vertex pixel) {
        int x = (int) (pixel.getUv().getX() * (image.getWidth() - 1));
        int y = (int) (pixel.getUv().getY() * (image.getHeight() - 1));

        int rgb = image.getRGB(x, y);
        return new Col(rgb);
    }
}
