package me.blog.tastedroid.attendance.config;

import me.blog.tastedroid.attendance.model.UserInfo;

import java.util.Map;
import java.util.UUID;

public interface Users extends FileInterface {

    public UserInfo getUser(UUID uuid);

    public void resetUser(UUID uuid);

    public Map<UUID, UserInfo> getUserMap();
}
