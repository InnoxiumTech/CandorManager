package uk.co.innoxium.candor.game;

import com.github.f4b6a3.uuid.util.UuidConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.Settings;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.json.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;


/**
 * This class handles the games list logic, such as adding, removing.
 */
public class GamesList {

    // Our games list.
    private static final ArrayList<Game> GAMES_LIST = new ArrayList<>();
    // The file to save the list to.
    private static final File GAMES_FILE = JsonUtil.getJsonFile(new File(Resources.CONFIG_PATH, "Games.json"), false);

    /**
     * Adds a game to the games list, only if the game is not already in it.
     *
     * @param game - The game to add.
     */
    public static void addGame(Game game) {

        if(!GAMES_LIST.contains(game)) {

            GAMES_LIST.add(game);
        }
    }

    /**
     * Gets a game instance from a UUID
     *
     * @param uuid - The UUID of the game to get.
     * @return - A game instance if the games list contains it, else null.
     */
    public static Game getGameFromUUID(UUID uuid) {

        AtomicReference<Game> ret = new AtomicReference<>();
        GAMES_LIST.forEach(game -> {

            if(game.getUUID().equals(uuid)) {

                ret.set(game);
            }
        });
        return ret.get();
    }

    /**
     * Gets a game instance from a UUID
     *
     * @param uuid - The UUID of the game to get.
     * @return - A game instance if the games list contains it, else null.
     */
    public static Game getGameFromUUID(String uuid) {

        return getGameFromUUID(UuidConverter.fromString(uuid));
    }

    /**
     *
     * Gets the current game in use.
     *
     * @return - A Game instance, null if not yet selected
     */
    public static Game getCurrentGame() {

        return getGameFromUUID(Settings.lastGameUuid);
    }

    /*
        Returns the games list object
     */
    public static ArrayList<Game> getGamesList() {

        return GAMES_LIST;
    }

    /**
     * Loads the games list from file, converts the JSON objects to Game objects and adds them to the games list.
     *
     * @throws IOException - If there is an error loading the file.
     */
    public static void loadFromFile() throws IOException {

        // Get the contents fo the JSON file
        JsonObject contents = JsonUtil.getObjectFromPath(GAMES_FILE.toPath());
        // If the contents doesn't have a "games" array, add one, else convert to Game object, and add to list.
        if(!contents.has("games")) {

            // Add new JSONArray to the contents
            contents.add("games", new JsonArray());
            // Create a FileWriter and ask Gson to write to file.
            FileWriter writer = JsonUtil.getFileWriter(GAMES_FILE);
            JsonUtil.getGson().toJson(contents, writer);
            // ALWAYS close your writers.
            writer.close();
        } else {

            // Get the games array
            JsonArray gamesArray = JsonUtil.getArray(contents, "games");
            // For each object in the array, convert to Game object, and add to games list.
            gamesArray.forEach(e -> {

                assert e instanceof JsonObject;
                JsonObject obj = (JsonObject) e;

                Game g = JsonUtil.getGson().fromJson(obj, Game.class);
                GAMES_LIST.add(g);
            });
        }
    }

    /**
     * Saves the list to file.
     *
     * @throws IOException - If there is an error writing to file.
     */
    public static void writeToFile() throws IOException {

        Logger.info("Writing Games List to file");
        // Get the contents of the current file.
        JsonObject contents = JsonUtil.getObjectFromPath(GAMES_FILE.toPath());
        // Get the current "games" array of the file
        JsonArray gamesArray = JsonUtil.getArray(contents, "games");
        // For each game in the Games List, add to the array if not already there.
        GAMES_LIST.forEach(game -> {

            JsonObject obj = game.toJson();
            if(!gamesArray.contains(obj)) gamesArray.add(obj);
        });
        // Create a file writer and ask Gson to write to file.
        FileWriter writer = JsonUtil.getFileWriter(GAMES_FILE);
        JsonUtil.getGson().toJson(contents, writer);
        // ALWAYS close your writers.
        writer.close();
    }
}
