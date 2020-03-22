package example.states;

import engine.display.Renderer;
import engine.managers.EntityManager;
import engine.managers.UIManager;
import engine.states.IState;
import engine.ui.UIButton;
import example.Game;
import example.entities.Pokemon;
import example.map.IsoMap;
import example.map.IsoTile;
import example.utils.Cursors;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Menu implements IState {

    private Game game;
    private EntityManager entityManager;
    private UIManager uiManager;
    private IsoMap isoMap;
    private Pokemon mover;

    public Menu(Game game) {
        this.game = game;
        entityManager = new EntityManager();
        uiManager = new UIManager();

        game.getDisplay().getFrame().setCursor(Cursors.standard);

        isoMap = new IsoMap(game, 6, 6, 80, 8, 2);

        Pokemon entei = new Pokemon("entei", isoMap, 0, 0);
        Pokemon entei2 = new Pokemon("entei", isoMap, 0, 0);
        Pokemon raikou = new Pokemon("raikou", isoMap, 0, 0);
        Pokemon raikou2 = new Pokemon("raikou", isoMap, 0, 0);
        Pokemon suicune = new Pokemon("suicune", isoMap, 0, 0);
        Pokemon suicune2 = new Pokemon("suicune", isoMap, 0, 0);
        entei.placeOnTile(2, 2);
        entei2.placeOnTile(2, 1);
        raikou.placeOnTile(3, 2);
        raikou2.placeOnTile(3, 1);
        suicune.placeOnTile(4, 2);
        suicune2.placeOnTile(4, 1);
        entityManager.add(entei);
        entityManager.add(entei2);
        entityManager.add(raikou);
        entityManager.add(raikou2);
        entityManager.add(suicune);
        entityManager.add(suicune2);
        mover = entei;

        game.inputs().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point p = new Point((int) (e.getX() / game.getDisplay().ppc), (int) (e.getY() / game.getDisplay().ppl));

                if(e.getButton() == MouseEvent.BUTTON1) {
                    for(int i = 0; i < isoMap.getTiles().length; i++) {
                        for(int j = 0; j < isoMap.getTiles()[0].length; j++) {
                            IsoTile tile = isoMap.getTiles()[i][j];
                            if(tile.isoBounds.contains(p)) { // Select mover
                                if(tile.taken != null)
                                    mover = tile.taken;
                            }
                        }
                    }
                }

                if(e.getButton() == MouseEvent.BUTTON3) { // Mover find path
                    for(int i = 0; i < isoMap.getTiles().length; i++) {
                        for(int j = 0; j < isoMap.getTiles()[0].length; j++) {
                            if(isoMap.getTiles()[i][j].isoBounds.contains(p)) {
                                mover.getPathfinder().findPath(j, i);
                            }
                        }
                    }
                }
            }
        });

        game.inputs().addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point p = new Point((int) (e.getX() / game.getDisplay().ppc), (int) (e.getY() / game.getDisplay().ppl));
                for(int i = 0; i < isoMap.getTiles().length; i++) {
                    for(int j = 0; j < isoMap.getTiles()[0].length; j++) {
                        if(isoMap.getTiles()[i][j].isoBounds.contains(p)) {
                            isoMap.getTiles()[i][j].hover = true;
                            if(isoMap.getTiles()[i][j].taken != null)
                                game.getDisplay().getFrame().setCursor(Cursors.select);
                            else
                                game.getDisplay().getFrame().setCursor(Cursors.standard);
                        }
                        else {
                            isoMap.getTiles()[i][j].hover = false;
                        }
                    }
                }
            }
        });

        UIButton button = new UIButton(game.inputs(), game.getDisplay(), "Start");
        button.setBounds(5, 5, 10, 6);
        uiManager.add(button);

    }

    @Override
    public void update() {
        entityManager.update();
        uiManager.update();
    }

    @Override
    public void render(Renderer renderer) {
        renderer.background(new Color(13, 31, 34));
        isoMap.render(renderer);
        renderer.drawIsoTile(mover.getTile().isoBounds, Color.GREEN, false);
        entityManager.render(renderer);
        uiManager.render(renderer);
    }

    @Override
    public void dispose() {
        entityManager.dispose();
        game.clearInputs();
    }

}
