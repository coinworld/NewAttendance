package me.blog.tastedroid.attendance.process;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.GlobalOptions;
import me.blog.tastedroid.attendance.config.ConfigValues;
import me.blog.tastedroid.attendance.model.Msg;
import me.blog.tastedroid.attendance.model.Status;
import me.blog.tastedroid.attendance.model.UserInfo;

public class DefaultBehaviors implements Behaviors {

    @Override
    public Runnable whenLogin(UserInfo user) {
        return () -> {
            if (Tools.isAlreadyCheckedToday(user)) {
                return;
            }

            user.setLastJoin(System.currentTimeMillis());

            Processor pro = AttendServices.getProcessor();
            AttendTasks tasks = pro.getTaskManager();
            ConfigValues conf = AttendServices.getFileManager().getConfig().getValues();

            if (tasks.isPending(user)) {
                tasks.stopTask(user);
            }

            tasks.runTask(user, () -> {
                if (GlobalOptions.DEBUG) {
                    System.out.println(pro.getName(user) + ": It's time!");
                }
                if (pro.isOnline(user)) {
                    Status status = Tools.checkStatus(user);
                    if (GlobalOptions.DEBUG) {
                        System.out.println("ㄴ " + status.name() + ": Current Status");
                    }
                    if (status.equals(Status.SUCCESS)) {
                        if (conf.launchInstantly()) {
                            user.setCount(user.getCount() + 1);
                            user.setLastCheck(System.currentTimeMillis());
                            pro.sendMessages(user, Msg.info(" >> 출석을 달성했습니다."));
                            pro.launchCommand(conf.getCommands(user.getCount()), pro.getName(user));
                        } else {
                            user.setCount(user.getCount() + 1);
                            pro.sendMessages(user, Msg.info(" >> 출석을 달성했습니다. 보상을 받으려면 '/attend'를 입력하세요."));
                        }
                    } else {
                        pro.sendMessages(user, status.getMessage());
                    }
                }
            }, conf.getCheckTime() + 2);
        };
    }

    @Override
    public Runnable whenLogout(UserInfo user) {
        return () -> {
            user.setLastQuit(System.currentTimeMillis());
            AttendServices.getProcessor().getTaskManager().stopTask(user);
        };
    }

}
