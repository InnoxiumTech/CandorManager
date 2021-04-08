package uk.co.innoxium.candor.process;

import uk.co.innoxium.candor.module.RunConfig;


public class ProcessLauncher {

    /**
     * Checks whether a process is currently running and if so, returns it's process id
     * @param processName - the process to check, i.e "someFile.exe"
     * @return The pid of the process, if the process is ot running, it returns -1
     */
    public int isProcessRunning(String processName) {

        return -1;
    }

    /**
     * Starts a process from a pre-build ProcessBuilder
     * @param builder - the builder to start the process from
     * @return - The process handle, else null
     */
    public Process startProcess(ProcessBuilder builder) {

        return null;
    }

    /**
     * Starts a process from a RunConfig, first checking if the process is already running.
     * @param launchConfig - the RunConfig to build a process from.
     * @return - The process handle, else null
     */
    public Process startProcess(RunConfig launchConfig) {

        return null;
    }
}
