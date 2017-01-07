package me.blog.tastedroid.attendance.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.blog.tastedroid.attendance.model.UserInfo;
import me.blog.tastedroid.attendance.process.FileTextTools;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BaseUsers implements Users {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting().create();

    private Map<UUID, UserInfo> userMap;

    @Override
    public void save() throws Exception {
        FileTextTools.setText(gson.toJson(userMap, getMapType()), this.getFile());
    }

    protected abstract Type getMapType();

    @Override
    public void reload() throws Exception {
        File file = this.getFile();
        if (file.exists()) {
            userMap = gson.fromJson(FileTextTools.getFile(this.getFile()), getMapType());
            userMap.entrySet().forEach(ent -> ent.getValue().setUUID(ent.getKey()));
        } else {
            userMap = new HashMap<>();
        }

    }

    @Override
    public UserInfo getUser(UUID uuid) {
        UserInfo user;
        if (userMap.containsKey(uuid)) {
            user = userMap.get(uuid);
        } else {
            user = this.getEmptyUser(uuid);
            userMap.put(uuid, user);
        }
        return user;
    }

    protected abstract UserInfo getEmptyUser(UUID uuid);

    @Override
    public void resetUser(UUID uuid) {
        if (userMap.containsKey(uuid)) {
            userMap.get(uuid).reset();
            userMap.remove(uuid);
        }
    }

    @Override
    public Map<UUID, UserInfo> getUserMap() {
        return Collections.unmodifiableMap(userMap);
    }
}
