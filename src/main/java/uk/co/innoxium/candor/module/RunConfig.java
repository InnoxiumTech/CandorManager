package uk.co.innoxium.candor.module;

// TODO: Migrate to Record once it is a release feature
public class RunConfig {

    /**
     * Gets the start command for the game, could be a protocol i.e. "steam://" or a file name
     * @return - The command to start the game
     */
    public String getStartCommand() {

        return "";
    }

    /**
     * @return - A string of arguments to pass to the game, works just like any other program arguments.
     */
    public String getProgramArgs() {

        return "";
    }

    /**
     * @return - The directory to start the game in, by default, this will be the directory that contains the executable.
     */
    public String getWorkingDir() {

        return "";
    }
}