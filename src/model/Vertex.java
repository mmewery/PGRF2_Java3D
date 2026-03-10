package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Vertex implements Vectorizable<Vertex>{
    private final Col color;
    private final Point3D position;
    //todo private final Vec2D uv;

    public Vertex(double x, double y, double z) {
        this.color = new Col(0xffff00);
        this.position = new Point3D(x, y, z);
    }

    public Vertex(double x, double y, double z, Col color) {
        this.color = color;
        this.position = new Point3D(x, y, z);
    }

    public Vertex(Point3D position, Col color) {
        this.color = color;
        this.position = position;
    }

    public Col getColor() {
        return color;
    }

    public Point3D getPosition() {
        return position;
    }

    public double getX(){
        return position.getX();
    }

    public double getY(){
        return position.getY();
    }

    public double getZ(){
        return position.getZ();
    }

    @Override
    public Vertex mul(double d) {
        return new Vertex(position.mul(d), color.mul(d));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(position.add(v.getPosition()), color.add(v.getColor()));
    }
}
