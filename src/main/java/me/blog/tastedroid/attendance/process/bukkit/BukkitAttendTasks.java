package me.blog.tastedroid.attendance.process.bukkit;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.AttendTasks;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BukkitAttendTasks implements AttendTasks {

    private final Map<UserInfo, BukkitTaskInfo> taskMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void runTaskWithoutStopping(UserInfo user, Runnable runnable, int delaySeconds) {
        BukkitTask task = Bukkit.getScheduler().runTaskLater(
                getMain(), () -> {
                    runnable.run();
                    taskMap.remove(user);
                }, delaySeconds * 20L
        );

        BukkitTaskInfo taskInfo = new BukkitTaskInfo(runnable, (delaySeconds + 2), task);
        taskMap.put(user, taskInfo);
    }

    @Override
    public void stopTask(UserInfo user) {
        if (taskMap.containsKey(user)) {
            taskMap.get(user).cancel();
            taskMap.remove(user);
        }
    }

    @Override
    public boolean isPending(UserInfo user) {
        return taskMap.containsKey(user);
    }

    @Override
    public Map<UserInfo, BukkitTaskInfo> getTaskMap() throws UnsupportedOperationException {
        return Collections.unmodifiableMap(taskMap);
    }

    @Override
    public void reload() {
        taskMap.keySet().forEach(this::stopTask);
    }

    @Override
    public void scheduleAsync(Runnable runnable, int repeatSecond) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(getMain(), runnable, 0, repeatSecond * 20L);
    }

    private static Plugin getMain() {
        return ((Plugin) AttendServices.getMain());
    }
}
