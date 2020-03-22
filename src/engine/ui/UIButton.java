package engine.ui;

import engine.display.Display;
import engine.display.Renderer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

public class UIButton extends UIComponent implements MouseListener, MouseMotionListener {

    private boolean hover;
    private String text;
    private Component parent;
    private Display display;
    private LinkedList<Runnable> onClick;

    public UIButton(Component parent, Display display, String text) {
        super();
        this.parent = parent;
        this.display = display;
        this.text = text;
        onClick = new LinkedList<>();
        super.setBounds(0, 0, 5, 5);
        parent.addMouseListener(this);
        parent.addMouseMotionListener(this);
    }

    public void onClick(Runnable task) {
        onClick.add(task);
    }

    public void add(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    @Override
    public void render(Renderer renderer) {
        if(hover)
            renderer.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height, Color.RED, true);
        else
            renderer.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height, Color.GREEN, true);

        renderer.drawString(text, getBounds().getCenterX(), getBounds().getCenterY(), Color.BLACK, true, 4);
    }

    @Override
    public void dispose() {
        parent.removeMouseListener(this);
        parent.removeMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(hover) {
            for(Runnable task : onClick)
                task.run();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = (int) (e.getX() / display.ppc);
        int y = (int) (e.getY() / display.ppl);
        if(getBounds().contains(x, y))
            hover = true;
        else
            hover = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = (int) (e.getX() / display.ppc);
        int y = (int) (e.getY() / display.ppl);
        if(getBounds().contains(x, y))
            hover = true;
        else
            hover = false;
    }
}
