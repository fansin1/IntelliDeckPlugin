package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

@Service
public class PluginAPIServiceImpl {
    private final Notifier notifier;
    private ArrayList<Command> commands;
    private Project project;
    private RunManager runManager;

    public PluginAPIServiceImpl() {
        notifier = new Notifier();
    }

    public static PluginAPIServiceImpl getInstance() {
        return ServiceManager.getService(PluginAPIServiceImpl.class);
    }

    public void setProject(Project project) {
        this.project = project;
        this.commands = new ArrayList<>();
    }

    public void updateCommands() {
        if (project == null) {
            return;
        }
        commands.clear();
        runManager = RunManager.getInstance(project);
        List<RunConfiguration> runConfigurations = runManager.getAllConfigurationsList();
        runConfigurations.forEach((runConfiguration) -> commands.add(new RunCommand(runConfiguration, runManager)));
        notifier.notifyInformation(null, "Command list updated");
    }

    public ArrayList<Command> getCommands() {
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