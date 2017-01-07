package me.blog.tastedroid.attendance.model;

import me.blog.tastedroid.attendance.config.Config;
import me.blog.tastedroid.attendance.config.Users;
import me.blog.tastedroid.attendance.process.Processor;

public interface ServerType {

    public String getName();

    public Construction<? extends Config> getConfigConstructor();

    public Construction<? extends Users> getUsersConstructor();

    public Construction<? extends Processor> getProcessorConstructor();
}
