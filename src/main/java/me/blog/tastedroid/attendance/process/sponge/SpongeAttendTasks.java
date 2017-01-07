package me.blog.tastedroid.attendance.process.sponge;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.TaskInfo;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.AttendTasks;
import org.spongepowered.api.scheduler.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SpongeAttendTasks implements AttendTasks {

    private final Map<UserInfo, Task> taskMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void runTaskWithoutStopping(UserInfo user, Runnable runnable, int delaySec) {
        Task task = Task.builder().execute(() -> {
            runnable.run();
            taskMap.remove(user);
        }).delay(delaySec, TimeUnit.SECONDS).submit(AttendServices.getMain());

        taskMap.put(user, task);
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
    public Map<UserInfo, TaskInfo> getTaskMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("No need to be implemented.");
    }

    @Override
    public void reload() {
        taskMap.keySet().forEach(this::stopTask);
    }

    @Override
    public void scheduleAsync(Runnable runnable, int repeatSecond) {
        Task.builder().interval(repeatSecond, TimeUnit.SECONDS).async().execute(runnable).submit(AttendServices.getMain());
    }

}
