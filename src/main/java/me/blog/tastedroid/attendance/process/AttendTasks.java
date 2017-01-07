package me.blog.tastedroid.attendance.process;

import me.blog.tastedroid.attendance.config.Reloadable;
import me.blog.tastedroid.attendance.model.TaskInfo;
import me.blog.tastedroid.attendance.model.UserInfo;

import java.util.Map;

public interface AttendTasks extends Reloadable {

    public default void runTask(UserInfo user, Runnable runnable, int delaySeconds) {
        stopTask(user);
        runTaskWithoutStopping(user, runnable, delaySeconds);
    }

    public void runTaskWithoutStopping(UserInfo user, Runnable runnable, int delaySeconds);

    public void stopTask(UserInfo user);

    public boolean isPending(UserInfo user);

    public Map<UserInfo, ? extends TaskInfo> getTaskMap() throws UnsupportedOperationException;

    public void scheduleAsync(Runnable runnable, int repeatSecond);
}
