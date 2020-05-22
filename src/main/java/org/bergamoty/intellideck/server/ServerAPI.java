package org.bergamoty.intellideck.server;

import org.bergamoty.intellideck.plugin.Command;
import org.bergamoty.intellideck.plugin.PluginAPI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public abstract class ServerAPI {

    public static void main(String[] args) {

    }

    public static void start() {
        int port = 3333;
        try (ServerSocket server = new ServerSocket(port)) {
            client = server.accept();
            System.out.println("Connection accepted");
            PluginAPI.onConnected();

            out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");

            in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");

            runningServer = new Thread(new Server());
            runningServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        System.out.println("Stopping server...");
        try {
            out.writeUTF("Plugin requested stopping server");
            out.flush();
            in.close();
            out.close();
            client.close();
            PluginAPI.onDisconnected();
            runningServer.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Thread runningServer = null;
    static Socket client = null;
    static DataInputStream in = null;
    static DataOutputStream out = null;
}