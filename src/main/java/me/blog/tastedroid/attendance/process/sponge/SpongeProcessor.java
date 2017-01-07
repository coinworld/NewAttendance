package me.blog.tastedroid.attendance.process.sponge;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.model.Msg;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.AttendTasks;
import me.blog.tastedroid.attendance.process.Processor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.io.InputStream;

public class SpongeProcessor implements Processor {

    @Override
    public void launchCommand(String[] commands, String userName) {
        for (String command : commands) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
        }
    }

    @Override
    public void sendMessages(UserInfo user, Msg... msg) {
        Sponge.getServer().getPlayer(user.getUUID())
                .ifPresent(player -> {
                    for (Msg m : msg) {
                        player.sendMessage(getFormattedText(m));
                    }
                });
    }

    public static Text getFormattedText(Msg msg) {
        TextColor color = TextColors.RESET;
        switch (msg.getColor()) {
            case WARN:
                color = TextColors.GOLD;
                break;
            case INFO:
                color = TextColors.YELLOW;
                break;
        }
        return Text.builder(msg.getMessage()).color(color).build();
    }

    @Override
    public boolean isOnline(UserInfo user) {
        return Sponge.getServer().getPlayer(user.getUUID()).isPresent();
    }

    @Override
    public InputStream getResource(String fileName) throws IOException {
        return Sponge.getAssetManager()
                .getAsset(AttendServices.getMain(), fileName)
                .orElseThrow(IllegalArgumentException::new)
                .getUrl().openStream();
    }

    private final AttendTasks spongeTasks = new SpongeAttendTasks();

    @Override
    public AttendTasks getTaskManager() {
        return spongeTasks;
    }

    @Override
    public String getName(UserInfo user) {
        try {
            return Sponge.getServer().getGameProfileManager().get(user.getUUID()).get().getName().get();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return "unknown";
        }
    }

}
