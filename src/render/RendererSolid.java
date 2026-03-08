package render;

import model.SolidPart;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;

import transforms.Mat4;


public class RendererSolid extends Renderer{
    public RendererSolid(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer, int width, int height, Mat4 view, Mat4 proj) {
        super(lineRasterizer, triangleRasterizer, width, height, view, proj);
    }

    public void render(Solid solid){
        for(SolidPart part: solid.getPartBuffer()){
            int index, indexA, indexB, indexC;
            switch (part.getTopology()){
                case LINE_LIST:
                    index = part.getStartIndex();
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexA = solid.getIndexBuffer().get(index++);
                        indexB = solid.getIndexBuffer().get(index++);

                        renderLine(solid, indexA, indexB);
                    }
                    break;

                case TRIANGLE_LIST:
                    index = part.getStartIndex();
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexA = solid.getIndexBuffer().get(index++);
                        indexB = solid.getIndexBuffer().get(index++);
                        indexC = solid.getIndexBuffer().get(index++);

                        renderTriangle(solid, indexA, indexB, indexC);
                    }
                    break;

                case LINE_STRIP:
                    index = part.getStartIndex();
                    indexA = solid.getIndexBuffer().get(index++);

                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexB = solid.getIndexBuffer().get(index++);
                        renderLine(solid, indexA, indexB);
                        indexA = indexB;
                    }
                    break;

                case LINE_LOOP:
                    index = part.getStartIndex();
                    indexA = solid.getIndexBuffer().get(index++);

                    for (int i = 0; i < part.getPrimitiveCount()-1; i++) {
                        indexB = solid.getIndexBuffer().get(index++);
                        renderLine(solid, indexA, indexB);
                        indexA = indexB;
                    }
                    renderLine(solid, part.getStartIndex(), solid.getIndexBuffer().get(--index));
                    break;

                case TRIANGLE_STRIP:
                    index = part.getStartIndex();
                    indexA = solid.getIndexBuffer().get(index++);
                    indexB = solid.getIndexBuffer().get(index++);

                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexC = solid.getIndexBuffer().get(index++);

                        if (i % 2 == 0) {
                            renderTriangle(solid, indexA, indexB, indexC);
                        } else {
                            renderTriangle(solid, indexB, indexA, indexC);
                        }

                        indexA = indexB;
                        indexB = indexC;
                    }
                    break;

                case TRIANGLE_FAN:
                    index = part.getStartIndex();
                    int centerIndex = solid.getIndexBuffer().get(index++);
                    int prevIndex = solid.getIndexBuffer().get(index++);

                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        int currIndex = solid.getIndexBuffer().get(index++);
                        renderTriangle(solid, centerIndex, prevIndex, currIndex);
                        prevIndex = currIndex;
                    }
                    break;

            }
        }
    }
}
