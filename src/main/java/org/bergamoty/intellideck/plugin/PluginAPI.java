package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManager;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PluginAPI {

    private static List<Command> commands;
    private Notifier notifier;

    public static void setCommands(List<Command> newCommands) {
        commands = newCommands;
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static void executeCommand(Command command) {
        command.run();
    }


    public static void onConnected() {
        Notifier.notifyInformation(null, "Connection established");
    }

    public static void onDisconnected() {
        Notifier.notifyError(null, "Connection closed");
    }

}
