package me.blog.tastedroid.attendance;

import com.google.inject.Inject;
import me.blog.tastedroid.attendance.config.Config;
import me.blog.tastedroid.attendance.config.Users;
import me.blog.tastedroid.attendance.config.sponge.SpongeConfig;
import me.blog.tastedroid.attendance.config.sponge.SpongeUsers;
import me.blog.tastedroid.attendance.model.Construction;
import me.blog.tastedroid.attendance.model.ServerType;
import me.blog.tastedroid.attendance.process.Processor;
import me.blog.tastedroid.attendance.process.sponge.SpongeProcessor;
import me.blog.tastedroid.attendance.sponge.SpongeCommands;
import me.blog.tastedroid.attendance.sponge.SpongeEventListener;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.File;

@Plugin(id = "attendance",
        version = "0.1e",
        name = "Attendance",
        description = "간단한 출석 플러그인입니다.")
public final class SpongeMain implements Main {

    @Inject
    @ConfigDir(sharedRoot = false)
    private File dir;

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    public SpongeMain() throws Exception {
        class ServerTypeImpl implements ServerType {

            @Override
            public String getName() {
                return "Sponge";
            }

            @Override
            public Construction<? extends Config> getConfigConstructor() {
                return SpongeConfig::new;
            }

            @Override
            public Construction<? extends Users> getUsersConstructor() {
                return SpongeUsers::new;
            }

            @Override
            public Construction<? extends Processor> getProcessorConstructor() {
                return SpongeProcessor::new;
            }
        }
        AttendServices.set(new ServerTypeImpl(), this);
    }

    private boolean failed = false;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        try {
            dir.mkdirs();
            AttendServices.getFileManager().setDataDir(dir);
            AttendServices.getFileManager().reload();
        } catch (Exception e) {
            logger.error("파일 로드에 실패했습니다. 플러그인을 사용할 수 없습니다.");
            e.printStackTrace(System.err);
            failed = true;
            return;
        }

        AttendServices.getRanking().start();
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        if (failed) {
            return;
        }

        Sponge.getEventManager().registerListeners(this, new SpongeEventListener());

        CommandSpec attendSpec = CommandSpec.builder()
                .description(Text.of("출석을 확인합니다."))
                .permission("attendance.dc")
                .arguments(GenericArguments.userOrSource(Text.of("target")))
                .executor(SpongeCommands.newAttendCommand())
                .build();

        CommandSpec rankingSpec = CommandSpec.builder()
                .description(Text.of("출석 랭킹을 확인합니다."))
                .permission("attendance.check")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page"))),
                        GenericArguments.flags()
                                .permissionFlag("attendance.check", "r") // check'd be edited.
                                .buildWith(GenericArguments.none()))
                .executor(SpongeCommands.newRankingCommand())
                .build();

        Sponge.getCommandManager().register(this, attendSpec, "attend", "dc", "dailycheck");
        Sponge.getCommandManager().register(this, rankingSpec, "attendrank", "dcrank");

        logger.info(
                String.format("%s v%s 스펀지 환경으로 준비 완료했습니다.",
                        container.getName(),
                        container.getVersion().get())
        );
    }

    @Listener
    public void onExit(GameStoppedServerEvent ev) {
        try {
            AttendServices.getFileManager().getUsers().save();
        } catch (Exception e) {
            logger.error("파일 저장에 실패했습니다.");
            e.printStackTrace(System.err);
        }
    }
}
