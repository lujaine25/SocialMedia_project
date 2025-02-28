package Backend.Notifications;

public class GroupNotification extends Notification {
    private final String groupId;
    private final String senderId;

    public GroupNotification(String senderId, String recipientId, String message, String type, String groupId) {
        super(recipientId, message, type);
        this.groupId = groupId;
        this.senderId = senderId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getSenderId() {
        return senderId;
    }
}
