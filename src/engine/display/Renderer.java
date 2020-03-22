package engine.display;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Renderer {

    private Graphics g;
    private Display display;

    public Renderer(Display display) {
        this.display = display;
    }

    public void dispose() {
        if(g != null)
            g.dispose();
    }

    public void drawImage(BufferedImage image, int cell, double cx, double cy, boolean flipX) {
        if(g == null)
            return;

        if(!flipX)
            drawImage(image, cell, cx, cy);
        else {
            double ratio = image.getWidth() * 1.0 / image.getHeight();
            int width = (int) (cell * display.ppc);
            int height = (int) (width / ratio);
            int x = (int) (cx * display.ppc + width);
            int y = (int) (cy * display.ppl);

            g.drawImage(image, x, y, -width, height, null);
        }
    }

    public void drawImage(BufferedImage image, int cell, double cx, double cy) {
        if(g == null)
            return;

        double ratio = image.getWidth() * 1.0 / image.getHeight();
        int width = (int) (cell * display.ppc);
        int height = (int) (width / ratio);
        int x = (int) (cx * display.ppc);
        int y = (int) (cy * display.ppl);

        g.drawImage(image, x, y, width, height, null);
    }

    public void drawImage(BufferedImage image, int cell, double cx, double cy, boolean centerX, boolean centerY) {
        if(g == null)
            return;

        double ratio = image.getWidth() * 1.0 / image.getHeight();
        int width = (int) (cell * display.ppc);
        int height = (int) (width / ratio);
        int x, y;
        if(centerX)
            x = (int) (cx * display.ppc - width/2);
        else
            x = (int) (cx * display.ppc);
        if(centerY)
            y = (int) (cy * display.ppl - height/2);
        else
            y = (int) (cy * display.ppl);

        g.drawImage(image, x, y, width, height, null);
    }

    public void drawPolygon(int cxPoints[], int cyPoints[], int nPoints, Color color, boolean fill) {
        if(g == null)
            return;

        g.setColor(color);
        int xPoints[] = new int[nPoints];
        int yPoints[] = new int[nPoints];
        for(int i = 0; i < nPoints; i++) {
            xPoints[i] = (int) (cxPoints[i] * display.ppc);
            yPoints[i] = (int) (cyPoints[i] * display.ppl);
        }

        if(fill) {
            g.fillPolygon(xPoints, yPoints, nPoints);
            return;
        }
        g.drawPolygon(xPoints, yPoints, nPoints);
    }

    public void drawIsoTile(Polygon tile, Color color, boolean fill) {
        if(g == null)
            return;

        g.setColor(color);
        int xPoints[] = new int[tile.npoints];
        int yPoints[] = new int[tile.npoints];
        for(int i = 0; i < tile.npoints; i++) {
            xPoints[i] = (int) ((tile.xpoints[i]) * display.ppc);
            yPoints[i] = (int) ((tile.ypoints[i]) * display.ppl);
        }

        if(fill) {
            g.fillPolygon(xPoints, yPoints, tile.npoints);
            return;
        }
        g.drawPolygon(xPoints, yPoints, tile.npoints);
    }

    public void drawString(String text, double cx, double cy, Color color, boolean centered, int fontSize) {
        if(g == null)
            return;

        int x = (int) (cx * display.ppc);
        int y = (int) (cy * display.ppl);
        int fs = (int) (fontSize * display.ppc);

        g.setColor(color);
        Font myFont = new Font("Helvetica", Font.PLAIN, fs);
        g.setFont(myFont);
        if(centered)
            g.drawString(text, x - g.getFontMetrics().stringWidth(text)/2, y + g.getFontMetrics().getHeight()/4);
        else
            g.drawString(text, x, y);
    }

    public void drawLine(Line2D line, Color color) {
        if(g == null)
            return;

        g.setColor(color);
        g.drawLine((int) (line.getX1() * display.ppc),(int) (line.getY1() * display.ppl),(int) (line.getX2() * display.ppc),(int) (line.getY2() * display.ppl));
    }

    public void drawRect(double cx, double cy, double cwidth, double cheight, Color color, boolean fill) {
        if(g == null)
            return;

        int x = (int) (cx * display.ppc);
        int y = (int) (cy * display.ppl);
        int width = (int) (cwidth * display.ppc);
        int height = (int) (cheight * display.ppl);

        g.setColor(color);

        if(fill)
            g.fillRect(x, y, width, height);
        else
            g.drawRect(x, y, width, height);
    }

    public void drawOval(double cx, double cy, double cwidth, double cheight, Color color) {
        if(g == null)
            return;

        int x = (int) (cx * display.ppc);
        int y = (int) (cy * display.ppl);
        int width = (int) (cwidth * display.ppc);
        int height = (int) (cheight * display.ppl);

        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    public void background(Color color) {
        if(g == null)
            return;

        g.setColor(color);
        g.fillRect(0, 0, display.getFrame().getWidth(), display.getFrame().getHeight());
    }

    public void setGraphics(Graphics g) {
        this.g = g;
    }

}
