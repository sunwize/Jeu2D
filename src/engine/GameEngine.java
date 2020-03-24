package engine;

public class GameEngine implements Runnable {

    public static int FPS = 0;

    private Thread thread;
    private IGame game;
    private boolean running = false;
    public double UPDATE_CAP = 1.0 / 300.0;

    public GameEngine(IGame game) {
        this.game = game;
        start();
    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }

    @Override
    public void run() {
        running = true;
        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double lt = System.nanoTime();
        double t;

        double frameTime = 0;
        int frames = 0;

        while(running) {
            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            //TODO: Update game
            t = System.nanoTime();
            if(t - lt > 10000000) {
                game.update();
                lt = t;
            }

            while(unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                if(frameTime >= 1.0) {
                    frameTime = 0;
                    FPS = frames;
                    frames = 0;
                    // System.out.println("FPS: " + FPS);
                }
            }

            if(render) {
                frames++;
                //TODO: Render the game
                game.render();
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        dispose();
    }

    public void dispose() {
        //TODO: Dispose the game
        game.dispose();
    }

}
