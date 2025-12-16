package controller;

import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import render.Renderer;
import solid.*;
import transforms.*;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    // Rasterizers
    private LineRasterizer lineRasterizer;
    // Renderers
    private Renderer renderer;
    // Solids
    private List<Solid> solids = new ArrayList<>();
    Solid axisX, axisY, axisZ, cube, cone, curve;
    private Point3D p1, p2, p3, p4;

    private int activeSolidIndex = 0;

    private int oldX, oldY;

    private Camera camera;
    private Mat4 proj;

    private enum CubicMode {BEZIER, FERGUSON, COONS}

    private List<CubicMode> cubicModes = new ArrayList<>();

    private int cubicMode = 0;

    private boolean editPointMode = false;
    private int activePointIndex = 0;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster());

        this.camera = new Camera()
                .withPosition(new Vec3D(0.5, -1.5, 1))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);

        this.proj = new Mat4PerspRH(
                Math.toRadians(90),
                panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(),
                0.1,
                100
        );

        this.renderer = new Renderer(
                lineRasterizer,
                panel.getRaster().getWidth(),
                panel.getRaster().getHeight(),
                camera.getViewMatrix(),
                proj
        );

        //curve initial points
        p1 = new Point3D(-0.2,1,0);
        p2 = new Point3D(0.0,0,0.3);
        p3 = new Point3D(0.2,0,0.6);
        p4 = new Point3D(0.4,1,1);

        // Init solids
        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();

        cube = new Cube();
        cube.setModel(new Mat4Transl(-0.7, 0.5, 0.5));
        solids.add(cube);

        cone = new Cone();
        cone.setModel(new Mat4Transl(0.7, 0.5, 0));
        solids.add(cone);

        curve = new Curve(Cubic.BEZIER, p1, p2, p3, p4);
        curve.setModel(new Mat4Transl(2, 0, 0));
        solids.add(curve);

        cubicModes.add(CubicMode.BEZIER);
        cubicModes.add(CubicMode.FERGUSON);
        cubicModes.add(CubicMode.COONS);

        initListeners();

        drawScene();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                int dx = oldX - e.getX();
                int dy = oldY - e.getY();

                oldX = e.getX();
                oldY = e.getY();

                double azimuthChange = ((double) dx / panel.getWidth()) * Math.PI;
                double zenithChange = ((double) dy / panel.getHeight()) * Math.PI;

                camera = camera.addAzimuth(azimuthChange)
                        .addZenith(zenithChange);

                drawScene();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    activeSolidIndex++;
                    if (activeSolidIndex >= solids.size()) {
                        activeSolidIndex = 0;
                    }
                    for(Solid solid : solids) {
                        solid.setColor(new Col(0xffffff));
                    }
                    solids.get(activeSolidIndex).setColor(new Col(0x00ffff));
                }

                //translace
                double step = 0.2;
                if (e.getKeyCode() == KeyEvent.VK_LEFT && !editPointMode)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(-step, 0, 0)));
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && !editPointMode)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(step, 0, 0)));
                if (e.getKeyCode() == KeyEvent.VK_UP && !editPointMode)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, step, 0)));
                if (e.getKeyCode() == KeyEvent.VK_DOWN && !editPointMode)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, -step, 0)));
                if (e.getKeyCode() == KeyEvent.VK_PAGE_UP && !editPointMode)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, 0, step)));
                if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN && !editPointMode)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, 0, -step)));

                //rotace
                double angle = Math.toRadians(5);
                if (e.getKeyCode() == KeyEvent.VK_X)
                    solids.get(activeSolidIndex).setModel(new Mat4RotX(angle).mul(solids.get(activeSolidIndex).getModel()));
                if (e.getKeyCode() == KeyEvent.VK_Y)
                    solids.get(activeSolidIndex).setModel(new Mat4RotY(angle).mul(solids.get(activeSolidIndex).getModel()));
                if (e.getKeyCode() == KeyEvent.VK_Z)
                    solids.get(activeSolidIndex).setModel(new Mat4RotZ(angle).mul(solids.get(activeSolidIndex).getModel()));

                //scale
                double scaleUp = 1.2;
                double scaleDown = 0.8;
                if (e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET)
                    solids.get(activeSolidIndex).setModel(new Mat4Scale(scaleUp).mul(solids.get(activeSolidIndex).getModel()));
                if (e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET)
                    solids.get(activeSolidIndex).setModel(new Mat4Scale(scaleDown).mul(solids.get(activeSolidIndex).getModel()));

                if (e.getKeyCode() == KeyEvent.VK_P) {
                    proj = new Mat4PerspRH(
                            Math.toRadians(90),
                            panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(), 0.1,100);
                }

                if (e.getKeyCode() == KeyEvent.VK_O) {
                    double aspect = (double) panel.getRaster().getWidth() / panel.getRaster().getHeight();

                    proj = new Mat4OrthoRH(5 * aspect, 5,0.1,200);
                }

                if(e.getKeyCode() == KeyEvent.VK_W)
                    camera = camera.forward(0.5);
                if(e.getKeyCode() == KeyEvent.VK_A)
                    camera = camera.left(0.5);
                if(e.getKeyCode() == KeyEvent.VK_S)
                    camera = camera.backward(0.5);
                if(e.getKeyCode() == KeyEvent.VK_D)
                    camera = camera.right(0.5);

                if (e.getKeyCode() == KeyEvent.VK_M) {

                    cubicMode++;
                    if (cubicMode >= cubicModes.size()) {
                        cubicMode = 0;
                    }

                    CubicMode mode = cubicModes.get(cubicMode);
                    panel.setCubicModeText(mode.toString());

                    solids.remove(curve);

                    Mat4 typeMatrix = switch (mode) {
                        case FERGUSON -> Cubic.FERGUSON;
                        case COONS -> Cubic.COONS;
                        default -> Cubic.BEZIER;
                    };

                    curve = new Curve(typeMatrix, p1, p2, p3, p4);
                    curve.setModel(new Mat4Transl(2, 0, 0));
                    solids.add(curve);
                }

                if (e.getKeyCode() == KeyEvent.VK_E) {
                    editPointMode = !editPointMode;
                    panel.setEditModeText(String.valueOf(editPointMode).toUpperCase());
                }

                if (editPointMode) {
                    if (e.getKeyCode() == KeyEvent.VK_1) activePointIndex = 0;
                    if (e.getKeyCode() == KeyEvent.VK_2) activePointIndex = 1;
                    if (e.getKeyCode() == KeyEvent.VK_3) activePointIndex = 2;
                    if (e.getKeyCode() == KeyEvent.VK_4) activePointIndex = 3;

                    Point3D activePoint = switch (activePointIndex) {
                        case 1 -> p2;
                        case 2 -> p3;
                        case 3 -> p4;
                        default -> p1;
                    };

                    if (e.getKeyCode() == KeyEvent.VK_LEFT)  activePoint = activePoint.add(new Point3D(-0.1, 0, 0));
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) activePoint = activePoint.add(new Point3D(0.1, 0, 0));
                    if (e.getKeyCode() == KeyEvent.VK_UP)    activePoint = activePoint.add(new Point3D(0, 0.1, 0));
                    if (e.getKeyCode() == KeyEvent.VK_DOWN)  activePoint = activePoint.add(new Point3D(0, -0.1, 0));
                    if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)   activePoint = activePoint.add(new Point3D(0, 0, step));
                    if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) activePoint = activePoint.add(new Point3D(0, 0, -step));

                    switch (activePointIndex) {
                        case 0 -> p1 = activePoint;
                        case 1 -> p2 = activePoint;
                        case 2 -> p3 = activePoint;
                        case 3 -> p4 = activePoint;
                    }

                    updateCurve();

                   panel.setEditModeText(String.valueOf(editPointMode).toUpperCase() + " P" + (activePointIndex + 1) + " " + activePoint.toString());

                   drawScene();
                   return;
                }

                drawScene();
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();

        renderer.setView(camera.getViewMatrix());
        renderer.setProj(proj);

        renderer.renderSolid(axisX);
        renderer.renderSolid(axisY);
        renderer.renderSolid(axisZ);

        renderer.renderSolids(solids);

        panel.repaint();
    }

    private void updateCurve() {
        Mat4 oldModel = curve.getModel();
        solids.remove(curve);

        Controller3D.CubicMode mode = cubicModes.get(cubicMode);
        Mat4 typeMatrix = switch (mode) {
            case FERGUSON -> Cubic.FERGUSON;
            case COONS -> Cubic.COONS;
            default -> Cubic.BEZIER;
        };

        curve = new Curve(typeMatrix, p1, p2, p3, p4);

        curve.setModel(oldModel);

        solids.add(curve);
    }


}
