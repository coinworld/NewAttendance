package me.blog.tastedroid.attendance.process.bukkit;

import me.blog.tastedroid.attendance.model.SimpleTaskInfo;
import me.blog.tastedroid.attendance.model.TaskInfo;
import org.bukkit.scheduler.BukkitTask;

public class BukkitTaskInfo extends SimpleTaskInfo {

    public BukkitTaskInfo(Runnable runnable, int delaySec, BukkitTask task) {
        super(runnable, delaySec);
        this.bukkitTask = task;
    }

    public BukkitTaskInfo(TaskInfo info, BukkitTask task) {
        this(info.getRunnable(), info.getDelaySeconds(), task);
    }

    private final BukkitTask bukkitTask;

    public void cancel() {
        bukkitTask.cancel();
    }
}
