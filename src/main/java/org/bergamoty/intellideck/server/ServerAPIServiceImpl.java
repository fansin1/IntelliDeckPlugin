package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import org.bergamoty.intellideck.plugin.Command;
import org.bergamoty.intellideck.plugin.PluginAPIServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

@Service
public final class ServerAPIServiceImpl {
    private static final int PORT = 3333;

    private ServerSocket serverSocket;
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread runningServer;
    private Server livingServer;
    private ArrayList<Command> allowedCommands;
    private boolean isConnected;

    public static ServerAPIServiceImpl getInstance() {
        return ServiceManager.getService(ServerAPIServiceImpl.class);
    }

    public ServerAPIServiceImpl() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        livingServer = new Server(serverSocket);
        runningServer = new Thread(livingServer);
        runningServer.start();
    }

    public void stop() {
        System.out.println("Stopping server...");
        try {
            if (isConnected()) {
                out.writeUTF("Plugin requested stopping server");
                out.flush();
                in.close();
                out.close();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            PluginAPIServiceImpl.getInstance().onDisconnected(); // telling plugin that server is going to stop
            setConnected(false);
            if (livingServer != null) {
                livingServer.closeServerSocket();
            }
        }
    }

    public void updateCommands(ArrayList<Command> commands) {
        updateLocalCommands(commands);
        if (isConnected()) {
            updateRemoteCommands();
        }
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public ArrayList<Command> getAllowedCommands() {
        return this.allowedCommands;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    private void updateLocalCommands(ArrayList<Command> commands) {
        allowedCommands = commands;
    }

    void updateRemoteCommands() {
        StringBuilder allCommands = new StringBuilder();
        allCommands.append("Tasks ");
        allCommands.append(allowedCommands.size());
        for (Command command : allowedCommands) {
            allCommands.append("\n");
            allCommands.append(command.getName());
        }
        try {
            out.writeUTF(allCommands.toString());
            out.flush();
        } catch (IOException e) {
            PluginAPIServiceImpl.getInstance().onDisconnected();
            e.printStackTrace();
        }
    }
}
