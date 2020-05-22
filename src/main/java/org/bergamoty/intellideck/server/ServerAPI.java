package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.ServiceManager;

public interface ServerAPI {
    static ServerAPI getInstance() {
        return ServiceManager.getService(ServerAPI.class);
    }
}
