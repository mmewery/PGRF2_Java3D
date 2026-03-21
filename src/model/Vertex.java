package model;

import transforms.*;

public class Vertex implements Vectorizable<Vertex>{
    private final Col color;
    private final Point3D position;
    private final Point3D viewPosition;
    private final Vec2D uv;
    private final Vec3D normal;

    public Vertex(double x, double y, double z, Vec3D normal) {
        this.normal = normal;
        this.color = new Col(0xffff00);
        this.position = new Point3D(x, y, z);
        this.uv = new Vec2D();
        this.viewPosition = position;
    }

    public Vertex(double x, double y, double z, Col color, Vec3D normal) {
        this.color = color;
        this.normal = normal;
        this.position = new Point3D(x, y, z);
        this.uv = new Vec2D();
        this.viewPosition = position;

    }
    public Vertex(Point3D position, Col color, Vec2D uv, Vec3D normal) {
        this.position = position;
        this.viewPosition = position;
        this.color = color;
        this.uv = uv;
        this.normal = normal;
    }

    //Axis X, Y, Z
    public Vertex(double x, double y, double z, Col color) {
        this.color = color;
        this.normal = new Vec3D();
        this.position = new Point3D(x, y, z);
        this.uv = new Vec2D();
        this.viewPosition = position;

    }

    //Add, Mul
    public Vertex(Point3D position, Point3D viewPosition, Col color, Vec2D uv, Vec3D normal) {
        this.color = color;
        this.position = position;
        this.viewPosition = viewPosition;
        this.normal = normal;
        this.uv = uv;
    }

    public Vertex(Point3D position, Col color) {
        this.color = color;
        this.position = position;
        this.normal = new Vec3D();
        this.uv = new Vec2D();
        this.viewPosition = position;
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

    public Point3D getViewPosition() {
        return viewPosition;
    }

    @Override
    public Vertex mul(double d) {
        return new Vertex(position.mul(d), viewPosition.mul(d), color.mul(d),
                uv.mul(d),  normal.mul(d));
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(position.add(v.getPosition()), viewPosition.add(v.getViewPosition()), color.add(v.getColor()),
                uv.add(v.getUv()), normal.add(v.getNormal()));
    }

    public Vertex transf(Mat4 m) {
        Point3D newPos = this.position.mul(m);

        Point3D tip = new Point3D(normal.getX(), normal.getY(), normal.getZ()).mul(m);
        Point3D origin = new Point3D(0, 0, 0).mul(m);
        Vec3D newNormal = new Vec3D(tip.getX() - origin.getX(), tip.getY() - origin.getY(), tip.getZ() - origin.getZ()).normalized().orElse(new Vec3D(0, 1, 0));

        return new Vertex(newPos, this.viewPosition, this.color, this.uv, newNormal);
    }

    public Vertex transfPosOnly(Mat4 m) {
        return new Vertex(this.position.mul(m), this.viewPosition, this.color, this.uv, this.normal);
    }

    public Vertex captureViewPos() {
        return new Vertex(this.position, this.position, this.color, this.uv, this.normal);
    }
}
