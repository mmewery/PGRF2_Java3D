package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;
import transforms.Vec3D;

public class Vertex implements Vectorizable<Vertex>{
    private final Col color;
    private final Point3D position;
    private final Vec2D uv;
    private final Vec3D normal;

    public Vertex(double x, double y, double z, Vec3D normal) {
        this.normal = normal;
        this.color = new Col(0xffff00);
        this.position = new Point3D(x, y, z);
        this.uv = new Vec2D();
    }

    public Vertex(double x, double y, double z, Col color, Vec3D normal) {
        this.color = color;
        this.normal = normal;
        this.position = new Point3D(x, y, z);
        this.uv = new Vec2D();

    }

    //Axis X, Y, Z
    public Vertex(double x, double y, double z, Col color) {
        this.color = color;
        this.normal = new Vec3D();
        this.position = new Point3D(x, y, z);
        this.uv = new Vec2D();

    }

    //Add, Mul
    public Vertex(Point3D position, Col color, Vec2D uv, Vec3D normal) {
        this.color = color;
        this.position = position;
        this.normal = normal;
        this.uv = uv;
    }

    public Vertex(Point3D position, Col color) {
        this.color = color;
        this.position = position;
        this.normal = new Vec3D();
        this.uv = new Vec2D();
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

    public Vec3D getNormal() {
        return normal;
    }

    public Vec2D getUv() {
        return uv;
    }

    @Override
    public Vertex mul(double d) {
        return new Vertex(position.mul(d), color.mul(d),
                uv.mul(d),  normal.mul(d));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(position.add(v.getPosition()), color.add(v.getColor()),
                uv.add(v.getUv()), normal.add(v.getNormal()));
    }
}
