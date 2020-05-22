package org.bergamoty.intellideck.server;

import org.bergamoty.intellideck.plugin.PluginAPI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerAPI {
    static Socket client = null;
    static DataInputStream in = null;
    static DataOutputStream out = null;
    private static Thread runningServer = null;

    public static void main(String[] args) {

    }

    public void start() {
        int port = 3333;
        try (ServerSocket server = new ServerSocket(port)) {
            client = server.accept();
            System.out.println("Connection accepted");
            // PluginAPI.onConnected();

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

    public void stop() {
        System.out.println("Stopping server...");
        try {
            out.writeUTF("The plugin requested the server be stopped");
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
}