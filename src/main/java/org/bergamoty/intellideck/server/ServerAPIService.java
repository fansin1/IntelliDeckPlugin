package org.bergamoty.intellideck.server;

import com.intellij.openapi.components.ServiceManager;

public interface ServerAPIService {
    static ServerAPIService getInstance() {
        return ServiceManager.getService(ServerAPIService.class);
    }
}