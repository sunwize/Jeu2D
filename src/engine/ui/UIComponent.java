package engine.ui;

import engine.display.Renderer;

import java.awt.*;

public abstract class UIComponent extends Component {

    public UIComponent() {
        super();
    }

    public abstract void render(Renderer renderer);

    public abstract void dispose();

}
