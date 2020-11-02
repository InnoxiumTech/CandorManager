package uk.co.innoxium.candor.tool;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.NativeDialogs;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.candor.window.modscene.ModScene;
import uk.co.innoxium.cybernize.json.JsonUtil;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ToolsList {

    private static final ArrayList<Tool> TOOL_LIST = new ArrayList<>();
    private static final File toolsFile = new File(Resources.CONFIG_PATH, ModuleSelector.currentModule.getModuleName() + "/tools.json");

    public static ArrayList<Tool> getToolList() {

        return TOOL_LIST;
    }

    public static boolean addTool(Tool tool) {

        if(!TOOL_LIST.contains(tool)) {

            if(TOOL_LIST.add(tool)) {

                if(Resources.currentScene instanceof ModScene) {

                    JMenuItem menuItem = new JMenuItem(tool.name);
                    menuItem.addActionListener(new ToolActionListener(tool));
                    ((ModScene) Resources.currentScene).getToolsMenu().add(menuItem);
                }
            }
            return true;
        } else {

            return false;
        }
    }

    public static boolean addTools(Collection<? extends Tool> tools) {

        if(TOOL_LIST.addAll(tools)) {

            if(Resources.currentScene instanceof ModScene) {

                tools.forEach(ToolsList::addTool);
            }
        }
        return false;
    }

    public static void determineDefinedTools() {

        if(TOOL_LIST.size() > 0) {

            TOOL_LIST.clear();
        }

        File tools = JsonUtil.getJsonFile(toolsFile, false);
        if(!tools.exists()) {

            Logger.info("Tools list for module doesn't exist, skipping");
            return;
        }

        JsonObject contents = null;
        try {

            contents = JsonUtil.getObjectFromPath(tools.toPath());
        } catch (IOException e) {

            e.printStackTrace();
            NativeDialogs.showCandorGenericFailure(true);
        }
        if(!contents.has("tools")) {

            contents.add("tools", new JsonArray());
        }
        JsonArray toolList = contents.get("tools").getAsJsonArray();
        toolList.forEach(element -> {

            assert element instanceof JsonObject;
            JsonObject obj = (JsonObject) element;
            Tool tool = JsonUtil.getGson().fromJson(obj, Tool.class);
            addTool(tool);
        });

        ModuleSelector.currentModule.getTools().forEach(ToolsList::addTool);
    }

    public static void writeToJson() throws IOException {

        // Creates the file if it doesn't already exist
        File tools = JsonUtil.getJsonFile(toolsFile, false);
        JsonObject contents = JsonUtil.getObjectFromPath(toolsFile.toPath());

        if(!contents.has("tools")) {

            contents.add("tools", new JsonArray());
        }
        // Get the current "games" array of the file
        JsonArray toolsArray = JsonUtil.getArray(contents, "tools");

        getToolList().forEach(tool -> {

            JsonObject obj = (JsonObject) JsonUtil.getGson().toJsonTree(tool);
            if(!toolsArray.contains(obj)) toolsArray.add(obj);
        });

        // Create a file writer and ask Gson to write to file.
        FileWriter writer = JsonUtil.getFileWriter(toolsFile);
        JsonUtil.getGson().toJson(contents, writer);
        // ALWAYS close your writers.
        writer.close();
    }
}
