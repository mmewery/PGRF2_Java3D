package render;

import model.SolidPart;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import java.util.Optional;

public class RendererWire extends Renderer{

    public RendererWire(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height, Mat4 view, Mat4 proj) {
        super(lineRasterizer, triangleRasterizer, width, height, view, proj);
    }

    public void render(Solid solid) {

        for (SolidPart part : solid.getPartBuffer()) {
            int index = part.getStartIndex();
            for (int i = 0; i < part.getPrimitiveCount(); i++) {
                int indexA = solid.getIndexBuffer().get(index++);
                int indexB = solid.getIndexBuffer().get(index++);

                renderLine(solid, indexA, indexB);
            }
        }
    }

}
