package view;

import raster.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private final RasterBufferedImage raster;

    //private String editModeText = "FALSE";


    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));

        raster = new RasterBufferedImage(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(raster.getImage(), 0, 0, null);

        //g.drawString("EDIT MODE: " + editModeText, 20, 50);
    }

    public RasterBufferedImage getRaster() {
        return raster;
    }

//    public void setEditModeText(String text) {
//        this.editModeText = text;
//    }
}
