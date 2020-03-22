package engine.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController implements KeyListener {

    private final int NUM_KEYS = 300;
    private boolean KEYS_PRESSED[] = new boolean[NUM_KEYS];
    private boolean LAST_KEYS[] = new boolean[NUM_KEYS];
    private boolean WAIT_RELEASE[] = new boolean[NUM_KEYS];

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (NUM_KEYS > e.getKeyCode()) {
            KEYS_PRESSED[e.getKeyCode()] = true;
            LAST_KEYS[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (NUM_KEYS > e.getKeyCode()) {
            KEYS_PRESSED[e.getKeyCode()] = false;
            LAST_KEYS[e.getKeyCode()] = false;
            WAIT_RELEASE[e.getKeyCode()] = false;
        }
    }

    public boolean keyPressed(int keyCode) {
        if (NUM_KEYS > keyCode)
            return KEYS_PRESSED[keyCode];
        else
            return false;
    }

    public boolean keyPressedOnce(int keycode) {
        if (NUM_KEYS > keycode) {
            if (KEYS_PRESSED[keycode] && LAST_KEYS[keycode] && !WAIT_RELEASE[keycode]) {
                LAST_KEYS[keycode] = false;
                WAIT_RELEASE[keycode] = true;
                return true;
            } else
                return false;
        } else
            return false;
    }

}
