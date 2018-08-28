package engine.Util;

public class Timer {
    public double lastTimeStamp;

    public void init() {
        lastTimeStamp = getTime();
    }

    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

}