package me.shadowchild.candor.mod;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

public class Mod {

    private File file;
    private String name;
    private State state;
    private String readableName;

    private Mod(File file, String name, State state, String readableName) {

        this.file = file;
        this.name = name;
        this.state = state;
        this.readableName = readableName;
    }

    public File getFile() {

        return file;
    }

    public String getName() {

        return name;
    }

    public State getState() {

        return state;
    }

    public String getReadableName() {

        return readableName;
    }

    public static Mod of(File file) {

        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        State state = State.DISABLED;
        return new Mod(file, name, state, name);
    }

    public static Mod of(JsonObject obj) {

        File file = new File(obj.get("file").getAsString());
        String name = obj.get("name").getAsString();
        State state = State.valueOf(obj.get("state").getAsString());
        String readableName;
        if(obj.has("readableName")) readableName = obj.get("readableName").getAsString();
        else readableName = file.getName().substring(0, file.getName().lastIndexOf("."));
        return new Mod(file, name, state, readableName);
    }

    public static JsonObject from(Mod mod) throws IOException {

        JsonObject obj = new JsonObject();
        obj.addProperty("file", mod.getFile().getCanonicalPath());
        obj.addProperty("name", mod.getName());
        obj.addProperty("state", mod.getState().name());
        obj.addProperty("readableName", mod.getReadableName());
        return obj;
    }

    public enum State {

        ENABLED,
        DISABLED,
    }
}
