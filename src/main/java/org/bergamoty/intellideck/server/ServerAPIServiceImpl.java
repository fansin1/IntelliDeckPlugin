package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import org.bergamoty.intellideck.plugin.PluginAPIServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public final class ServerAPIServiceImpl implements ServerAPIService {
    Socket client;
    DataInputStream in;
    DataOutputStream out;
    private Thread runningServer;

    public void start() {
        int port = 3333;
        PluginAPIServiceImpl pluginService = ServiceManager.getService(PluginAPIServiceImpl.class);
        try (ServerSocket server = new ServerSocket(port)) {
            client = server.accept();
            System.out.println("Connection accepted");
            pluginService.onConnected(); // telling plugin that everything ok and connected

            out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream created");

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
        PluginAPIServiceImpl pluginService = ServiceManager.getService(PluginAPIServiceImpl.class);
        try {
            out.writeUTF("Plugin requested stopping server");
            out.flush();
            in.close();
            out.close();
            client.close();
            pluginService.onDisconnected(); // telling plugin that server is going to stop
            runningServer.interrupt(); // interrupting server thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}