package render;

import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;

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


    public void setView(Mat4 view) {
        this.view = view;
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
        Vertex vA = solid.getVertexBuffer().get(indexA);
        Vertex vB = solid.getVertexBuffer().get(indexB);

        Mat4 modelView = solid.getModel().mul(view);
        vA = vA.transf(modelView).captureViewPos();
        vB = vB.transf(modelView).captureViewPos();

        double zMin = 0;

        if (vA.getPosition().getZ() > zMin && vB.getPosition().getZ() > zMin) {
            return;
        }

        if (vA.getPosition().getZ() > zMin) {
            double t = (zMin - vA.getPosition().getZ()) / (vB.getPosition().getZ() - vA.getPosition().getZ());
            vA = vertexLerp.lerp(vA, vB, t);
        }
        else if (vB.getPosition().getZ() > zMin) {
            double t = (zMin - vA.getPosition().getZ()) / (vB.getPosition().getZ() - vA.getPosition().getZ());
            vB = vertexLerp.lerp(vA, vB, t);
        }

        vA = vA.transfPosOnly(proj);
        vB = vB.transfPosOnly(proj);

        if (vA.getPosition().getW() < 0.1 || vB.getPosition().getW() < 0.1) return;

        // dehomog
        Optional<Vec3D> dehomogA = vA.getPosition().dehomog();
        Optional<Vec3D> dehomogB = vB.getPosition().dehomog();

        if (dehomogA.isPresent() && dehomogB.isPresent()) {
            Vec3D vecA = transformToWindow(dehomogA.get());
            Vec3D vecB = transformToWindow(dehomogB.get());

            Vertex v1 = new Vertex(new Point3D(vecA), vA.getColor(), vA.getUv(), vA.getNormal());
            Vertex v2 = new Vertex(new Point3D(vecB), vB.getColor(), vB.getUv(), vB.getNormal());

            lineRasterizer.rasterize(v1, v2);
        }
    }
    public void renderTriangle(Solid solid, int indexA, int indexB, int indexC){
        Vertex vA = solid.getVertexBuffer().get(indexA);
        Vertex vB = solid.getVertexBuffer().get(indexB);
        Vertex vC = solid.getVertexBuffer().get(indexC);

        //aplikace Model a View cely vertex
        Mat4 modelView = solid.getModel().mul(view);
        vA = vA.transf(modelView);
        vB = vB.transf(modelView);
        vC = vC.transf(modelView);

        vA = vA.captureViewPos();
        vB = vB.captureViewPos();
        vC = vC.captureViewPos();

        double zMix = 0;

        if (vA.getZ() > vB.getZ()) { Vertex temp = vA; vA = vB; vB = temp; }
        if (vA.getZ() > vC.getZ()) { Vertex temp = vA; vA = vC; vC = temp; }
        if (vB.getZ() > vC.getZ()) { Vertex temp = vB; vB = vC; vC = temp; }

        if (vA.getZ() > zMix) {
            return;
        }

        if (vB.getZ() > zMix) {
            double tAB = (zMix - vA.getZ()) / (vB.getZ() - vA.getZ());
            double tAC = (zMix - vA.getZ()) / (vC.getZ() - vA.getZ());
            vB = vertexLerp.lerp(vA, vB, tAB);
            vC = vertexLerp.lerp(vA, vC, tAC);

            drawTriangle(solid, vA, vB, vC);
        }
        else if (vC.getZ() > zMix && vB.getZ() <= zMix) {
            double tAC = (zMix - vA.getZ()) / (vC.getZ() - vA.getZ());
            double tBC = (zMix - vB.getZ()) / (vC.getZ() - vB.getZ());

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
        // proj matice na zobrazovací souřadnice
        vA = vA.transfPosOnly(proj);
        vB = vB.transfPosOnly(proj);
        vC = vC.transfPosOnly(proj);

        // Clip W
        if (vA.getPosition().getW() < 0.1 || vB.getPosition().getW() < 0.1 || vC.getPosition().getW() < 0.1) return;

        // Dehomogenizace
        Optional<Vec3D> dehomogA = vA.getPosition().dehomog();
        Optional<Vec3D> dehomogB = vB.getPosition().dehomog();
        Optional<Vec3D> dehomogC = vC.getPosition().dehomog();

        if (dehomogA.isPresent() && dehomogB.isPresent() && dehomogC.isPresent()) {
            Vec3D vecA = transformToWindow(dehomogA.get());
            Vec3D vecB = transformToWindow(dehomogB.get());
            Vec3D vecC = transformToWindow(dehomogC.get());

            Vertex v1 = new Vertex(new Point3D(vecA), vA.getViewPosition(), vA.getColor(), vA.getUv(), vA.getNormal());
            Vertex v2 = new Vertex(new Point3D(vecB), vB.getViewPosition(), vB.getColor(), vB.getUv(), vB.getNormal());
            Vertex v3 = new Vertex(new Point3D(vecC), vC.getViewPosition(), vC.getColor(), vC.getUv(), vC.getNormal());

            triangleRasterizer.rasterize(v1, v2, v3, solid.getShader());
        }
    }


}
