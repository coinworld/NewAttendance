package me.blog.tastedroid.attendance;

import me.blog.tastedroid.attendance.config.FilesManager;
import me.blog.tastedroid.attendance.model.ServerType;
import me.blog.tastedroid.attendance.process.Processor;
import me.blog.tastedroid.attendance.ranking.Ranking;

public final class AttendServices {

    private static Processor processor;
    private static FilesManager fileManager;
    private static Ranking ranking;

    private static ServerType type;

    private static Main main;

    public static void set(ServerType serverType, Main plugin) throws Exception {
        type = serverType;
        main = plugin;
        processor = serverType.getProcessorConstructor().construct();
        fileManager = new FilesManager();
        ranking = new Ranking();
    }

    public static ServerType getType() {
        return type;
    }

    public static Processor getProcessor() {
        return processor;
    }

    public static FilesManager getFileManager() {
        return fileManager;
    }

    public static Main getMain() {
        return main;
    }

    public static Ranking getRanking() {
        return ranking;
    }
}
