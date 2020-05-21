package org.bergamoty.intellideck.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public abstract class Notifier {
    private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("IntelliDeck notifications",
            NotificationDisplayType.BALLOON, true);

    public static void notifyInformation(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION);
        notification.notify(project);
    }

    public static void notifyError(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR);
        notification.notify(project);
    }
}
