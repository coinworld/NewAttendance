package me.blog.tastedroid.attendance.config.sponge;

import me.blog.tastedroid.attendance.AttendServices;
import me.blog.tastedroid.attendance.config.BaseConfig;

import java.io.File;

public final class SpongeConfig extends BaseConfig {

    @Override
    public File getFile() {
        return new File(AttendServices.getFileManager().getDataDir(), "config.json");
    }

}
