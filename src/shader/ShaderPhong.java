package shader;

import model.Vertex;
import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

public class ShaderPhong implements Shader {

    private Point3D lightPosViewSpace;
    private Col ambientColor;
    private Col diffuseColor;

    public ShaderPhong() {
    }

    public void updateLight(Point3D lightPosWorld, Mat4 viewMatrix, Col lightColor) {
        this.lightPosViewSpace = lightPosWorld.mul(viewMatrix);
        this.diffuseColor = lightColor;
        this.ambientColor = new Col(20, 0, 20);
    }

    @Override
    public Col getColor(Vertex pixel) {

        if (lightPosViewSpace == null) return diffuseColor;

        Vec3D lightDir = new Vec3D(
                lightPosViewSpace.getX() - pixel.getViewPosition().getX(),
                lightPosViewSpace.getY() - pixel.getViewPosition().getY(),
                lightPosViewSpace.getZ() - pixel.getViewPosition().getZ()
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