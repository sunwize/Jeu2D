package engine.utils;

public class Clock {

    private long time;

    public Clock() {
        time = System.currentTimeMillis();
    }

    public long elapsedTime() {
        return System.currentTimeMillis() - time;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

}
