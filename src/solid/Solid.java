package solid;

import model.SolidPart;
import model.Vertex;
import transforms.Col;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {
    protected final List<Vertex> vertexBuffer = new ArrayList<>();
    protected final List<Integer> indexBuffer = new ArrayList<>();
    protected final List<SolidPart> partBuffer = new ArrayList<>();


    public List<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public List<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public List<SolidPart> getPartBuffer() {
        return partBuffer;
    }

    public void addIndices(Integer... indices){
        indexBuffer.addAll(Arrays.asList(indices));
    }


}
