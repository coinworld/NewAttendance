package me.blog.tastedroid.attendance.model;

public class SimpleTaskInfo implements TaskInfo {

    private final Runnable runnable;
    private final int delaySec;

    public SimpleTaskInfo(Runnable runnable, int delaySec) {
        this.runnable = runnable;
        this.delaySec = delaySec;
    }

    @Override
    public Runnable getRunnable() {
        return runnable;
    }

    @Override
    public int getDelaySeconds() {
        return delaySec;
    }

}
