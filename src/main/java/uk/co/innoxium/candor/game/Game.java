package uk.co.innoxium.candor.game;

import com.github.f4b6a3.uuid.UuidCreator;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class Game {

    private UUID uuid;
    private String gameExe;
    private String modsFolder;
    private String moduleClass;

    public Game(String gameExe, String modsFolder, String moduleClass) {

        this.uuid = UuidCreator.getNameBasedSha1(gameExe);
        this.gameExe = gameExe;
        this.modsFolder = modsFolder;
        this.moduleClass = moduleClass;
    }

    public String getGameExe() {

        return gameExe;
    }

    public String getModsFolder() {

        return modsFolder;
    }

    public String getModuleClass() {

        return moduleClass;
    }

    public JsonObject toJson() {

        JsonObject ret = new JsonObject();

        ret.addProperty("uuid", String.valueOf(uuid));
        ret.addProperty("gameExe", gameExe);
        ret.addProperty("modsFolder", modsFolder);
        ret.addProperty("moduleClass", moduleClass);

        return ret;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return uuid.equals(game.getUUID());
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid);
    }

    @Override
    public String toString() {

        File gameFile = new File(gameExe);
        return gameFile.getName().substring(0, gameFile.getName().indexOf("."));
    }

    public UUID getUUID() {

        return uuid;
    }

    //    @Override
//    public String toString() {
//        return "Game{" +
//                "gameExe='" + gameExe + '\'' +
//                ", modsFolder='" + modsFolder + '\'' +
//                ", moduleClass='" + moduleClass + '\'' +
//                '}';
//    }
}
