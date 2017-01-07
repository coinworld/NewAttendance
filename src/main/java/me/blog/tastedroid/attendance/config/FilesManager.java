package me.blog.tastedroid.attendance.config;

import me.blog.tastedroid.attendance.AttendServices;

import java.io.File;

public class FilesManager implements Reloadable, Savable {

    public FilesManager() throws Exception {
        this.config = AttendServices.getType().getConfigConstructor().construct();
        this.users = AttendServices.getType().getUsersConstructor().construct();
    }

    private File dataDir;

    private final Config config;
    private final Users users;

    public Config getConfig() {
        return config;
    }

    public Users getUsers() {
        return users;
    }

    @Override
    public void reload() throws Exception {
        config.reload();
        users.reload();
    }

    @Override
    public void save() throws Exception {
        config.save();
        users.save();
    }

    public File getDataDir() {
        return dataDir;
    }

    public void setDataDir(File dir) {
        dataDir = dir;
    }
}
