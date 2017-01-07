package me.blog.tastedroid.attendance.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.blog.tastedroid.attendance.process.FileTextTools;

import java.io.File;
import java.lang.reflect.Type;

public abstract class BaseConfig implements Config {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();

    private static final Type VALUES_TYPE = TypeToken.get(ConfigValues.class).getType();

    private ConfigValues values;

    @Override
    public void reload() throws Exception {
        File config = this.getFile();
        if (!config.exists()) {
            values = new ConfigValues();
            save();
        } else {
            values = gson.fromJson(FileTextTools.getFile(config), VALUES_TYPE);
        }
    }

    @Override
    public void save() throws Exception {
        FileTextTools.setText(gson.toJson(values, VALUES_TYPE), this.getFile());
    }

    @Override
    public ConfigValues getValues() {
        return values;
    }
}
