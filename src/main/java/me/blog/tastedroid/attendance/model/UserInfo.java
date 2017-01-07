package me.blog.tastedroid.attendance.model;

import java.util.UUID;

public interface UserInfo {

    public UUID getUUID();

    public void setUUID(UUID uuid);

    public int getCount();

    public void setCount(int count);

    public long getLastJoin();

    public void setLastJoin(long time);

    public long getLastCheck();

    public void setLastCheck(long time);

    public long getLastQuit();

    public void setLastQuit(long time);

    public void reset();
}
