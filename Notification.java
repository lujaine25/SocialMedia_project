package Backend.Notifications;

public class Notification {
    private String receiverUserId;
    private final String message;
    private final String type;

    public Notification(String receiverUserId, String message, String type) {
        this.receiverUserId = receiverUserId;
        this.message = message;
        this.type = type;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public String getType() { return type; }
    public String getRecipientId() { return receiverUserId; }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

}