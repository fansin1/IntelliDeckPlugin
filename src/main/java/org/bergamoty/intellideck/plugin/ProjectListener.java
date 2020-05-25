package org.bergamoty.intellideck.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class ProjectListener implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        PluginAPIServiceImpl pluginAPIService = PluginAPIServiceImpl.getInstance();
        pluginAPIService.setProject(project);
    }
}
