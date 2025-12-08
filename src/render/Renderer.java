package render;

import rasterize.LineRasterizer;
import solid.Solid;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.List;

public class Renderer {
    private LineRasterizer lineRasterizer;
    private int width, height;
    private Mat4 view, proj;

    public Renderer(LineRasterizer lineRasterizer, int width, int height, Mat4 view, Mat4 proj) {
        this.lineRasterizer = lineRasterizer;
        this.width = width;
        this.height = height;
        this.view = view;
        this.proj = proj;
    }

    public void renderSolid(Solid solid) {
        lineRasterizer.setColor(solid.getColor());

        for (int i = 0; i < solid.getIb().size(); i += 2) {
            int indexA = solid.getIb().get(i);
            int indexB = solid.getIb().get(i + 1);

            Point3D a = solid.getVb().get(indexA);
            Point3D b = solid.getVb().get(indexB);

            // Model - Modelovací transformace
            a = a.mul(solid.getModel());
            b = b.mul(solid.getModel());

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

    private Vec3D transformToWindow(Point3D p) {
        return new Vec3D(p).mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) / 2, 1));
    }

    public void renderSolids(List<Solid> solids) {
        for(Solid solid : solids) {
            renderSolid(solid);
        }
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}
