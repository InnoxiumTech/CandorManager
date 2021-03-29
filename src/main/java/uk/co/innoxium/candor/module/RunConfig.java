package uk.co.innoxium.candor.module;


// TODO: Migrate to Record once it is a release feature
public class RunConfig {

    private final String runConfigName;
    public String startCommand = "";
    public String programArgs = "";
    public String workingDir = "";
    public RunConfig(String runConfigName) {

        this.runConfigName = runConfigName;
    }

    /**
     * Gets the start command for the game, could be a protocol i.e. "steam://" or a file name
     *
     * @return - The command to start the game
     */
    public String getStartCommand() {

        return startCommand;
    }

    /**
     * @return - A string of arguments to pass to the game, works just like any other program arguments.
     */
    public String getProgramArgs() {

        return programArgs;
    }

    /**
     * @return - The directory to start the game in, by default, this will be the directory that contains the executable.
     */
    public String getWorkingDir() {

        return workingDir;
    }

    public String getRunConfigName() {

        return runConfigName;
    }

    public void setStartCommand(String startCommand) {

        this.startCommand = startCommand;
    }

    public void setProgramArgs(String programArgs) {

        this.programArgs = programArgs;
    }

    public void setWorkingDir(String workingDir) {

        this.workingDir = workingDir;
    }

}