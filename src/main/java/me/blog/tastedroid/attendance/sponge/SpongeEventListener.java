package me.blog.tastedroid.attendance.sponge;

import me.blog.tastedroid.attendance.AttendServices;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public final class SpongeEventListener {

    @Listener
    public void onJoin(ClientConnectionEvent.Join e, @First Player p) {
        AttendServices.getProcessor().getBehaviors().whenLogin(
                AttendServices.getFileManager().getUsers().getUser(
                        p.getUniqueId()
                )
        ).run();
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect e, @First Player p) {
        AttendServices.getProcessor().getBehaviors().whenLogout(
                AttendServices.getFileManager().getUsers().getUser(
                        p.getUniqueId()
                )
        ).run();
    }
}
