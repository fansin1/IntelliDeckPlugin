package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public final class PluginAPIServiceImpl implements PluginAPIService {
    private ArrayList<Command> commands;
    private Notifier notifier;
    private final Project project;
    private final RunManager runManager;


    public PluginAPIServiceImpl(Project project) {
        this.project = project;
        notifier = new Notifier();
        runManager = RunManager.getInstance(Objects.requireNonNull(this.project));
        List<RunConfiguration> runConfigurations = runManager.getAllConfigurationsList();
        commands = new ArrayList<Command>();

    }


    public void updateCommands(List<Command> newCommands) {
        commands.clear();
        List<RunConfiguration> runConfigurations = runManager.getAllConfigurationsList();
        runConfigurations.forEach((runConfiguration) -> commands.add(new RunCommand(runConfiguration, runManager)));
        notifier.notifyInformation(null, "Command list updated");
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void executeCommand(Command command) {
        notifier.notifyInformation(null, "Command " + command.getName() + " ran remotely");
        command.run();
    }


    public void onConnected() {
        notifier.notifyInformation(null, "Connection established");
    }

    public void onDisconnected() {
        notifier.notifyError(null, "Connection closed");
    }

}