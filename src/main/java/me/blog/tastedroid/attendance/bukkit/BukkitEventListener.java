package me.blog.tastedroid.attendance.bukkit;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.GlobalOptions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class BukkitEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (GlobalOptions.DEBUG) {
            System.out.println(e.getPlayer().getName() + ": Join Event Fired.");
        }
        AttendServices.getProcessor().getBehaviors().whenLogin(
                AttendServices.getFileManager().getUsers().getUser(
                        e.getPlayer().getUniqueId()
                )
        ).run();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (GlobalOptions.DEBUG) {
            System.out.println(e.getPlayer().getName() + ": Quit Event Fired.");
        }
        AttendServices.getProcessor().getBehaviors().whenLogout(
                AttendServices.getFileManager().getUsers().getUser(
                        e.getPlayer().getUniqueId()
                )
        ).run();
    }
}
