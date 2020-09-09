package uk.co.innoxium.candor.game;

import com.google.gson.JsonObject;
import uk.co.innoxium.candor.module.ModuleSelector;

import java.io.File;
import java.util.Objects;

public class Game {

    private String gameExe;
    private String modsFolder;
    private String moduleClass;

    public Game(String gameExe, String modsFolder, String moduleClass) {

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
        return gameExe.equals(game.gameExe);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameExe);
    }

    @Override
    public String toString() {

        String gameName = ModuleSelector.getModuleForGame(new File(gameExe)).getReadableGameName();
        return gameName;
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
