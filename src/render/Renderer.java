package render;

import model.SolidPart;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.SimpleSolid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.List;

public class Renderer {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;
    private int width, height;
    private Mat4 view, proj;

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height, Mat4 view, Mat4 proj) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.width = width;
        this.height = height;
        this.view = view;
        this.proj = proj;
    }

    public void renderSolid(model.Solid solid) {
        for (SolidPart part : solid.getPartBuffer()) {
            int startIndex = part.getStartIndex();
            int primitiveCount = part.getPrimitiveCount();

            switch (part.getTopology()) {
                case LINE_LIST:
                    for (int i = 0; i < primitiveCount; i++) {
                        int index1 = solid.getIndexBuffer().get(startIndex + i * 2);
                        int index2 = solid.getIndexBuffer().get(startIndex + i * 2 + 1);
                        renderLine(solid, index1, index2);
                    }
                    break;

                case TRIANGLE_LIST:
                    for (int i = 0; i < primitiveCount; i++) {
                        int index1 = solid.getIndexBuffer().get(startIndex + i * 3);
                        int index2 = solid.getIndexBuffer().get(startIndex + i * 3 + 1);
                        int index3 = solid.getIndexBuffer().get(startIndex + i * 3 + 2);
                        renderTriangle(solid, index1, index2, index3);
                    }
                    break;
            }
        }
    }

    private void renderLine(model.Solid solid, int i1, int i2) {
        Point3D a = solid.getVertexBuffer().get(i1).getPosition();
        Point3D b = solid.getVertexBuffer().get(i2).getPosition();

        a = a.mul(solid.getModelMat()).mul(view).mul(proj);
        b = b.mul(solid.getModelMat()).mul(view).mul(proj);

        if (a.getW() < 0.1 || b.getW() < 0.1) return;

        Vec3D v1 = transformToWindow(a.mul(1 / a.getW()));
        Vec3D v2 = transformToWindow(b.mul(1 / b.getW()));

        lineRasterizer.rasterize(
                (int) Math.round(v1.getX()), (int) Math.round(v1.getY()),
                (int) Math.round(v2.getX()), (int) Math.round(v2.getY())
        );
    }

    private void renderTriangle(model.Solid solid, int i1, int i2, int i3) {
        Vertex v1 = solid.getVertexBuffer().get(i1);
        Vertex v2 = solid.getVertexBuffer().get(i2);
        Vertex v3 = solid.getVertexBuffer().get(i3);

        Point3D p1 = v1.getPosition().mul(solid.getModelMat()).mul(view).mul(proj);
        Point3D p2 = v2.getPosition().mul(solid.getModelMat()).mul(view).mul(proj);
        Point3D p3 = v3.getPosition().mul(solid.getModelMat()).mul(view).mul(proj);

        if (p1.getW() < 0.1 || p2.getW() < 0.1 || p3.getW() < 0.1) return;

        Vec3D screenP1 = transformToWindow(p1.mul(1 / p1.getW()));
        Vec3D screenP2 = transformToWindow(p2.mul(1 / p2.getW()));
        Vec3D screenP3 = transformToWindow(p3.mul(1 / p3.getW()));

        Vertex screenV1 = new Vertex(screenP1.getX(), screenP1.getY(), screenP1.getZ(), v1.getColor());
        Vertex screenV2 = new Vertex(screenP2.getX(), screenP2.getY(), screenP2.getZ(), v2.getColor());
        Vertex screenV3 = new Vertex(screenP3.getX(), screenP3.getY(), screenP3.getZ(), v3.getColor());

        triangleRasterizer.rasterize(screenV1, screenV2, screenV3, v1.getColor());
    }


    public void renderSimpleSolid(SimpleSolid simpleSolid) {
        lineRasterizer.setColor(simpleSolid.getColor());

        for (int i = 0; i < simpleSolid.getIb().size(); i += 2) {
            int indexA = simpleSolid.getIb().get(i);
            int indexB = simpleSolid.getIb().get(i + 1);

            Point3D a = simpleSolid.getVb().get(indexA);
            Point3D b = simpleSolid.getVb().get(indexB);

            // Model - Modelovací transformace
            a = a.mul(simpleSolid.getModel());
            b = b.mul(simpleSolid.getModel());

            // View - Pohledová transformace
            a = a.mul(view);
            b = b.mul(view);

            // Projection - Projekční transformace
            a = a.mul(proj);
            b = b.mul(proj);

            if (a.getW() < 0.1 || b.getW() < 0.1) {
                continue;
            }

            a = a.mul(1 / a.getW());
            b = b.mul(1 / b.getW());

            // Transformace do okna obrazovky
            Vec3D vecA = transformToWindow(a);
            Vec3D vecB = transformToWindow(b);

            lineRasterizer.rasterize(
                    (int) Math.round(vecA.getX()),
                    (int) Math.round(vecA.getY()),
                    (int) Math.round(vecB.getX()),
                    (int) Math.round(vecB.getY())
            );
        }
    }

    public void renderSolids(List<model.Solid> solids) {
        for(model.Solid solid : solids) {
            renderSolid(solid);
        }
    }
    public void renderSimpleSolids(List<SimpleSolid> simpleSolids) {
        for(SimpleSolid simpleSolid : simpleSolids) {
            renderSimpleSolid(simpleSolid);
        }
    }
    private Vec3D transformToWindow(Point3D p) {
        return new Vec3D(p).mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) / 2, 1));
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
