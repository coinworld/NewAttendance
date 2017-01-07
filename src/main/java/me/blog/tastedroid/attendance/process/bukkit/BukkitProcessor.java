package me.blog.tastedroid.attendance.process.bukkit;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.Msg;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.AttendTasks;
import me.blog.tastedroid.attendance.process.Processor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;

public class BukkitProcessor implements Processor {

    @Override
    public void launchCommand(String[] commands, String userName) {
        for (String command : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%p%", userName));
        }
    }

    @Override
    public void sendMessages(UserInfo user, Msg... msg) {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getUniqueId().equals(user.getUUID()))
                .findFirst()
                .ifPresent(p -> {
                    for (Msg m : msg) {
                        p.sendMessage(getFormattedString(m));
                    }
                });
    }

    public static String getFormattedString(Msg msg) {
        String color = "";
        switch (msg.getColor()) {
            case WARN:
                color = ChatColor.GOLD + "";
                break;
            case INFO:
                color = ChatColor.YELLOW + "";
                break;
        }
        return color + msg.getMessage();
    }

    @Override
    public boolean isOnline(UserInfo user) {
        return Bukkit.getPlayer(user.getUUID()) != null;
    }

    @Override
    public InputStream getResource(String fileName) {
        return ((JavaPlugin) AttendServices.getMain()).getResource(fileName);
    }

    private final BukkitAttendTasks taskManager = new BukkitAttendTasks();

    @Override
    public AttendTasks getTaskManager() {
        return taskManager;
    }

    @Override
    public String getName(UserInfo user) {
        OfflinePlayer off = Bukkit.getOfflinePlayer(user.getUUID());
        String name = null;
        if (off != null) {
            name = off.getName();
        }

        return name;
    }

}
