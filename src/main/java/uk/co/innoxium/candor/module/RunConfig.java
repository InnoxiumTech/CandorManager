package uk.co.innoxium.candor.module;

// TODO: Migrate to Record once it is a release feature
public class RunConfig {

    public RunConfig(String runConfigName) {

        this.runConfigName = runConfigName;
    }

    private final String runConfigName;
    private String startCommand = "";
    private String programArgs = "";
    private String workingDir = "";

    /**
     * Gets the start command for the game, could be a protocol i.e. "steam://" or a file name
     * @return - The command to start the game
     */
    @Deprecated
    public String getStartCommand() {

        return startCommand;
    }

    /**
     * @return - A string of arguments to pass to the game, works just like any other program arguments.
     */
    @Deprecated
    public String getProgramArgs() {

        return programArgs;
    }

    /**
     * @return - The directory to start the game in, by default, this will be the directory that contains the executable.
     */
    @Deprecated
    public String getWorkingDir() {

        return workingDir;
    }
}