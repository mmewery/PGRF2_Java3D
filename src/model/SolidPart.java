package model;

public class SolidPart  {
    private final Topology topology;
    private final int primitiveCount;
    private final int startIndex;

    public SolidPart(Topology topology, int primitiveCount, int startIndex) {
        this.topology = topology;
        this.primitiveCount = primitiveCount;
        this.startIndex = startIndex;
    }
}
