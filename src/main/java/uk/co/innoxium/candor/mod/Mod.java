package uk.co.innoxium.candor.mod;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.Utils;
import uk.co.innoxium.cybernize.archive.Archive;
import uk.co.innoxium.cybernize.archive.ArchiveBuilder;
import uk.co.innoxium.cybernize.archive.ArchiveItem;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * An object representing a mod file.
 */
public class Mod {

    // The file of the mod
    private File file;
    // The name of the mod - used in code
    private String name;
    // The state of the mod, either enabled or disabled
    private State state;
    // The name of the mod, in a readable and identifiable way for the user.
    private String readableName;
    // An array of files to be associated to the mod. Can then be used to check for conflicts, or simply to remove.
    private JsonArray associatedFiles;

    /*
        The default constructor, see above for information on the params.
     */
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

    /**
     * Creates a Mod object from a file.
     * @param file - The File to create a mod of.
     * @return - A mod instance representing the mod file.
     */
    public static Mod fromFile(File file) {

        // Get the name of the file, excluding the extension.
        String name = file.getName().substring(0, file.getName().lastIndexOf("."));
        // Set state to disabled initially.
        State state = State.DISABLED;

        // Create a new JSon array for associated files.
        JsonArray array = new JsonArray();

        // Check if the mod file is an archive.
        if(Utils.isArchive(file)) {

            // Create an Archive instance for the file.
            Archive archive = new ArchiveBuilder(file).build();
            try {

                // For each item in the Archive, check if the item is critical to the game's vanilla functionality, if not, associate it
                for(ArchiveItem archiveItem : archive.getAllArchiveItems()) {

                    // TODO: Backup and restore files that conflict with vanilla files.
                    if(!ModuleSelector.currentModule.isCritical(archiveItem)) {

                        Logger.info(archiveItem);
                        // Add the item if the array if it isn't critical to the game's vanilla functionality
                        array.add(archiveItem.getFilePath());
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        } else {

            // If the mod is not an archive, associate just the file.
            array.add(file.getName());
        }

        // Return a Mod instance from the file.
        return new Mod(file, name, state, name, array);
    }

    /**
     * Creates a Mod object from a JSON Object
     * @param obj - The JSON object to convert
     * @return - A mod object from the JSON object provided.
     */
    public static Mod fromJson(JsonObject obj) {

        File file = new File(obj.get("file").getAsString());
        String name = obj.get("name").getAsString();
        State state = State.valueOf(obj.get("state").getAsString());
        String readableName;
        if(obj.has("readableName")) readableName = obj.get("readableName").getAsString();
        else readableName = file.getName().substring(0, file.getName().lastIndexOf("."));
        JsonArray array = obj.getAsJsonArray("associatedFiles");
        return new Mod(file, name, state, readableName, array);
    }

    /**
     * Converts a Mod object, to a JSON object
     * @param mod - The mod object to convert
     * @return - A JSON Object representing the Mod object
     * @throws IOException - if the is an error with the file name.
     */
    public static JsonObject toJson(Mod mod) throws IOException {

        JsonObject obj = new JsonObject();
        obj.addProperty("file", mod.getFile().getCanonicalPath()); // TODO: Change to absolute path
        obj.addProperty("name", mod.getName());
        obj.addProperty("state", mod.getState().name());
        obj.addProperty("readableName", mod.getReadableName());
        obj.add("associatedFiles", mod.getAssociatedFiles());
        return obj;
    }

    /* Create a hash code based on the equals criteria, here in case Mod's are used in a hashmap */
    @Override
    public int hashCode() {

        return Objects.hashCode(name);
    }

    /* Return true only if the name is the same */
    @Override
    public boolean equals(Object obj) {

        assert obj instanceof Mod;
        return this.name.equals(((Mod) obj).name);
    }

    /**
     * An enum representing the State of the mod
     */
    public enum State {

        ENABLED,
        DISABLED,
    }
}
