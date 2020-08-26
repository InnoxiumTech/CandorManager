package uk.co.innoxium.candor.mod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.util.Utils;
import me.shadowchild.cybernize.archive.Archive;
import me.shadowchild.cybernize.archive.ArchiveBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Mod {

    private File file;
    private String name;
    private State state;
    private String readableName;
    private JsonArray associatedFiles;

    private Mod(File file, String name, State state, String readableName, JsonArray associatedFiles) {

        this.file = file;
        this.name = name;
        this.state = state;
        this.readableName = readableName;
        this.associatedFiles = associatedFiles;
    }

    public File getFile() {

        return file;
    }

    public String getName() {

        return name;
    }

    public void setState(State state) {

        this.state = state;
    }

    public State getState() {

        return state;
    }

    public void setReadableName(String readableName) {

        this.readableName = readableName;
    }

    public String getReadableName() {

        return readableName;
    }

    public JsonArray getAssociatedFiles() {

        return associatedFiles;
    }

    public static Mod of(File file) {

        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        State state = State.DISABLED;

        JsonArray array = new JsonArray();

        if(Utils.isArchive(file)) {

            Archive archive = new ArchiveBuilder(file).build();
            try {

                for(String filePath : archive.getAllArchiveItems()) {

                    System.out.println(filePath);
                    array.add(filePath);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {

            array.add(file.getName());
        }

        return new Mod(file, name, state, name, array);
    }

    public static Mod of(JsonObject obj) {

        File file = new File(obj.get("file").getAsString());
        String name = obj.get("name").getAsString();
        State state = State.valueOf(obj.get("state").getAsString());
        String readableName;
        if(obj.has("readableName")) readableName = obj.get("readableName").getAsString();
        else readableName = file.getName().substring(0, file.getName().lastIndexOf("."));
        JsonArray array = obj.getAsJsonArray("associatedFiles");
        return new Mod(file, name, state, readableName, array);
    }

    public static JsonObject from(Mod mod) throws IOException {

        JsonObject obj = new JsonObject();
        obj.addProperty("file", mod.getFile().getCanonicalPath());
        obj.addProperty("name", mod.getName());
        obj.addProperty("state", mod.getState().name());
        obj.addProperty("readableName", mod.getReadableName());
        obj.add("associatedFiles", mod.getAssociatedFiles());
        return obj;
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {

        assert obj instanceof Mod;
        return this.name.equals(((Mod) obj).name);
    }

    public enum State {

        ENABLED,
        DISABLED,
    }
}
