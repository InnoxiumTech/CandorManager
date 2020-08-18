package me.shadowchild.candor.thread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.shadowchild.candor.mod.Mod;
import me.shadowchild.candor.mod.ModsHandler;
import me.shadowchild.candor.module.AbstractModInstaller;
import me.shadowchild.candor.module.ModuleSelector;
import me.shadowchild.cybernize.util.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadModInstaller extends Thread {

    private final Mod mod;

    public ThreadModInstaller(Mod mod) {

        this.mod = mod;
    }

    @Override
    public void run() {

        boolean installed = false;

        AbstractModInstaller module = ModuleSelector.currentModule.getModInstaller();
        if(module.canInstall(mod)) {

            installed = module.install(mod);
        }
        if(installed) {

            mod.setState(Mod.State.ENABLED);
            updateStateInModStore(mod, Mod.State.ENABLED);
            ModsHandler.MODS.fireChangeToListeners("install", mod, true);
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
