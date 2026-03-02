package controller;

import model.Vertex;
import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.TriangleRasterizer;
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
    private final ZBuffer zBuffer;
    // Rasterizers
    private LineRasterizer lineRasterizer;
    private TriangleRasterizer triangleRasterizer;
    // Renderers
    private final Renderer renderer;
    // Solids
    private List<Solid> solids = new ArrayList<>();
    Solid axisX, axisY, axisZ, cube, cone;


    private int activeSolidIndex = 0;

    private int oldX, oldY;

    private Camera camera;
    private Mat4 proj;

    private boolean isOrth = false;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        this.lineRasterizer = new LineRasterizerTrivial(panel.getRaster());
        this.triangleRasterizer = new TriangleRasterizer(zBuffer);

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

        cube = new Cube();
        cube.setModel(new Mat4Transl(-0.7, 0.5, 0.5));
        solids.add(cube);

        cone = new Cone();
        cone.setModel(new Mat4Transl(0.7, 0.5, 0));
        solids.add(cone);

        initListeners();

        drawScene();

    }

    private void initListeners() {
        //rozhlizeni mysi
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
                //aktivni teleso
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
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(-step, 0, 0)));
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(step, 0, 0)));
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, step, 0)));
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, -step, 0)));
                if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
                    solids.get(activeSolidIndex).setModel(solids.get(activeSolidIndex).getModel().mul(new Mat4Transl(0, 0, step)));
                if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
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

                //perspective
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    if(isOrth){
                        proj = new Mat4PerspRH(
                                Math.toRadians(90),
                                panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(), 0.1,100);
                        isOrth = false;
                    }
                    else{
                        double aspect = (double) panel.getRaster().getWidth() / panel.getRaster().getHeight();
                        proj = new Mat4OrthoRH(5 * aspect, 5,0.1,200);
                        isOrth = true;
                    }

                }
                // camera wasd
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

//        renderer.renderSolids(solids);

        panel.repaint();
    }




}
