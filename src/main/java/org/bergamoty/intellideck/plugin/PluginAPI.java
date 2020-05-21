package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PluginAPI {

    private List<Command> commands;
    private Notifier notifier;

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void executeCommand(Command command) {
        command.run();
    }


    public void onConnected() {
        //TODO notify user in ide(success)
    }

    public void onDisconnected() {
        //TODO notify user in ide(disconnected)
    }

}
