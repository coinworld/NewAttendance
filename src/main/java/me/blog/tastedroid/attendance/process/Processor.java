package me.blog.tastedroid.attendance.process;

import me.blog.tastedroid.attendance.model.Msg;
import me.blog.tastedroid.attendance.model.UserInfo;

import java.io.InputStream;

public interface Processor {

    public void launchCommand(String[] commands, String userName);

    public void sendMessages(UserInfo user, Msg... msg); // 각각의 User 인터페이스 구현한 거에 메시지 보내는 거 넣는다던가..

    public boolean isOnline(UserInfo user);

    public InputStream getResource(String fileName) throws Exception;

    public AttendTasks getTaskManager();

    public String getName(UserInfo user);

    public default Behaviors getBehaviors() {
        return new DefaultBehaviors();
    }
} // 에서 AttendTask 얻는 함수 넣는 건 어떨까..
