package org.bergamoty.intellideck.plugin;

import com.intellij.openapi.components.ServiceManager;

public interface PluginAPIService {
    static PluginAPIService getInstance() {
        return ServiceManager.getService(PluginAPIService.class);
    }
}
