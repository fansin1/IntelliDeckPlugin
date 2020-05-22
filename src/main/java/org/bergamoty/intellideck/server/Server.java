package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.ServiceManager;
import org.bergamoty.intellideck.plugin.Command;
import org.bergamoty.intellideck.plugin.PluginAPIServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Server implements Runnable {
    @Override
    public void run() {
        try {
            ServerAPIServiceImpl serverService = ServiceManager.getService(ServerAPIServiceImpl.class);

            while (!serverService.client.isClosed()) {
                String entry = serverService.in.readUTF();

                String[] terms = entry.split(" ");

                MainTerms.valueOf(terms[0]).exec(Arrays.copyOfRange(terms, 1, terms.length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum MainTerms {
        CLICK {
            public void exec(String... args) {
                sendCommand(args[0]);
            }
        }, EXIT {
            public void exec(String... args) {
                ServerAPIServiceImpl serverService = ServiceManager.getService(ServerAPIServiceImpl.class);
                serverService.stop();
            }
        };

        public abstract void exec(String... args);

        void sendCommand(String commandName) {
            PluginAPIServiceImpl pluginService = ServiceManager.getService(PluginAPIServiceImpl.class);
            ArrayList<Command> allowedCommand = pluginService.getCommands();
            List<Command> result = allowedCommand
                    .stream()
                    .filter(command -> command.getName().equalsIgnoreCase(commandName))
                    .collect(Collectors.toList());
            pluginService.executeCommand(result.get(0));
        }
    }
}
