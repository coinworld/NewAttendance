package me.blog.tastedroid.attendance.process;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.Status;
import me.blog.tastedroid.attendance.model.UserInfo;

import java.util.Calendar;
import java.util.TimeZone;

public class Tools {

    private static TimeZone timezone = TimeZone.getTimeZone("Asia/Seoul");

    public static void setTimeZone(TimeZone zone) {
        timezone = zone;
    }

    public static boolean isSameDay(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance(timezone);
        Calendar cal2 = Calendar.getInstance(timezone);
        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static long getMidnightOfToday() {
        Calendar date = Calendar.getInstance(timezone);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTimeInMillis();
    }

    public static final int A_DAY = 86400000;

    public static boolean isAlreadyCheckedToday(UserInfo user) {
        return isSameDay(user.getLastCheck(), System.currentTimeMillis());
    }

    public static Status checkStatus(UserInfo user) {
        long start = System.currentTimeMillis();
        if (!Tools.isSameDay(start, user.getLastJoin())) {
            return Status.OTHER_DAY; // 들어온 날이 오늘과 같은지
        }

        if (Tools.isSameDay(start, user.getLastCheck())) {
            return Status.ALREADY_CHECKED; // 마지막 확인과 오늘이 같은지
        }

        long gap = start - user.getLastJoin();
        long time = AttendServices.getFileManager().getConfig().getValues().getCheckTime() * 1000L;

        if (gap >= 86400000 + (time * 2)) {
            return Status.TWO_DAYS; // 2일 연속
        }

        if (gap >= time) {
            return Status.SUCCESS; // 성공
        } else {
            return Status.FAIL; // 실패
        }
    }
}
