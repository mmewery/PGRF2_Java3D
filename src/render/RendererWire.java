package render;

import model.SolidPart;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Mat4;

public class RendererWire extends Renderer{

    public RendererWire(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height, Mat4 view, Mat4 proj) {
        super(lineRasterizer, triangleRasterizer, width, height, view, proj);
    }

    public void render(Solid solid) {
        for (SolidPart part : solid.getPartBuffer()) {
            int index = part.getStartIndex();
            int indexA, indexB, indexC;

            switch (part.getTopology()) {
                case LINE_LIST:
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexA = solid.getIndexBuffer().get(index++);
                        indexB = solid.getIndexBuffer().get(index++);
                        renderLine(solid, indexA, indexB);
                    }
                    break;

                case TRIANGLE_LIST:
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexA = solid.getIndexBuffer().get(index++);
                        indexB = solid.getIndexBuffer().get(index++);
                        indexC = solid.getIndexBuffer().get(index++);

                        renderLine(solid, indexA, indexB);
                        renderLine(solid, indexB, indexC);
                        renderLine(solid, indexC, indexA);
                    }
                    break;

            }
        }
    }

}
