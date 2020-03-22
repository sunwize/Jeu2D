package engine.display;

import engine.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class Display {

    public double ppc, ppl;

    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy bs;
    private Renderer renderer;

    public Display(String title) {
        renderer = new Renderer(this);

        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
        canvas.setMaximumSize(new Dimension(frame.getWidth(), frame.getHeight()));
        canvas.setMinimumSize(new Dimension(frame.getWidth(), frame.getHeight()));
        canvas.setFocusable(true);

        ppc = frame.getWidth() * 1.0 / Constants.MAP_GRID_COLUMNS;
        ppl = frame.getHeight() * 1.0 / Constants.MAP_GRID_ROWS;

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(e.getComponent().getWidth() < Toolkit.getDefaultToolkit().getScreenSize().width)
                    frame.setBounds(e.getComponent().getX(), e.getComponent().getY(), e.getComponent().getWidth(), (int) (e.getComponent().getWidth() / Constants.ASPECT_RATIO));
                ppc = frame.getWidth() * 1.0 / Constants.MAP_GRID_COLUMNS;
                ppl = frame.getHeight() * 1.0 / Constants.MAP_GRID_ROWS;
                canvas.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
            }
        });

        frame.add(canvas);
        frame.pack();
    }

    public void preRender() {
        bs = canvas.getBufferStrategy();

        if(bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        canvas.setSize(new Dimension(frame.getWidth(), frame.getHeight()));

        renderer.setGraphics(bs.getDrawGraphics());
    }

    public void postRender() {
        if(bs == null)
            return;

        bs.show();
        renderer.dispose();
    }

    public void dispose() {
        frame.dispose();
        bs.dispose();
        renderer.dispose();
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    };

}
