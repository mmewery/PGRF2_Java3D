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

    private void renderLine(Solid solid, int indexA, int indexB) {
        Vertex vertexA = solid.getVertexBuffer().get(indexA);
        Vertex vertexB = solid.getVertexBuffer().get(indexB);

        Point3D a = solid.getVertexBuffer().get(indexA).getPosition();
        Point3D b = solid.getVertexBuffer().get(indexB).getPosition();

        // mvp transformace
        a = a.mul(view).mul(proj);
        b = b.mul(view).mul(proj);

        // orezani
        if (a.getW() < 0.1 || b.getW() < 0.1) return;

        // dehomog
        Optional<Vec3D> dehomogA = a.dehomog();
        Optional<Vec3D> dehomogB = b.dehomog();

        if (dehomogA.isPresent() && dehomogB.isPresent()) {
            // transformace do okna
            Vec3D vecA = transformToWindow(dehomogA.get());
            Vec3D vecB = transformToWindow(dehomogB.get());

            Vertex v1 = new Vertex(new Point3D(vecA), vertexA.getColor());
            Vertex v2 = new Vertex(new Point3D(vecB), vertexB.getColor());

            lineRasterizer.rasterize(v1, v2);
        }
    }
}
