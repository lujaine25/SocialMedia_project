package Backend.Notifications;

import java.util.ArrayList;

import Backend.*;
import org.json.JSONArray;
import org.json.JSONObject;

import static Backend.ContentManager.getContent;

public class NotificationManager implements Subject {
    private static final String NOTIFICATIONS_FILE_PATH = "databases/notifications.json";
    private static final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private static JSONArray notificationsArray = databaseManager.readJSONFile(NOTIFICATIONS_FILE_PATH);
    private final ArrayList<Observer> observers;
    private final ArrayList<Notification> notifications;
    private static NotificationManager instance;

    private NotificationManager() {
        observers = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    public static void addNotification(Notification notification) {
        if (notificationsArray == null) {
            notificationsArray = new JSONArray();
        }
        JSONObject notificationJson = new JSONObject();
        notificationJson.put("message", notification.getMessage());
        notificationJson.put("type", notification.getType());
        notificationJson.put("RecipientId", notification.getRecipientId());
        if(notification.getType().equalsIgnoreCase("friend Request")) {
            notificationJson.put("SenderUserId", ((RequestNotification) notification).getUserId());
        }
        else
       if(notification.getType().equalsIgnoreCase("group Activity")) {
            notificationJson.put("SenderUserId", ((GroupNotification) notification).getSenderId());
            notificationJson.put("GroupId", ((GroupNotification) notification).getGroupId());
        }
        else if(notification.getType().equalsIgnoreCase("Post")) {
            notificationJson.put("SenderUserId", ((GroupPostNotifications) notification).getSenderId());
            notificationJson.put("GroupId", ((GroupNotification) notification).getGroupId());
            notificationJson.put("ContentId", ((GroupPostNotifications) notification).getContent().getContentId());
        }

        notificationsArray.put(notificationJson);
        instance.notifyObservers();
        databaseManager.writeJSONFile(NOTIFICATIONS_FILE_PATH, notificationsArray);
    }

   public static ArrayList<Notification> getNotifications(String userId) {
        notificationsArray = databaseManager.readJSONFile(NOTIFICATIONS_FILE_PATH);
    ArrayList<Notification> userNotifications = new ArrayList<>();
    if (notificationsArray != null) {
        for (int i = 0; i < notificationsArray.length(); i++) {
            JSONObject notificationJson = notificationsArray.getJSONObject(i);
            if (notificationJson.getString("RecipientId").equals(userId)) {
                String type = notificationJson.getString("type");
                Notification notification = switch (type) {
                    case "Friend Request" -> new RequestNotification(
                            notificationJson.getString("SenderUserId"),
                            notificationJson.getString("message"),
                            type,
                            notificationJson.getString("RecipientId")
                    );
                    case "Group Activity" -> new GroupNotification(
                            notificationJson.getString("SenderUserId"),
                            notificationJson.getString("RecipientId"),
                            notificationJson.getString("message"),
                            type,
                            notificationJson.getString("GroupId")
                    );
                    case "Post" -> new GroupPostNotifications(
                            notificationJson.getString("SenderUserId"),
                            notificationJson.getString("RecipientId"),
                            notificationJson.getString("message"),
                            type,
                            notificationJson.getString("GroupId"),
                            getContent(notificationJson.getString("ContentId"))
                    );
                    default -> new Notification(
                            notificationJson.getString("RecipientId"),
                            notificationJson.getString("message"),
                            type
                    );
                };
                userNotifications.add(notification);
            }
        }
    }
    return userNotifications;
}

    public static void removeNotification(Notification notification) {
        if (notificationsArray != null) {
            for (int i = 0; i < notificationsArray.length(); i++) {
                JSONObject notificationJson = notificationsArray.getJSONObject(i);
                if (notificationJson.getString("RecipientId").equals(notification.getRecipientId()) && notificationJson.getString("message").equals(notification.getMessage())) {
                    notificationsArray.remove(i);
                    databaseManager.writeJSONFile(NOTIFICATIONS_FILE_PATH, notificationsArray);
                    break;
                }
            }
        }
    }

   public static synchronized void removeAllRelatedNotifications(GroupNotification notification) {
    notificationsArray = databaseManager.readJSONFile(NOTIFICATIONS_FILE_PATH);
    if (notificationsArray != null) {
        for (int i = 0; i < notificationsArray.length(); i++) {
            JSONObject notificationJson = notificationsArray.getJSONObject(i);
            if(notificationJson.getString("type").equalsIgnoreCase("Group Activity")) {
                if (notificationJson.getString("SenderUserId").equals(notification.getSenderId()) && notificationJson.getString("message").equals(notification.getMessage()) && notificationJson.getString("GroupId").equals(notification.getGroupId())) {
                    notificationsArray.remove(i);
                    i--; // Adjust the index after removal
                }
            }
        }
        databaseManager.writeJSONFile(NOTIFICATIONS_FILE_PATH, notificationsArray);
    }
}


    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            for (Notification notification : notifications) {
                observer.update(notification);
            }
        }
    }
}
