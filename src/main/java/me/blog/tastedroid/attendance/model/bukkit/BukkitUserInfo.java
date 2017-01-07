package me.blog.tastedroid.attendance.model.bukkit;

import me.blog.tastedroid.attendance.model.BaseUserInfo;
import me.blog.tastedroid.attendance.model.sponge.SpongeUserInfo;

import java.util.UUID;

public final class BukkitUserInfo extends BaseUserInfo {

    private transient UUID uuid;

    public BukkitUserInfo(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return ("attend:" + uuid).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BukkitUserInfo)) {
            return false;
        }

        BukkitUserInfo user = (BukkitUserInfo) o;

        return this.getUUID().equals(user.getUUID());
    }

}
