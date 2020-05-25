package org.bergamoty.intellideck.plugin;


import com.intellij.execution.*;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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

        RunnerAndConfigurationSettings runConfig = runManager.findConfigurationByName(runConfiguration.getName());
        Executor executor = DefaultRunExecutor.getRunExecutorInstance();
        ExecutionTarget target = getExecutionTarget(runConfiguration.getProject(), Objects.requireNonNull(runConfig));
        ApplicationManager.getApplication().invokeLater(() ->
                ExecutionUtil.runConfiguration(runConfig, executor, target)
        );
    }

    @NotNull
    private ExecutionTarget getExecutionTarget(@NotNull Project project, @NotNull RunnerAndConfigurationSettings runConfig) {
        ExecutionTargetManager targetManager = ExecutionTargetManager.getInstance(project);
        ExecutionTarget active = targetManager.getActiveTarget();

        List<ExecutionTarget> targets = targetManager.getTargetsFor(runConfig.getConfiguration());
        for (ExecutionTarget target : targets) {
            if (target.getId().equals(DefaultRunExecutor.getRunExecutorInstance().getId())) {
                return target;
            }
        }
        return active;
    }
}
