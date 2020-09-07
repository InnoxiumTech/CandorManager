package uk.co.innoxium.candor.game;

import com.google.gson.JsonObject;

import java.util.Objects;

public class Game {

    public String gameExe;
    public String modsFolder;
    public String moduleClass;

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
        return "Game{" +
                "gameExe='" + gameExe + '\'' +
                ", modsFolder='" + modsFolder + '\'' +
                ", moduleClass='" + moduleClass + '\'' +
                '}';
    }
}
