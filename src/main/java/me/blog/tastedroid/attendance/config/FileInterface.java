package me.blog.tastedroid.attendance.config;

import java.io.File;

public interface FileInterface extends Reloadable, Savable {

    public File getFile();
}
