package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.ServiceManager;
import org.bergamoty.intellideck.plugin.Command;
import org.bergamoty.intellideck.plugin.PluginAPIServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Server implements Runnable {
    @Override
    public void run() {
        int port = 3333;
        try (ServerSocket server = new ServerSocket(port)) {
            Socket client = server.accept();
            System.out.println("Connection accepted");
            PluginAPIServiceImpl.getInstance().onConnected(); // telling plugin that everything ok and connected
            ServerAPIServiceImpl.getInstance().setClient(client);

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream created");
            ServerAPIServiceImpl.getInstance().setOut(out);

            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");
            ServerAPIServiceImpl.getInstance().setIn(in);

            while (!client.isClosed()) {
                String entry = in.readUTF();

                String[] terms = entry.split(" ");

                MainTerms.valueOf(terms[0].toUpperCase()).exec(Arrays.copyOfRange(terms, 1, terms.length));
            }
        } catch (IOException e) {
            PluginAPIServiceImpl.getInstance().onDisconnected();
            e.printStackTrace();
        }
    }

    private enum MainTerms {
        CLICK {
            public void exec(String... args) {
                sendCommand(args[0].toLowerCase());
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
