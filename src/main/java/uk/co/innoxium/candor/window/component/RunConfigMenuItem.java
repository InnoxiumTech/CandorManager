package uk.co.innoxium.candor.window.component;

import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.process.ProcessLauncher;
import uk.co.innoxium.candor.window.dialog.SwingDialogs;

import javax.swing.*;
import java.io.IOException;


public class RunConfigMenuItem extends JMenuItem {

    private final RunConfig runConfig;

    public RunConfigMenuItem(RunConfig runConfig) {

        super("Launch: " + runConfig.getRunConfigName());
        this.runConfig = runConfig;

        this.addActionListener(e -> {

            try {

                Process process = ProcessLauncher.startProcess(runConfig);
                if(process == null) {

                    SwingDialogs.showInfoMessage("Warning", "The game is currently running and will not be launched twice.", JOptionPane.WARNING_MESSAGE);
                }
            } catch(IOException ioException) {

                ioException.printStackTrace();
            }
        });
    }

    public RunConfig getRunConfig() {

        return runConfig;
    }
}
