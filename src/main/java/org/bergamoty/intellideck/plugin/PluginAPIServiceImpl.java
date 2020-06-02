package org.bergamoty.intellideck.plugin;

import com.intellij.execution.RunManager;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.bergamoty.intellideck.server.ServerAPIServiceImpl;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PluginAPIServiceImpl {
    private final Notifier notifier;
    private ArrayList<Command> commands;
    private Project project;
    private RunManager runManager;

    public PluginAPIServiceImpl() {
        notifier = new Notifier();
    }

    public static PluginAPIServiceImpl getInstance() {
        return ServiceManager.getService(PluginAPIServiceImpl.class);
    }

    public void setProject(Project project) {
        this.project = project;
        this.commands = new ArrayList<>();
    }

    public void updateCommands() {
        if (project == null) {
            return;
        }
        commands.clear();
        runManager = RunManager.getInstance(project);
        List<RunConfiguration> runConfigurations = runManager.getAllConfigurationsList();
        runConfigurations.forEach((runConfiguration) -> commands.add(new RunCommand(runConfiguration, runManager)));
        runConfigurations.forEach((runConfiguration) -> commands.add(new DebugCommand(runConfiguration, runManager)));
        ServerAPIServiceImpl.getInstance().updateCommands(commands);
        notifier.notifyInformation(null, "Command list updated");
    }

    public void executeCommand(Command command) {
        notifier.notifyInformation(null, "Command " + command.getName() + " ran remotely");
        command.run();
    }

    public void onConnected() {
        notifier.notifyInformation(null, "Connection established");
    }

    public void onDisconnected() {
        notifier.notifyError(null, "Connection closed");
    }

    public void onServerStarted() throws IOException, InterruptedException{
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            Process myAppProcess = Runtime.getRuntime().exec("netsh.exe advfirewall firewall show rule name=\"IntelliDeck\"");
            myAppProcess.waitFor();
            BufferedReader input = new BufferedReader(new InputStreamReader(myAppProcess.getInputStream()));

            boolean check = false;
            String line = input.readLine();
            while (line != null) {
                if (line.contains("IntelliDeck")) {
                    check = true;
                }
                line = input.readLine();
            }

            if (!check) {
                JOptionPane.showMessageDialog(null, "Due you are Windows user, you need to let plugin open ports",
                        "About using our plugin", JOptionPane.INFORMATION_MESSAGE);
                myAppProcess = Runtime.getRuntime().exec("powershell.exe Start-Process netsh -Argument " +
                        "'advfirewall firewall add rule name=\"IntelliDeck\" dir=in protocol=tcp localport=3333" +
                        " action=allow' -verb RunAs");
                myAppProcess.waitFor();
                myAppProcess = Runtime.getRuntime().exec("powershell.exe Start-Process netsh -Argument " +
                        "'advfirewall firewall add rule name=\"IntelliDeck\" dir=out protocol=tcp localport=3333" +
                        " action=allow' -verb RunAs");
                myAppProcess.waitFor();
            }
        }
        notifier.notifyInformation(null, "Server started");
        updateCommands();
        ServerAPIServiceImpl.getInstance().updateCommands(commands);
    }

    public void onServerStopped() {
        notifier.notifyError(null, "Server stopped");
    }
}
