package render;

import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public abstract class Renderer {
    protected LineRasterizer lineRasterizer;
    protected TriangleRasterizer triangleRasterizer;
    protected int width, height;
    protected Mat4 view, proj;

    public Renderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height, Mat4 view, Mat4 proj) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
        this.width = width;
        this.height = height;
        this.view = view;
        this.proj = proj;
    }

    public void render(Solid solid){}

    public LineRasterizer getLineRasterizer() {
        return lineRasterizer;
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public TriangleRasterizer getTriangleRasterizer() {
        return triangleRasterizer;
    }

    public void setTriangleRasterizer(TriangleRasterizer triangleRasterizer) {
        this.triangleRasterizer = triangleRasterizer;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Mat4 getView() {
        return view;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public Mat4 getProj() {
        return proj;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public Vec3D transformToWindow(Vec3D p) {
        return p.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((width - 1) / 2.0, (height - 1) / 2.0, 1.0));
    }
    public void renderLine(Solid solid, int indexA, int indexB) {
        Vertex vertexA = solid.getVertexBuffer().get(indexA);
        Vertex vertexB = solid.getVertexBuffer().get(indexB);

        Point3D pA = solid.getVertexBuffer().get(indexA).getPosition();
        Point3D pB = solid.getVertexBuffer().get(indexB).getPosition();

        // mvp transformace
        pA = pA.mul(view).mul(proj);
        pB = pB.mul(view).mul(proj);

        // orezani
        if (pA.getW() < 0.1 || pB.getW() < 0.1) return;

        // dehomog
        Optional<Vec3D> dehomogA = pA.dehomog();
        Optional<Vec3D> dehomogB = pB.dehomog();

        if (dehomogA.isPresent() && dehomogB.isPresent()) {
            // transformace do okna
            Vec3D vecA = transformToWindow(dehomogA.get());
            Vec3D vecB = transformToWindow(dehomogB.get());

            Vertex v1 = new Vertex(new Point3D(vecA), vertexA.getColor());
            Vertex v2 = new Vertex(new Point3D(vecB), vertexB.getColor());

            lineRasterizer.rasterize(v1, v2);
        }
    }
    public void renderTriangle(Solid solid, int indexA, int indexB, int indexC){
        Vertex vA = solid.getVertexBuffer().get(indexA);
        Vertex vB = solid.getVertexBuffer().get(indexB);
        Vertex vC = solid.getVertexBuffer().get(indexC);

        Point3D pA = vA.getPosition().mul(view).mul(proj);
        Point3D pB = vB.getPosition().mul(view).mul(proj);
        Point3D pC = vC.getPosition().mul(view).mul(proj);

        if (pA.getW() < 0.1 || pB.getW() < 0.1 || pC.getW() < 0.1) return;

        Optional<Vec3D> dehomogA = pA.dehomog();
        Optional<Vec3D> dehomogB = pB.dehomog();
        Optional<Vec3D> dehomogC = pC.dehomog();

        if (dehomogA.isPresent() && dehomogB.isPresent() && dehomogC.isPresent()) {
            Vec3D vecA = transformToWindow(dehomogA.get());
            Vec3D vecB = transformToWindow(dehomogB.get());
            Vec3D vecC = transformToWindow(dehomogC.get());

            Vertex v1 = new Vertex(new Point3D(vecA), vA.getColor());
            Vertex v2 = new Vertex(new Point3D(vecB), vB.getColor());
            Vertex v3 = new Vertex(new Point3D(vecC), vC.getColor());

            triangleRasterizer.rasterize(v1, v2, v3);
        }
    }

}
