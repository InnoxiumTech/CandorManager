package uk.co.innoxium.candor.window.component;

import uk.co.innoxium.candor.game.GamesList;
import uk.co.innoxium.candor.module.RunConfig;
import uk.co.innoxium.candor.util.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class RunConfigMenuItem extends JMenuItem {

    private final RunConfig runConfig;

    public RunConfigMenuItem(RunConfig runConfig) {

        super("Launch: " + runConfig.getRunConfigName());
        this.runConfig = runConfig;
//        this.addMouseListener(new RunConfigMouseHandler());
//        this.addActionListener(this::onClicked);
    }

    private void onClicked(ActionEvent e) {

        System.out.println(e.getActionCommand());
        System.out.println(e.getModifiers());
        boolean rightClick = e.getModifiers() == 4; // This is super hacky.
        System.out.println(rightClick);

        if(rightClick) {

            JPopupMenu menu = new JPopupMenu();

            JMenuItem delete = new JMenuItem();
            delete.addActionListener(this::delete);
            menu.add(delete);

            menu.show(this, this.getX(), this.getY());
        } else {

            ProcessBuilder builder = new ProcessBuilder();
            builder.command(runConfig.getStartCommand(), runConfig.getProgramArgs());
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

    private void delete(ActionEvent actionEvent) {

        AtomicReference<RunConfig> ret = new AtomicReference<>();

        GamesList.getCurrentGame().customLaunchConfigs.forEach(runConfig -> {

            if(runConfig.getRunConfigName().equals(this.runConfig.getRunConfigName())) {

                ret.set(runConfig);
            }
        });

        GamesList.getCurrentGame().customLaunchConfigs.remove(ret.get());
    }

    public RunConfig getRunConfig() {

        return runConfig;
    }

    private class RunConfigMouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if(SwingUtilities.isLeftMouseButton(e)) {

                ProcessBuilder builder = new ProcessBuilder();
                builder.command(runConfig.getStartCommand(), runConfig.getProgramArgs());
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
            } else if(SwingUtilities.isRightMouseButton(e)) {

                JPopupMenu menu = new JPopupMenu();

                JMenuItem delete = new JMenuItem();
//                delete.addActionListener(a -> delete(a, (RunConfigMenuItem)e.getComponent()));
                menu.add(delete);

                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }


    }
}
