package uk.co.innoxium.candor.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.json.JsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class GamesList {

    private static final ArrayList<Game> GAMES_LIST = new ArrayList<>();
    private static final File GAMES_FILE = JsonUtil.getJsonFile(new File(Resources.CONFIG_PATH, "Games.json"), false);

    public static void addGame(Game game) {

        if(!GAMES_LIST.contains(game)) {

            GAMES_LIST.add(game);
        }
    }

    public static Game getGameFromUUID(UUID uuid) {

        AtomicReference<Game> ret = new AtomicReference<>();
        GAMES_LIST.forEach(game -> {

            if(game.getUUID().equals(uuid)) {

                ret.set(game);
            }
        });
        return ret.get();
    }

    public static ArrayList<Game> getGamesList() {

        return GAMES_LIST;
    }

    public static void loadFromFile() throws IOException {

        JsonObject contents = JsonUtil.getObjectFromPath(GAMES_FILE.toPath());
        if(!contents.has("games")) {

            contents.add("games", new JsonArray());
            FileWriter writer = JsonUtil.getFileWriter(GAMES_FILE);
            JsonUtil.getGson().toJson(contents, writer);
            writer.close();
        } else {

            JsonArray gamesArray = JsonUtil.getArray(contents, "games");
            gamesArray.forEach(e -> {

                assert e instanceof JsonObject;
                JsonObject obj = (JsonObject)e;

                Game g = JsonUtil.getGson().fromJson(obj, Game.class);
                GAMES_LIST.add(g);
            });
        }
    }

    public static void writeToFile() throws IOException {

        JsonObject contents = JsonUtil.getObjectFromPath(GAMES_FILE.toPath());
        JsonArray gamesArray = JsonUtil.getArray(contents, "games");
        GAMES_LIST.forEach(game -> {

            JsonObject obj = game.toJson();
            if(!gamesArray.contains(obj)) gamesArray.add(obj);
        });
        FileWriter writer = JsonUtil.getFileWriter(GAMES_FILE);
        JsonUtil.getGson().toJson(contents, writer);
        writer.close();
    }
}
