package Backend.Notifications;

public class RequestNotification extends Notification {
    String userId;

    public RequestNotification(String userId, String recipientId, String message, String type) {
        super(recipientId, message, type);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

}
