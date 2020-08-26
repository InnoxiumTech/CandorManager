package uk.co.innoxium.candor.thread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.mod.Mod;
import uk.co.innoxium.candor.mod.ModsHandler;
import uk.co.innoxium.candor.module.AbstractModInstaller;
import uk.co.innoxium.candor.module.ModuleSelector;
import me.shadowchild.cybernize.util.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadModInstaller extends Thread {

    private final Mod mod;
    private final boolean installing;

    public ThreadModInstaller(Mod mod) {

        this(mod, true);
    }

    public ThreadModInstaller(Mod mod, boolean installing) {

        this.mod = mod;
        this.installing = installing;
    }

    @Override
    public void run() {

        AbstractModInstaller modInstaller = ModuleSelector.currentModule.getModInstaller();

        if(installing) {

            boolean installed = false;

            if(modInstaller.canInstall(mod)) {

                installed = modInstaller.install(mod);
            }
            if(installed) {

                mod.setState(Mod.State.ENABLED);
                updateStateInModStore(mod, Mod.State.ENABLED);
                ModsHandler.MODS.fireChangeToListeners("install", mod, true);
            }
        } else {

            boolean successful = modInstaller.uninstall(mod);
            ModsHandler.MODS.fireChangeToListeners("uninstall", mod, successful);
        }
    }

    private void updateStateInModStore(Mod mod, Mod.State state) {

        File installedModsConfig = new File("config/" + ModuleSelector.currentModule.getExeName() + "/mods.json");

        try {

            JsonObject contents = JsonUtil.getObjectFromPath(installedModsConfig.toPath());
            JsonArray array = contents.get("mods").getAsJsonArray();
            JsonArray newArray = array.deepCopy();

            for (int i = 0; i < array.size(); i++) {

                JsonObject obj = array.get(i).getAsJsonObject();
                if(mod.getName().equals(obj.get("name").getAsString())) {

                    newArray.get(i).getAsJsonObject().remove("state");
                    newArray.get(i).getAsJsonObject().addProperty("state", state.name());
                }
            }

            contents.remove("mods");
            contents.add("mods", newArray);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(installedModsConfig);
            gson.toJson(contents, writer);

            writer.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
