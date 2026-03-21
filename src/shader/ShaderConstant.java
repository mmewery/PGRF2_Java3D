package shader;

import model.Vertex;
import transforms.Col;


public class ShaderConstant implements Shader {
    private final Col color;
    @Override
    public Col getColor(Vertex pixel) {
        return color;
    }

    public ShaderConstant(Col color) {
        this.color = color;
    }
}
