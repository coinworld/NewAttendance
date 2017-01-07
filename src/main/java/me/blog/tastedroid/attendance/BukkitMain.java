package me.blog.tastedroid.attendance;

import me.blog.tastedroid.attendance.bukkit.BukkitCommands;
import me.blog.tastedroid.attendance.bukkit.BukkitEventListener;
import me.blog.tastedroid.attendance.config.Config;
import me.blog.tastedroid.attendance.config.Users;
import me.blog.tastedroid.attendance.config.bukkit.BukkitConfig;
import me.blog.tastedroid.attendance.config.bukkit.BukkitUsers;
import me.blog.tastedroid.attendance.model.Construction;
import me.blog.tastedroid.attendance.model.ServerType;
import me.blog.tastedroid.attendance.process.Processor;
import me.blog.tastedroid.attendance.process.bukkit.BukkitProcessor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitMain extends JavaPlugin implements Main {

    public BukkitMain() throws Exception {
        super();
        class ServerTypeImpl implements ServerType {

            @Override
            public String getName() {
                return "Bukkit";
            }

            @Override
            public Construction<? extends Config> getConfigConstructor() {
                return BukkitConfig::new;
            }

            @Override
            public Construction<? extends Users> getUsersConstructor() {
                return BukkitUsers::new;
            }

            @Override
            public Construction<? extends Processor> getProcessorConstructor() {
                return BukkitProcessor::new;
            }
        }
        AttendServices.set(new ServerTypeImpl(), this);
    }

    @Override
    public void onEnable() {
        try {
            this.getDataFolder().mkdirs();
            AttendServices.getFileManager().setDataDir(this.getDataFolder());
            AttendServices.getFileManager().reload();
        } catch (Exception e) {
            this.getLogger().severe("파일 로드에 실패했습니다. 플러그인을 종료합니다.");
            e.printStackTrace(System.err);
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        if (GlobalOptions.DEBUG) {
            System.out.print(AttendServices.getFileManager().getConfig().getValues().toString());
        }

        AttendServices.getRanking().start();

        CommandExecutor cmd = new BukkitCommands();
        this.getCommand("attend").setExecutor(cmd);
        this.getCommand("attendrank").setExecutor(cmd);

        Listener listener = new BukkitEventListener();
        this.getServer().getPluginManager().registerEvents(listener, this);

        this.getLogger().info(
                String.format("%s v%s 버킷 환경으로 준비 완료했습니다.",
                        this.getDescription().getName(),
                        this.getDescription().getVersion())
        );
    }

    @Override
    public void onDisable() {
        try {
            AttendServices.getFileManager().getUsers().save();
        } catch (Exception e) {
            this.getLogger().severe("파일 저장에 실패했습니다.");
            e.printStackTrace(System.err);
        }
    }
}
