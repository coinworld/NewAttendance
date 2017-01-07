package me.blog.tastedroid.attendance.model;

import java.util.UUID;

public interface UserInfo {

    public UUID getUUID();

    public void setUUID(UUID uuid); // 개선 필요 있음. 이 메서드는 인터페이스에서 제공되면 안 좋을 거 같음.

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
