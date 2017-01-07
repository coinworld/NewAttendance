package me.blog.tastedroid.attendance.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigValues {

    private int checktime = 1800;
    private boolean launchinstantly = true;
    private Map<Integer, String[]> rules = new HashMap<>();

    {
        rules.put(-1, new String[]{"say one"});
        rules.put(-2, new String[]{"say two", "say test"});
    }

    private int rankinginterval = 1800;
    private int perpage = 9;
    private boolean showdays = true;

    public ConfigValues() {
    }

    public int getCheckTime() {
        return checktime;
    }

    public void setCheckTime(int checktime) {
        this.checktime = checktime;
    }

    public boolean launchInstantly() {
        return launchinstantly;
    }

    public void setLaunchInstantly(boolean launchinstantly) {
        this.launchinstantly = launchinstantly;
    }

    public String[] getCommands(int num) {
        String[] arr = rules.get(num);
        if (arr == null) {
            arr = new String[0];
        }

        return arr;
    }

    public int getRankingRefreshInterval() {
        return rankinginterval;
    }

    public void setRankingRefreshInterval(int interval) {
        rankinginterval = interval;
    }

    public int getRankingPlayersPerPage() {
        return perpage;
    }

    public void setRankingPlayersPerPage(int num) {
        perpage = num;
    }

    public boolean showDays() {
        return showdays;
    }

    public void setShowDays(boolean value) {
        showdays = value;
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder(this.getClass().getSimpleName()).append('[');
        for (Field f : this.getClass().getDeclaredFields()) {
            String val = "ERROR";
            try {
                val = f.get(this).toString();
            } catch (Exception e) {
            }
            value.append(f.getName()).append('=').append(val).append(',');
        }

        return value.append(']').toString();
    }
}
