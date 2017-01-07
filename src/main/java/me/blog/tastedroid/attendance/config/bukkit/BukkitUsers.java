package me.blog.tastedroid.attendance.config.bukkit;

import com.google.gson.reflect.TypeToken;
import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.config.BaseUsers;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.model.bukkit.BukkitUserInfo;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public final class BukkitUsers extends BaseUsers {

    @Override
    public File getFile() {
        return new File(AttendServices.getFileManager().getDataDir(), "users.json");
    }

    @Override
    protected UserInfo getEmptyUser(UUID uuid) {
        return new BukkitUserInfo(uuid);
    }

    @Override
    protected Type getMapType() {
        return new TypeToken<Map<UUID, BukkitUserInfo>>() {
        }.getType();
    }

}
