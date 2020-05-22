package org.bergamoty.intellideck.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class Notifier {
    private final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("IntelliDeck notifications",
            NotificationDisplayType.BALLOON, true);

    public void notifyInformation(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION);
        notification.notify(project);
    }

    public void notifyError(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR);
        notification.notify(project);
    }
}
