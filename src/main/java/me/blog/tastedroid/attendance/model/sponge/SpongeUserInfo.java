package me.blog.tastedroid.attendance.model.sponge;

import me.blog.tastedroid.attendance.model.BaseUserInfo;

import java.util.UUID;

public final class SpongeUserInfo extends BaseUserInfo {

    private transient UUID uuid;

    public SpongeUserInfo(UUID uuid) {
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
        if (!(o instanceof SpongeUserInfo)) {
            return false;
        }

        SpongeUserInfo user = (SpongeUserInfo) o;

        return user.getUUID().equals(this.getUUID());
    }

}
