package render;

import model.SolidPart;
import model.Vertex;
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
            Vertex a, b, c;
            switch (part.getTopology()){
                case LINE_LIST:
                    index = part.getStartIndex();
                    indexA = solid.getIndexBuffer().get(index++);
                    indexB = solid.getIndexBuffer().get(index);

                    a = solid.getVertexBuffer().get(indexA);
                    b = solid.getVertexBuffer().get(indexB);

                    //todo vrcholy vynasobit MVP
                    //todo orezani
                    //todo dehomog
                    //todo transformace do okna

                    //rasterizace
                    lineRasterizer.rasterize(a, b);
                    break;

                case TRIANGLE_LIST:
                    index = part.getStartIndex();
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        indexA = solid.getIndexBuffer().get(index++);
                        indexB = solid.getIndexBuffer().get(index++);
                        indexC = solid.getIndexBuffer().get(index++);

                        a = solid.getVertexBuffer().get(indexA);
                        b = solid.getVertexBuffer().get(indexB);
                        c = solid.getVertexBuffer().get(indexC);

                        //rasterizace
                        triangleRasterizer.rasterize(a, b, c);
                    }
                    break;

                case LINE_STRIP:
                    //todo
                    break;

                case LINE_LOOP:
                    //todo
                    break;
                case TRIANGLE_STRIP:
                    //todo
                    break;

                case TRIANGLE_FAN:
                    //todo
                    break;

                case POINTS:
                    //todo
                    break;
            }
        }
    }
}
