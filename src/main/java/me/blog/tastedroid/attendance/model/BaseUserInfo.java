package me.blog.tastedroid.attendance.model;

public abstract class BaseUserInfo implements UserInfo {

    private int count = 0;
    private long lastjoin = 0;
    private long lastcheck = 0;
    private long lastquit = 0;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public long getLastJoin() {
        return lastjoin;
    }

    @Override
    public void setLastJoin(long time) {
        lastjoin = time;
    }

    @Override
    public long getLastCheck() {
        return lastcheck;
    }

    @Override
    public void setLastCheck(long time) {
        lastcheck = time;
    }

    @Override
    public long getLastQuit() {
        return lastquit;
    }

    @Override
    public void setLastQuit(long time) {
        lastquit = time;
    }

    @Override
    public void reset() {
        count = 0;
        lastjoin = 0;
        lastcheck = 0;
        lastquit = 0;
    } // 생성자에 넣어야 하나

}
