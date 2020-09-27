package uk.co.innoxium.candor.module;

// TODO: Migrate to Record once it is a release feature
public abstract class RunConfig {

    /**
     * Gets the start command for the game, could be a protocol i.e. "steam://" or a file name
     * @return - The command to start the game
     */
    public abstract String getStartCommand();

    /**
     * @return - A string of arguments to pass to the game, works just like any other program arguments.
     */
    public abstract String getProgramArgs();

    /**
     * @return - The directory to start the game in, by default, this will be the directory that contains the executable.
     */
    public abstract String getWorkingDir();
}
