package org.bergamoty.intellideck.server;

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
    private ServerSocket serverSocket;
    private boolean isStopped;

    @Override
    public void run() {
        int port = 3333;
        try {
            setServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Socket client = serverSocket.accept();
            System.out.println("Connection accepted");
            PluginAPIServiceImpl.getInstance().onConnected(); // telling plugin that everything ok and connected
            ServerAPIServiceImpl.getInstance().setClient(client);

            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream created");
            ServerAPIServiceImpl.getInstance().setOut(out);

            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");
            ServerAPIServiceImpl.getInstance().setIn(in);

            ServerAPIServiceImpl.getInstance().setConnected(true);

            while (!client.isClosed()) {
                String entry = in.readUTF();

                String[] terms = entry.split(" ");

                MainTerms.valueOf(terms[0].toUpperCase()).exec(Arrays.copyOfRange(terms, 1, terms.length));
            }
        } catch (IOException e) {
            PluginAPIServiceImpl.getInstance().onDisconnected();
            ServerAPIServiceImpl.getInstance().setConnected(false);
            if (!isStopped) {
                ServerAPIServiceImpl.getInstance().start();
            }
        }
    }

    public void closeServerSocket() {
        isStopped = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            // vse horosho
        }
    }

    public void setServerSocket(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    private enum MainTerms {
        CLICK {
            public void exec(String... args) {
                sendCommand(args[0].toLowerCase());
            }
        }, EXIT {
            public void exec(String... args) {
                ServerAPIServiceImpl.getInstance().stop();
            }
        };

        public abstract void exec(String... args);

        void sendCommand(String commandName) {
            ArrayList<Command> allowedCommand = ServerAPIServiceImpl.getInstance().getAllowedCommands();
            List<Command> result = allowedCommand
                    .stream()
                    .filter(command -> command.getName().equalsIgnoreCase(commandName))
                    .collect(Collectors.toList());
            PluginAPIServiceImpl.getInstance().executeCommand(result.get(0));
        }
    }
}
