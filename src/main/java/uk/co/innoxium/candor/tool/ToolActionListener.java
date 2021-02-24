package uk.co.innoxium.candor.tool;

import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.util.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class ToolActionListener implements ActionListener {

    private final Tool theTool;

    public ToolActionListener(Tool tool) {

        this.theTool = tool;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        RunConfig runConfig = theTool.runConfig;

        ProcessBuilder builder = new ProcessBuilder();
        builder.command(runConfig.getStartCommand() + " " + runConfig.getProgramArgs());
        String workingDir = runConfig.getWorkingDir();
        if(workingDir != null && !workingDir.isEmpty()) {

            builder.directory(new File(workingDir));
        }
        try {

            Logger.info(builder.command().toString());
            Process process = builder.start();
        } catch(IOException ioException) {

            ioException.printStackTrace();
        }
    }
}
