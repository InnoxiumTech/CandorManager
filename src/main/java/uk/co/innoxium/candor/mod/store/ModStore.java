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
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.json.JsonUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModStore {

    private static boolean initialised = false;

    public static final ModList MODS = new ModList<Mod>();

    private static File modStoreFolder = new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods");
    private static File modStoreFile = JsonUtil.getJsonFile(new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods.json"), false);
//    private static File modStoreFile = new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods.json");

    public static void initialise() {

        updateStoreDetails();
        MODS.clear();

        System.out.println(modStoreFolder.getAbsolutePath());

        if(!modStoreFolder.exists()) {

            modStoreFolder.mkdirs();
        }
        initialised = true;
    }

    public static void determineInstalledMods() throws IOException {

        // If not already initialised, do it now, pure sanity check
        if(!initialised) initialise();

        JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
        if(contents.has("mods")) {

            JsonArray array = contents.getAsJsonArray("mods");
            array.forEach(e -> {

                Mod mod = Mod.fromJson(e.getAsJsonObject());
                MODS.add(mod);
            });
        }
    }

    public static Result addModFile(File file) {

        if(ModUtils.checkAlreadyInstalled(file)) return Result.DUPLICATE;

        try {

            FileUtils.copyFileToDirectory(file, modStoreFolder);
        } catch (IOException exception) {

            System.out.println("Could not copy Mod to the mod store, please retry");
            exception.printStackTrace();
            return Result.FAIL;
        }
        File newFile = new File(modStoreFolder, file.getName());
        Mod mod = Mod.fromFile(newFile);
        mod.setReadableName(JOptionPane.showInputDialog("Please Enter a name for this mod: ", mod.getName()));
        try {

            JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
            if(!contents.has("mods")) contents.add("mods", new JsonArray());
            contents.get("mods").getAsJsonArray().add(Mod.toJson(mod));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(modStoreFile);
            gson.toJson(contents, writer);

            writer.close();
        } catch (IOException exception) {

            exception.printStackTrace();
            return Result.FAIL;
        }
        MODS.add(mod);
        return Result.OK;
    }

    public static boolean removeModFile(Mod mod) throws IOException {

        // We let the module decide how to delete the files
        if(ModuleSelector.currentModule.getModInstaller().uninstall(mod)) {

            // Once the module has deleted the files, remove the mod from the mods config
            JsonObject contents = JsonUtil.getObjectFromPath(modStoreFile.toPath());
            JsonArray modsArray = contents.getAsJsonArray("mods");
            JsonArray newModArray = modsArray.deepCopy();

            // Attempt to remove the mod from the mods array
            modsArray.forEach(element -> {

                JsonObject obj = (JsonObject)element;
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
            MODS.remove(mod);
            return true;
        }
        return false;
    }

    public static File getModStoreFolder() {

        return modStoreFolder;
    }

    public static void updateStoreDetails() {

        modStoreFolder = new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods");
        modStoreFile = JsonUtil.getJsonFile(new File(Resources.STORE_PATH, ModuleSelector.currentModule.getExeName() + "/mods.json"), false);
    }

    public enum Result {

        OK, FAIL, DUPLICATE
    }
}
