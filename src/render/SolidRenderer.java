package render;

import model.SolidPart;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.TriangleRasterizer;
import solid.Solid;
import transforms.Col;

public class SolidRenderer {
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;

    public SolidRenderer(LineRasterizer lineRasterizer, TriangleRasterizer triangleRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.triangleRasterizer = triangleRasterizer;
    }

    public void render(Solid solid){
        for(SolidPart part: solid.getPartBuffer()){
            switch (part.getTopology()){
                case LINE_LIST:
                    int index = part.getStartIndex();
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);

                        //todo vrcholy vynasobit MVP
                        //todo orezani
                        //todo dehomog
                        //todo transformace do okna

                        //rasterizace
                        lineRasterizer.rasterize(
                                (int) Math.round(a.getX()),
                                (int) Math.round(a.getY()),
                                (int) Math.round(b.getX()),
                                (int) Math.round(a.getY()));

                    }
                    break;

                case TRIANGLE_LIST:
                    index = part.getStartIndex();
                    for (int i = 0; i < part.getPrimitiveCount(); i++) {
                        int indexA = solid.getIndexBuffer().get(index++);
                        int indexB = solid.getIndexBuffer().get(index++);
                        int indexC = solid.getIndexBuffer().get(index++);

                        Vertex a = solid.getVertexBuffer().get(indexA);
                        Vertex b = solid.getVertexBuffer().get(indexB);
                        Vertex c = solid.getVertexBuffer().get(indexC);

                        //rasterizace
                        triangleRasterizer.rasterize(a, b, c, new Col(0xffff00));
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
