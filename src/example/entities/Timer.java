package example.entities;

public class Timer {

    private long duration, time, startTime;
    private Runnable callback;
    private Thread thread;
    private volatile boolean running;

    public Timer(long duration, Runnable callback) {
        this.duration = duration;
        this.callback = callback;
        startTime = System.currentTimeMillis();
        time = startTime;
        running = true;

        thread = new Thread(() -> {
                while(running && time - startTime < duration) {
                    time = System.currentTimeMillis();
                }

                if(time - startTime >= duration)
                    callback.run();
            }
        );

        thread.start();
    }

    public void stop() {
        running = false;
    }

}
