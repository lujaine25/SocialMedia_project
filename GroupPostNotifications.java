package Backend.Notifications;

import Backend.Content;

public class GroupPostNotifications extends GroupNotification {
    Content content;

    public GroupPostNotifications(String authorId,String recipientId, String message, String type, String groupId, Content content) {
        super(authorId,recipientId, message, type, groupId);
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

}
