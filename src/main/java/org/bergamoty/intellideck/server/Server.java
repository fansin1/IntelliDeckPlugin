package org.bergamoty.intellideck.server;

import java.io.IOException;

public class Server implements Runnable {
    @Override
    public void run() {
        try {
            while (!ServerAPI.client.isClosed()) {
                String entry = ServerAPI.in.readUTF();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
