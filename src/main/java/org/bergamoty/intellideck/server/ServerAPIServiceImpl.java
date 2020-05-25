package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import org.bergamoty.intellideck.plugin.Command;
import org.bergamoty.intellideck.plugin.PluginAPIServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

@Service
public final class ServerAPIServiceImpl {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread runningServer;
    private ArrayList<Command> allowedCommands;

    public static ServerAPIServiceImpl getInstance() {
        return ServiceManager.getService(ServerAPIServiceImpl.class);
    }

    public void start() {
        runningServer = new Thread(new Server());
        runningServer.start();
    }

    public void stop() {
        System.out.println("Stopping server...");
        try {
            out.writeUTF("Plugin requested stopping server");
            out.flush();
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            PluginAPIServiceImpl.getInstance().onDisconnected(); // telling plugin that server is going to stop
            runningServer.interrupt(); // interrupting server thread
        }
    }

    public void updateCommands(ArrayList<Command> commands) {
        allowedCommands = commands;
        StringBuilder allCommands = new StringBuilder();
        for (Command command : commands) {
            allCommands.append(command.getName());
            allCommands.append(" ");
        }
        try {
            out.writeUTF(allCommands.toString());
            out.flush();
        } catch (IOException e) {
            PluginAPIServiceImpl.getInstance().onDisconnected();
            e.printStackTrace();
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
}