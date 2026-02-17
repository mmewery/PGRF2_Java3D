package model;

import transforms.Col;
import transforms.Point3D;

public class Vertex {
    private final Col color;
    private final Point3D position;

    public Vertex(double x, double y, double z) {
        this.color = new Col(0xffff00);
        this.position = new Point3D(x, y, z);
    }

    public Vertex(double x, double y, double z, Col color) {
        this.color = color;
        this.position = new Point3D(x, y, z);
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
}
