package controller;


import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerTrivial;
import rasterize.TriangleRasterizer;
import render.Renderer;
import render.RendererSolid;
import render.RendererWire;
import shader.ShaderConstant;
import shader.ShaderInterpolated;
import shader.ShaderPhong;
import shader.ShaderTexture;
import solid.*;
import transforms.*;
import view.Panel;

import javax.imageio.ImageIO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final ZBuffer zBuffer;
    // Renderers
    private final Renderer rendererWire;
    private final Renderer rendererSolid;

    // Solids
    Solid axisX, axisY, axisZ, cube, sphere, cone, lightSource, arrow;
    private final List<Solid> solids = new ArrayList<>();
    private int activeSolidIndex = 0;

    private int oldX, oldY;

    private Camera camera;
    private Mat4 proj;

    private boolean isOrth = false;
    private boolean isWireframe = false;

    private final BufferedImage sandstone;
    private final BufferedImage brick;
    private final BufferedImage water;

    private Col lightColor = new Col(255, 0, 0);
    private int lightColorInt;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        // Rasterizers
        LineRasterizer lineRasterizer = new LineRasterizerTrivial(zBuffer);
        TriangleRasterizer triangleRasterizer = new TriangleRasterizer(zBuffer);


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

        this.rendererWire = new RendererWire(
                lineRasterizer, triangleRasterizer,
                panel.getRaster().getWidth(),
                panel.getRaster().getHeight(),
                camera.getViewMatrix(),
                proj
        );
        this.rendererSolid = new RendererSolid(
                lineRasterizer, triangleRasterizer,
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
        cube.setModel(new Mat4Transl(0, 1.5, 0));
        cube.setModel(new Mat4Scale(0.5).mul(cube.getModel()));

        sphere = new Sphere();
        sphere.setModel(new Mat4Transl(-1.5, 1.5, 0));
        sphere.setModel(new Mat4Scale(0.5).mul(sphere.getModel()));

        cone = new Cone();
        cone.setModel(new Mat4Transl(1.5, 1.5, -0.5));
        cone.setModel(new Mat4Scale(0.5).mul(cone.getModel()));

        arrow = new Arrow();
        arrow.setModel(new Mat4Transl(0, -0.5, 0));
        arrow.setModel(new Mat4Scale(0.5).mul(arrow.getModel()));

        lightSource = new Sphere();
        lightSource.setModel(new Mat4Transl(1, 1, 4));
        lightSource.setModel(new Mat4Scale(0.2).mul(lightSource.getModel()));

        solids.add(cube);
        solids.add(sphere);
        solids.add(cone);
        solids.add(lightSource);
        solids.add(arrow);


        try {
            sandstone = ImageIO.read(new File("./res/textures/sandstone" + ".jpg"));
            brick = ImageIO.read(new File("./res/textures/brick" + ".jpg"));
            water = ImageIO.read(new File("./res/textures/water" + ".jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lightSource.setShader(new ShaderConstant(lightColor));
        cube.setShader(new ShaderTexture(brick));
        sphere.setShader(new ShaderTexture(water));
        cone.setShader(new ShaderTexture(sandstone));
        axisX.setShader(new ShaderConstant(new Col(0xff0000)));
        axisY.setShader(new ShaderConstant(new Col(0x00ff00)));
        axisZ.setShader(new ShaderConstant(new Col(0x0000ff)));


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
                        proj = new Mat4PerspRH(Math.toRadians(90),
                                panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(), 0.1,100);
                    }
                    else{
                        double aspect = (double) panel.getRaster().getWidth() / panel.getRaster().getHeight();
                        proj = new Mat4OrthoRH(5 * aspect, 5,0.1,200);
                    }
                    isOrth = !isOrth;
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

                if (e.getKeyCode() == KeyEvent.VK_M) {
                    isWireframe = !isWireframe;
                }

                if (e.getKeyCode() == KeyEvent.VK_T) {
                    Solid activeSolid = solids.get(activeSolidIndex);

                    if (activeSolid != lightSource) {
                        if (activeSolid.getShader() instanceof ShaderTexture) {
                            activeSolid.setShader(new ShaderPhong());
                        }
                        else if (activeSolid.getShader() instanceof ShaderPhong) {
                            activeSolid.setShader(new ShaderInterpolated());
                        }
                        else {
                            if (activeSolid instanceof Sphere)
                                activeSolid.setShader(new ShaderTexture(water));
                            else if (activeSolid instanceof Cone)
                                activeSolid.setShader(new ShaderTexture(sandstone));
                            else
                                activeSolid.setShader(new ShaderTexture(brick));
                        }
                    }
                    else{
                        lightColorInt++;
                        if (lightColorInt >= 3) {
                            lightColorInt = 0;
                        }
                        switch (lightColorInt) {
                            case 0:
                                lightColor = new Col(255, 0, 0);
                                break;
                            case 1:
                                lightColor = new Col(0, 255, 0);
                                break;
                            case 2:
                            lightColor = new Col(0, 0, 255);
                            break;
                        }
                        lightSource.setShader(new ShaderConstant(lightColor));
                    }
                }
                drawScene();
            }
        });
    }

    private void drawScene() {
        panel.getRaster().clear();
        zBuffer.clear();

        Point3D currentLightPos = new Point3D(0, 0, 0).mul(lightSource.getModel());
        Mat4 currentView = camera.getViewMatrix();

        for (Solid solid : solids) {
            if (solid.getShader() instanceof ShaderPhong) {
                ((ShaderPhong) solid.getShader()).updateLight(currentLightPos, currentView, lightColor);
            }
        }
        rendererWire.setView(camera.getViewMatrix());
        rendererWire.setProj(proj);
        rendererSolid.setView(camera.getViewMatrix());
        rendererSolid.setProj(proj);

        rendererSolid.render(axisX);
        rendererSolid.render(axisY);
        rendererSolid.render(axisZ);

        for (Solid solid : solids) {
            if (isWireframe) {
                rendererWire.render(solid);
            } else {
                rendererSolid.render(solid);
                if (solids.indexOf(solid) == activeSolidIndex) {
                    rendererWire.render(solid);
                }
            }
        }


        panel.repaint();
    }




}
