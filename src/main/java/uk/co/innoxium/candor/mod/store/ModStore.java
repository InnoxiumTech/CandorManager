package uk.co.innoxium.candor.mod.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.ModList;
import uk.co.innoxium.candor.mod.ModUtils;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.json.JsonUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * A class that handles how the mod files are stored, and handled.
 */
public class ModStore {

    // Our ModsList instance
    public static final ModList MODS = new ModList();
    // is the mod store already initialised?
    private static boolean initialised = false;
    // The folder to store the mod files
    private static File modStoreFolder = new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods");
    // The file that holds the mod store data
    private static File modStoreFile = JsonUtil.getJsonFile(new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods.json"), false);

    /**
     * Initialises the mod store.
     */
    public static void initialise() {

        // Updates the store files to create the correct files, if the don't already exist.
        updateStoreDetails();
        // Clear the mods, in case this was called from the load new game option in the mods scene.
        MODS.clear();

        Logger.info(modStoreFolder.getAbsolutePath());

        // Make the folder, if it doesn't already exist.
        if(!modStoreFolder.exists()) {

            modStoreFolder.mkdirs();
        }
        // We are initialised!
        initialised = true;
    }

    /**
     * Determines which mods are installed, regardless of state.
     *
     * @throws IOException - If there is an error loading from the file.
     */
    public static void determineInstalledMods() throws IOException {

        // If not already initialised, do it now, pure sanity check
        if(!initialised) initialise();

        // Get the contents of the file
        JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
        if(contents.has("mods")) {

            // for each mod, add it to the list.
            JsonArray array = contents.getAsJsonArray("mods");
            array.forEach(e -> {

                Mod mod = Mod.fromJson(e.getAsJsonObject());
                MODS.add(mod);
            });
        }
    }

    /**
     * Adds a mod file to the list
     *
     * @param file - The file to add
     * @return - Will return a result, either OK, FAIL, or DUPLICATE
     */
    public static Result addModFile(File file) {

        // If the mod is already installed, it's a duplicate
        if(ModUtils.checkAlreadyInstalled(file)) return Result.DUPLICATE;

        // If it's not already installed, try to copy it to the mod store directory.
        try {

            FileUtils.copyFileToDirectory(file, modStoreFolder);
        } catch(IOException e) {

            Logger.info("Could not copy Mod to the mod store, please retry");
            e.printStackTrace();
            return Result.FAIL;
        }
        // create a new File instance for the file in the new location
        File newFile = new File(modStoreFolder, file.getName());
        // Create the mod from the the new file.
        Mod mod = Mod.fromFile(newFile);
        // Ask the user to input a readable name for the mod.
        mod.setReadableName(JOptionPane.showInputDialog("Please Enter a name for this mod: ", mod.getName()));
        // Try to add the mod to a mods json.
        try {

            JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
            if(!contents.has("mods")) contents.add("mods", new JsonArray());
            contents.get("mods").getAsJsonArray().add(Mod.toJson(mod));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(modStoreFile);
            gson.toJson(contents, writer);

            // ALWAYS close your writers.
            writer.close();
        } catch(IOException e) {

            e.printStackTrace();
            return Result.FAIL;
        }
        MODS.add(mod);
        return Result.OK;
    }

    /**
     * Remove the mod from the store.
     *
     * @param mod                - The mod to remove from the store
     * @param removeFromModsList - should it also be removed from the mods list?
     * @return - true if it was removed, false if not.
     * @throws IOException - If there was an error reading/writing to file.
     */
    public static boolean removeModFile(Mod mod, boolean removeFromModsList) throws IOException {

        // We let the module decide how to delete the files
        if(ModuleSelector.currentModule.getModInstaller().uninstall(mod)) {

            // Once the module has deleted the files, remove the mod from the mods config
            JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
            JsonArray modsArray = contents.getAsJsonArray("mods");
            JsonArray newModArray = modsArray.deepCopy();

            // Attempt to remove the mod from the mods array
            modsArray.forEach(element -> {

                JsonObject obj = (JsonObject) element;
                if(mod.getName().equals(obj.get("name").getAsString())) {

                    newModArray.remove(obj);
                }
            });
            contents.remove("mods");
            contents.add("mods", newModArray);

            // Write the new array to the mod store json file
            Gson gson = JsonUtil.getGson();
            FileWriter writer = JsonUtil.getFileWriter(modStoreFile);
            gson.toJson(contents, writer);

            // Close the writer and delete the mod in the mod store
            writer.close();
            FileUtils.deleteQuietly(mod.getFile());
            // remove from mods list, only if boolean is true
            if(removeFromModsList) MODS.remove(mod);
            return true;
        }
        return false;
    }

    /**
     * Change the state of a mod
     *
     * @param mod   - The mod whose state will change
     * @param state - The state to change it to.
     */
    public static void updateModState(Mod mod, Mod.State state) {

        try {

            // Get the contents of the mod store file
            JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
            // Get the mods array and make a copy of it, in case of an error while it's being manipulated.
            JsonArray array = contents.get("mods").getAsJsonArray();
            JsonArray newArray = array.deepCopy();

            // for each object in the array, check if it's the right mod, and if so, change the state
            for(int i = 0; i < array.size(); i++) {

                JsonObject obj = array.get(i).getAsJsonObject();
                JsonObject newObj = newArray.get(i).getAsJsonObject();
                if(mod.getName().equals(obj.get("name").getAsString())) {

                    newObj.remove("state");
                    newObj.addProperty("state", state.name());

                    newObj.remove("associatedFiles");
                    newObj.add("associatedFiles", mod.getAssociatedFiles());

                    newObj.remove("readableName");
                    newObj.addProperty("readableName", mod.getReadableName());
                }
            }

            // Remove the old array, and set the new array
            contents.remove("mods");
            contents.add("mods", newArray);

            // Ask GSON to write to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(modStoreFile);
            gson.toJson(contents, writer);

            // ALWAYS close your writers.
            writer.close();
        } catch(IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * @return - The mod store File
     */
    public static File getModStoreFolder() {

        return modStoreFolder;
    }

    /* Updates the store for the current game, for example, if we the user used the load new game option in the Mod scene */
    public static void updateStoreDetails() {

        modStoreFolder = new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods");
        modStoreFile = JsonUtil.getJsonFile(new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods.json"), false);
    }

    /**
     * An enum of the result of the install
     */
    public enum Result {

        OK, FAIL, DUPLICATE
    }
}
