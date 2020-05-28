package org.bergamoty.intellideck.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.NotIgnored;
import com.thoughtworks.qdox.model.expression.Not;
import org.bergamoty.intellideck.server.ServerAPIServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PluginButton extends AnAction {

    private static final Icon ON_ICON = IconLoader.getIcon("/icons/plugOn.svg");
    private static final Icon OFF_ICON = IconLoader.getIcon("/icons/plugOff.svg");

    private boolean buttonState = false;
    private final Notifier notifier = new Notifier();

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        buttonState = !buttonState;
        if(buttonState){
            ServerAPIServiceImpl.getInstance().start();
            PluginAPIServiceImpl.getInstance().onServerStarted();
        }
        else{
            ServerAPIServiceImpl.getInstance().stop();
            PluginAPIServiceImpl.getInstance().onServerStopped();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        if(buttonState){
            e.getPresentation().setIcon(ON_ICON);
        }
        else{
            e.getPresentation().setIcon(OFF_ICON);
        }
        super.update(e);
    }
}
