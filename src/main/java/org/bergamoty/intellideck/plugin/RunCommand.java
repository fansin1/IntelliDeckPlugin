package org.bergamoty.intellideck.plugin;


import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;

import java.util.Objects;


public class RunCommand implements Command {

    private final static String COMMAND_TYPE="Run-";
    private final RunConfiguration runConfiguration;
    private final RunManager runManager;

    public RunCommand(RunConfiguration runConfiguration, RunManager runManager) {
        this.runConfiguration = runConfiguration;
        this.runManager = runManager;
    }

    @Override
    public String getName() {
        return COMMAND_TYPE + runConfiguration.getName();
    }

    @Override
    public void run() {
        ProgramRunnerUtil.executeConfiguration(Objects.requireNonNull(runManager.findSettings(runConfiguration)),
                DefaultRunExecutor.getRunExecutorInstance());
    }

}
