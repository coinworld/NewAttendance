package me.blog.tastedroid.attendance.process;

import me.blog.tastedroid.attendance.model.UserInfo;

public interface Behaviors {

    public Runnable whenLogin(UserInfo user);

    public Runnable whenLogout(UserInfo user);
}
