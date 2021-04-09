package uk.co.innoxium.candor.process;

import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.util.Logger;

import java.io.File;
import java.io.IOException;


public class ProcessLauncher {

    /**
     * Checks whether a process is currently running and if so, returns it's process id
     * @param processName - the process to check, i.e "someFile.exe"
     * @return The pid of the process, if the process is not running, it returns -1
     */
    public static long isProcessRunning(String processName) {

        long[] ret = new long[] { -1 };

        ProcessHandle.allProcesses().forEach(processHandle -> {

            if(processHandle.info().command().isPresent() && processHandle.info().command().get().equalsIgnoreCase(processName)) {

                System.out.println(processHandle.pid());
                ret[0] = processHandle.pid();
            }
        });

        return ret[0];
    }

    /**
     * Starts a process from a pre-build ProcessBuilder
     * @param builder - the builder to start the process from
     * @return - The process handle, else null
     */
    public static Process startProcess(ProcessBuilder builder) throws IOException {

        Logger.info("Launching with command > " + builder.command().toString());
        return builder.start();
    }

    /**
     * Starts a process from a RunConfig, first checking if the process is already running.
     * @param launchConfig - the RunConfig to build a process from.
     * @return - The process handle, else null
     */
    public static Process startProcess(RunConfig launchConfig) throws IOException {

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(launchConfig.getStartCommand(), launchConfig.getProgramArgs());
        String workingDir = launchConfig.getWorkingDir();
        if(workingDir != null && !workingDir.isEmpty()) {

            builder.directory(new File(workingDir));
        }
        if(isProcessRunning(launchConfig.getStartCommand()) == -1) {

            return startProcess(builder);
        } else {

            Logger.warn("Process > " + builder.command().get(0) + " is already running, will not start.");
            return null;
        }
    }
}
