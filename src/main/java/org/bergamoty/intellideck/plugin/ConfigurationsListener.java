package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;

public class ConfigurationsListener implements RunManagerListener {

    PluginAPIServiceImpl pluginAPIService = PluginAPIServiceImpl.getInstance();

    @Override
    public void runConfigurationAdded(@NotNull RunnerAndConfigurationSettings settings) {
        if(pluginAPIService != null){
            pluginAPIService.updateCommands();
        }
    }

    @Override
    public void runConfigurationRemoved(@NotNull RunnerAndConfigurationSettings settings) {
        if(pluginAPIService != null){
            pluginAPIService.updateCommands();
        }
    }

}
