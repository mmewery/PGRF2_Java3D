package render;

import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import shader.Shader;
import shader.ShaderInterpolated;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import util.Lerp;

import java.util.Optional;

public abstract class Renderer {
    protected LineRasterizer lineRasterizer;
    protected TriangleRasterizer triangleRasterizer;
    protected int width, height;
    protected Mat4 view, proj;

    private final Lerp<Vertex> vertexLerp = new Lerp<>();

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
        Mat4 transform = solid.getModel().mul(view).mul(proj);

        pA = pA.mul(transform);
        pB = pB.mul(transform);

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

        Mat4 modelView = solid.getModel().mul(view);

        vA = new Vertex(vA.getPosition().mul(modelView), vA.getColor(), vA.getUv(), vA.getNormal());
        vB = new Vertex(vB.getPosition().mul(modelView), vB.getColor(), vB.getUv(), vB.getNormal());
        vC = new Vertex(vC.getPosition().mul(modelView), vC.getColor(), vC.getUv(), vC.getNormal());


        double zMax = -0.1;

        if (vA.getZ() > vB.getZ()) { Vertex temp = vA; vA = vB; vB = temp; }
        if (vA.getZ() > vC.getZ()) { Vertex temp = vA; vA = vC; vC = temp; }
        if (vB.getZ() > vC.getZ()) { Vertex temp = vB; vB = vC; vC = temp; }

        if (vA.getZ() > zMax) {
            return;
        }

        if (vB.getZ() > zMax) {
            double tAB = (zMax - vA.getZ()) / (vB.getZ() - vA.getZ());
            double tAC = (zMax - vA.getZ()) / (vC.getZ() - vA.getZ());
            vB = vertexLerp.lerp(vA, vB, tAB);
            vC = vertexLerp.lerp(vA, vC, tAC);

            drawTriangle(solid, vA, vB, vC);
        }
        else if (vC.getZ() > zMax && vB.getZ() <= zMax) {
            double tAC = (zMax - vA.getZ()) / (vC.getZ() - vA.getZ());
            double tBC = (zMax - vB.getZ()) / (vC.getZ() - vB.getZ());

            Vertex vAC = vertexLerp.lerp(vA, vC, tAC);
            Vertex vBC = vertexLerp.lerp(vB, vC, tBC);

            drawTriangle(solid, vA, vB, vAC);
            drawTriangle(solid, vB, vBC, vAC);
        }
        else {
            drawTriangle(solid, vA, vB, vC);
        }
    }
    private void drawTriangle(Solid solid, Vertex vA, Vertex vB, Vertex vC) {
        Point3D pA = vA.getPosition().mul(proj);
        Point3D pB = vB.getPosition().mul(proj);
        Point3D pC = vC.getPosition().mul(proj);

        if (pA.getW() < 0.1 || pB.getW() < 0.1 || pC.getW() < 0.1) return;

        Optional<Vec3D> dehomogA = pA.dehomog();
        Optional<Vec3D> dehomogB = pB.dehomog();
        Optional<Vec3D> dehomogC = pC.dehomog();

        if (dehomogA.isPresent() && dehomogB.isPresent() && dehomogC.isPresent()) {
            Vec3D vecA = transformToWindow(dehomogA.get());
            Vec3D vecB = transformToWindow(dehomogB.get());
            Vec3D vecC = transformToWindow(dehomogC.get());

            Vertex v1 = new Vertex(new Point3D(vecA), vA.getColor(), vA.getUv(), vA.getNormal());
            Vertex v2 = new Vertex(new Point3D(vecB), vB.getColor(), vB.getUv(), vB.getNormal());
            Vertex v3 = new Vertex(new Point3D(vecC), vC.getColor(), vC.getUv(), vC.getNormal());

            triangleRasterizer.rasterize(v1, v2, v3, solid.getShader());
        }
    }


}
