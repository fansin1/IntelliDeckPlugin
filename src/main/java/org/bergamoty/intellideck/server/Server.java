package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.ServiceManager;

import java.io.IOException;

public class Server implements Runnable {
    @Override
    public void run() {
        try {
            ServerAPIServiceImpl serverService = ServiceManager.getService(ServerAPIServiceImpl.class);
            while (!serverService.client.isClosed()) {
                String entry = serverService.in.readUTF();
                String[] terms = entry.split(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
