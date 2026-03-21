package shader;

import model.Vertex;
import solid.Solid;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

public class ShaderPhong implements Shader {

    private final Point3D lightPosition;
    private final Col ambientColor;

    public ShaderPhong(Solid lightSource) {
        this.lightPosition = new Point3D(lightSource.getModel().getTranslate());
        this.ambientColor = new Col(0.2, 0.2, 0.2);
    }

    @Override
    public Col getColor(Vertex pixel) {
        Col diffuseColor = pixel.getColor();

        Vec3D lightDir = new Vec3D(
                lightPosition.getX() - pixel.getViewPosition().getX(),
                lightPosition.getY() - pixel.getViewPosition().getY(),
                lightPosition.getZ() - pixel.getViewPosition().getZ()
        ).normalized().orElse(new Vec3D(0, 0, 1));

        Vec3D normal = pixel.getNormal().normalized().orElse(new Vec3D(0, 0, 1));

        double nDotL = Math.max(0.0, normal.dot(lightDir));

        double r = ambientColor.getR() + (diffuseColor.getR() * nDotL);
        double g = ambientColor.getG() + (diffuseColor.getG() * nDotL);
        double b = ambientColor.getB() + (diffuseColor.getB() * nDotL);

        return new Col(
                Math.min(1.0, r),
                Math.min(1.0, g),
                Math.min(1.0, b)
        );
    }
}