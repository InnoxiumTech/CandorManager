package uk.co.innoxium.candor.game;

import com.github.f4b6a3.uuid.UuidCreator;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.module.AbstractModule;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.module.RunConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


/**
 * An object to represent a "game".
 */
public class Game {

    // A list of Run Configs
    public ArrayList<RunConfig> runConfigs = new ArrayList<>();
    // The UUID of the game
    private UUID uuid;
    // The path to the executable of the game
    private String gameExe;
    // The path to the game's mods folder
    private String modsFolder;
    // The module that game should use, not currently used.
    private String moduleClass;

    /**
     * Creates a game instance
     *
     * @param gameExe     - The executable of the game
     * @param modsFolder  - The mods folder for the game
     * @param moduleClass - The module class
     */
    public Game(String gameExe, String modsFolder, String moduleClass) {

        // set the objects fields
        this.uuid = UuidCreator.getNameBasedSha1(gameExe);
        this.gameExe = gameExe;
        this.modsFolder = modsFolder;
        this.moduleClass = moduleClass;
    }

    /**
     * @return - A String of the game executable
     */
    public String getGameExe() {

        return gameExe;
    }

    /**
     * @return - A strong of the mods folder.
     */
    public String getModsFolder() {

        return modsFolder;
    }

    /**
     * @return - A string of the module class
     */
    public String getModuleClass() {

        return moduleClass;
    }

    /**
     * Converts the object to a json object
     *
     * @return - A JSON Object representing the game.
     */
    public JsonObject toJson() {

        JsonObject ret = new JsonObject();

        ret.addProperty("uuid", String.valueOf(uuid));
        ret.addProperty("gameExe", gameExe);
        ret.addProperty("modsFolder", modsFolder);
        ret.addProperty("moduleClass", moduleClass);

        return ret;
    }

    // returns a hashcode to match the equals, here in case the games are even used in a hashmap
    @Override
    public int hashCode() {

        return Objects.hash(uuid);
    }

    // Will only return true UUID's match.
    @Override
    public boolean equals(Object o) {

        if(this == o) return true;
        if(!(o instanceof Game)) return false;
        Game game = (Game) o;
        return uuid.equals(game.getUUID());
    }

    /**
     * @return - A string representation of the game.
     */
    @Override
    public String toString() {

        try {

            AbstractModule module = ModuleSelector.getModuleForGame(this);
            if(module.getReadableGameName() == null) {

                File gameFile = new File(gameExe);
                return gameFile.getName().substring(0, gameFile.getName().indexOf("."));
            }
            return module.getReadableGameName();
        } catch(NullPointerException e) {

            File gameFile = new File(gameExe);
            return gameFile.getName().substring(0, gameFile.getName().indexOf("."));
        }
    }

    /**
     * @return - The UUID of this game.
     */
    public UUID getUUID() {

        return uuid;
    }

    // A toString instance used for debugging, will stay for the time being.
    //    @Override
//    public String toString() {
//        return "Game{" +
//                "gameExe='" + gameExe + '\'' +
//                ", modsFolder='" + modsFolder + '\'' +
//                ", moduleClass='" + moduleClass + '\'' +
//                '}';
//    }
}
