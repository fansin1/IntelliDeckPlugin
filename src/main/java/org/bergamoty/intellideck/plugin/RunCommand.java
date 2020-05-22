package org.bergamoty.intellideck.plugin;


import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;

import java.util.Objects;


public class RunCommand implements Command {

    private final RunConfiguration runConfiguration;
    private final RunManager runManager;

    public RunCommand(RunConfiguration runConfiguration, RunManager runManager) {
        this.runConfiguration = runConfiguration;
        this.runManager = runManager;
    }

    @Override
    public String getName() {
        return runConfiguration.getName();
    }

    @Override
    public void run() {
        ProgramRunnerUtil.executeConfiguration(Objects.requireNonNull(runManager.findSettings(runConfiguration)),
                DefaultDebugExecutor.getDebugExecutorInstance());
    }
}