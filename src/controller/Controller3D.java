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

public class Controller3D {

    private final Panel panel;
    // Rasterizers
    private LineRasterizer lineRasterizer;
    // Renderers
    private Renderer renderer;
    // Solids
    private Solid axisX, axisY, axisZ, arrow, cube, cone, curve;

    private int oldX, oldY;

    private Camera camera;
    private Mat4 proj;

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

        // Init solids
        axisX = new AxisX();
        axisY = new AxisY();
        axisZ = new AxisZ();
        arrow = new Arrow();
        cube = new Cube();
        cone = new Cone();
        curve = new Curve();
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
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    arrow.setModel(arrow.getModel().mul(new Mat4Transl(-0.5, 0, 0)));
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    arrow.setModel(arrow.getModel().mul(new Mat4Transl(0.5, 0, 0)));

                if (e.getKeyCode() == KeyEvent.VK_R) {
                    Mat4 model = new Mat4Transl(-0.25, 0, 0)
                            .mul(new Mat4RotZ(Math.toRadians(15)))
                            .mul(new Mat4Transl(0.25, 0, 0));
                    arrow.setModel(arrow.getModel().mul(model));
                }

                if (e.getKeyCode() == KeyEvent.VK_P) {
                    proj = new Mat4PerspRH(
                            Math.toRadians(90),
                            panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(),
                            0.1,
                            100);
                }

                if (e.getKeyCode() == KeyEvent.VK_O) {
                    double aspect = (double) panel.getRaster().getWidth() / panel.getRaster().getHeight();
                    double zoom = 5.0;
                    proj = new Mat4OrthoRH(
                            zoom * aspect,
                            zoom,
                            0.1,
                            200
                    );
                }

                if(e.getKeyCode() == KeyEvent.VK_W)
                    camera = camera.forward(0.5);
                if(e.getKeyCode() == KeyEvent.VK_A)
                    camera = camera.left(0.5);
                if(e.getKeyCode() == KeyEvent.VK_S)
                    camera = camera.backward(0.5);
                if(e.getKeyCode() == KeyEvent.VK_D)
                    camera = camera.right(0.5);

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

        renderer.renderSolid(arrow);

        renderer.renderSolid(cube);

        renderer.renderSolid(cone);

        renderer.renderSolid(curve);

        panel.repaint();
    }


}
