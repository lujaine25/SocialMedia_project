package Backend;

import Backend.Notifications.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static Backend.UserManager.findUser;

public class RealGroup implements Group, Observer {

    private final String name;
    private final String description;
    private ArrayList<Content> contents;
    private ArrayList<User> pendingRequests;
    private final String groupId;
    private final String photoPath;
    private HashMap<User, String> userRoles;


    private RealGroup(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.contents = builder.contents;
        this.groupId = builder.groupId;
        this.photoPath = builder.photoPath;
        this.pendingRequests =builder.pendingRequests;
        this.userRoles = builder.userRoles;
    }

    public HashMap<User, String> getUserRoles() {
        return userRoles;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public ArrayList<User> getPendingRequests() {
        return pendingRequests;
    }

    public ArrayList<Content> getContents() {
        return contents;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getPhotoPath() {
        return photoPath;
    }



    @Override
    public void addContent(Content content) {
       contents.add(content);
       GroupManagement.saveGroupToFile(this);
       User author = findUser(content.getAuthorId());
       sendGroupNotification(author, new GroupPostNotifications(content.getAuthorId(), content.getAuthorUserName(), content.getAuthorUserName() + "has added a post.", "Post", this.getGroupId(), content));
    }

    @Override
    public void removeContent(Content content) {
        contents.remove(content);
        GroupManagement.saveGroupToFile(this);
    }

    @Override
    public void addPendingRequest(User user) {
         pendingRequests.add(user);
        GroupManagement.saveGroupToFile(this);
        update(new Notification(user.getUserId(), "Your request to join " + name + " has been sent", "Default"));
        sendGroupNotification(user, new GroupNotification( user.getUserId(), user.getUserId(), user.getUsername() + " has requested to join " + name, "Group Activity", this.getGroupId()));
    }

    @Override
    public void removePendingRequest(User user) {
        pendingRequests.remove(user);
        GroupManagement.saveGroupToFile(this);
    }

    @Override
    public void addUser(User user, String role) {
        userRoles.put(user, role);
        GroupManagement.saveGroupToFile(this);
    }

    @Override
    public void removeUser(User user) {
        if (userRoles.containsKey(user)) {
            userRoles.remove(user);
            GroupManagement.saveGroupToFile(this);
        }
    }



    @Override
    public void deleteGroup() {

    }

    @Override
    public void promoteUser(User user) {
        if (userRoles.containsKey(user)) {
            userRoles.replace(user, "admin");
            GroupManagement.saveGroupToFile(this);
            update(new Notification(user.getUserId(), "You have been promoted to admin in " + name, "Default"));
            sendGroupNotification(user, new Notification(user.getUserId(), user.getUsername() + " has been promoted to admin", "Default"));
        }
    }

    @Override
    public void demoteUser(User user) {
        if (userRoles.containsKey(user)) {
            userRoles.replace(user, "user");
            GroupManagement.saveGroupToFile(this);
        }
    }

    @Override
    public void approveRequest(User user) {
         userRoles.put(user, "user");
        pendingRequests.remove(user);
        GroupManagement.saveGroupToFile(this);
        update(new Notification(user.getUserId(), "Your request to join " + name + " has been approved", "Default"));
        sendGroupNotification(user, new Notification(user.getUserId(), user.getUsername() + " has joined " + name, "Default"));
    }

    @Override
    public void rejectRequest(User user) {
      pendingRequests.remove(user);
        GroupManagement.saveGroupToFile(this);
    }

    @Override
    public void leaveGroup(User user) {
       userRoles.remove(user);
        GroupManagement.saveGroupToFile(this);
    }

    @Override
    public void update(Notification notification) {
        NotificationManager.addNotification(notification);
    }

    public void update(GroupNotification notification) {
        NotificationManager.addNotification(notification);
    }

    public void update(GroupPostNotifications notification) {
        NotificationManager.addNotification(notification);
    }



    public void sendGroupNotification(User sender, GroupNotification notification){
        for(User user : userRoles.keySet()){
            if(!user.getUserId().equals(sender.getUserId()) && !userRoles.get(user).equals("user")){
                notification.setReceiverUserId(user.getUserId());
                update(notification);
            }
        }
    }

    public void sendGroupNotification(User sender, GroupPostNotifications notification){
        for(User user : userRoles.keySet()){
            if(!user.getUserId().equals(sender.getUserId())){
                notification.setReceiverUserId(user.getUserId());
                update(notification);
            }
        }
    }

    public void sendGroupNotification(User sender, Notification notification){
        for(User user : userRoles.keySet()){
            if(!user.getUserId().equals(sender.getUserId())) {
                notification.setReceiverUserId(user.getUserId());
                update(notification);
            }
        }
    }

    public static class Builder {
        private String name;
        private String description;
        private ArrayList<Content> contents = new ArrayList<>();
        private ArrayList<User> pendingRequests = new ArrayList<>();
        private String groupId;
        private String photoPath;
        private HashMap<User, String> userRoles = new HashMap<>();
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setContents(ArrayList<Content> contents) {
            this.contents = contents;
            return this;
        }

        public Builder addContent(Content content) {
            this.contents.add(content);  // Use this method if adding one content at a time
            return this;
        }

        public Builder setPendingRequests(ArrayList<User> pendingRequests) {
            this.pendingRequests = pendingRequests;
            return this;
        }

        public Builder addPendingRequest(User user) {
            this.pendingRequests.add(user);  // Use this method if adding one user at a time
            return this;
        }

        public Builder setGroupId(String groupId) {
            if(groupId != null){
                this.groupId = groupId;
            }
            else {
                this.groupId = UUID.randomUUID().toString();
            }
            return this;
        }

        public Builder setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
            return this;
        }

        public Builder setUserRoles(HashMap<User, String> userRoles) {
            this.userRoles = userRoles;
            return this;
        }

        public Builder addUserRole(User user, String role) {
            this.userRoles.put(user, role);  // Use this method to add a user with a role
            return this;
        }

        // Build the GroupManagement object
        public RealGroup build() {
            return new RealGroup(this);
        }
    }

    public String toString(){
        return "Group Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Contents: " + contents + "\n" +
                "Pending Requests: " + pendingRequests + "\n" +
                "Group ID: " + groupId + "\n" +
                "Photo Path: " + photoPath + "\n" +
                "User Roles: " + userRoles + "\n";
    }
}
