package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PluginAPI {

    private List<Command> commands;
    private Notifier notifier;

    public void setCommands(List<Command> commands){
        this.commands = commands;
    }

    public boolean executeCommand(String commandName) {
        List<Command> result = commands
                .stream()
                .filter(command -> commandName.equals(command.getName()))
                .collect(Collectors.toList());
        if (result.size() == 0) {
            //TODO notify user in ide(0)
            return false;
        } else if (result.size() > 0) {
            //TODO notify user in ide(are you ahereli tam)
            return false;
        } else {
            result.get(0).run();
            return true;
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void onConnected() {
        //TODO notify user in ide(success)
    }

    public void onDisconnected() {
        //TODO notify user in ide(disconnected)
    }

}
