package Frontend;

import Backend.*;
import Backend.Notifications.*;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static Backend.UserManager.findUser;


public class NewsFeed_Updates {
    private static final UserManager userManager = UserManager.getInstance();
    private static final ContentManager contentManager = ContentManager.getInstance();
    private static final GroupManagement groupManagement = GroupManagement.getInstance();
    private static Timer timer;

    public static void RefreshNewsFeed(User user, JScrollPane friendsList, JScrollPane suggestedFriendPanel, JScrollPane postPanel, JScrollPane storyPanel, JScrollPane NotificationPanel, JScrollPane groupsList, JScrollPane groupsSuggestionList) {
        contentManager.readContent();
        userManager.loadAllUsers();
        userManager.loadAllFriends();
        groupManagement.loadGroups();
        UpdatePosts(user, postPanel);

        // Start the file watcher to monitor changes in files
        FileWatcher fileWatcher = new FileWatcher(Paths.get("databases"), () -> {
            contentManager.readContent();
            userManager.loadAllUsers();
            userManager.loadAllFriends();
            groupManagement.loadGroups();
            UpdateFriends(user, friendsList);
            UpdateStories(user, storyPanel);
            UpdateSuggestedFriends(user, suggestedFriendPanel);
            UpdateNotifications(user, NotificationPanel);
            UpdateGroups(user, groupsList);
            UpdateGroupSuggestions(user, groupsSuggestionList);
        });
        new Thread(fileWatcher).start();
    }

    public static void UpdateFriends(User user, JScrollPane friendsList) {
        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);

        for (int i = 0; i < user.getFriendManagement().getFriends().size(); i++) {
            JLabel friendLabel = new JLabel(user.getFriendManagement().getFriends().get(i).getUsername());
            System.out.println(user.getFriendManagement().getFriends().get(i).getStatus());
            JLabel statusLabel = new JLabel(user.getFriendManagement().getFriends().get(i).getStatus());
            JPanel friendPanel = new JPanel();
            friendPanel.add(friendLabel);
            friendPanel.add(statusLabel);
            containerPanel.add(friendPanel);
        }
        friendsList.setViewportView(containerPanel);
    }

    public static void UpdateSuggestedFriends(User user, JScrollPane suggestedFriendPanel) {
        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);

        for (int i = 0; i < user.getFriendManagement().getSuggestedFriends().size(); i++) {
            User suggestedFriend = user.getFriendManagement().getSuggestedFriends().get(i);
            JLabel friendLabel = new JLabel(suggestedFriend.getUsername());
            JButton addFriendButton = new JButton("Add Friend");
            addFriendButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                user.sendFriendRequest(suggestedFriend);
                JOptionPane.showMessageDialog(null, "Friend Request Sent");
                user.getFriendManagement().fillSuggestedFriends();
                UpdateSuggestedFriends(user, suggestedFriendPanel);
            });
            JPanel innerSuggestedFriendPanel = new JPanel();
            innerSuggestedFriendPanel.add(friendLabel);
            innerSuggestedFriendPanel.add(addFriendButton);
            containerPanel.add(innerSuggestedFriendPanel);
        }
        suggestedFriendPanel.setViewportView(containerPanel);
    }

public static void UpdatePosts(User user, JScrollPane postPanel) {
    JPanel containerPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
    containerPanel.setLayout(boxLayout);


    // Display friends' posts
    for (Content post : user.getFriendsPosts()) {
        addPostToPanel(post, containerPanel, postPanel, null);
    }

    // Display group posts
    for (RealGroup group : user.getGroups()) {
        for (Content post : group.getContents()) {
            addPostToPanel(post, containerPanel, postPanel, group.getGroupId());
        }
    }

    postPanel.setViewportView(containerPanel);
}

private static void addPostToPanel(Content post, JPanel containerPanel, JScrollPane postPanel, String flag) {
    JLabel postLabel = new JLabel(post.getContent());
    JLabel nameLabel = new JLabel(post.getAuthorUserName());
    if(flag != null){
        nameLabel.setText(nameLabel.getText() + " in " + Objects.requireNonNull(GroupManagement.getGroup(flag)).getName());
    }
    long time = post.getTime().until(LocalDateTime.now(), ChronoUnit.MINUTES);
    if (time > 60 && time < 120) {
        time = post.getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
        nameLabel.setText(nameLabel.getText() + " " + time + " hour ago");
    } else if (time > 120 && time < 1440) {
        time = post.getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
        nameLabel.setText(nameLabel.getText() + " " + time + " hours ago");
    } else if (time > 1440 && time < 2880) {
        time = post.getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
        nameLabel.setText(nameLabel.getText() + " " + time + " day ago");
    } else if (time > 2880) {
        time = post.getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
        nameLabel.setText(nameLabel.getText() + " " + time + " days ago");
    } else {
        nameLabel.setText(nameLabel.getText() + " " + time + " minutes ago");
    }
    // Resize the image
    ImageIcon imageIcon = new ImageIcon(post.getImagePath());
    Image image = imageIcon.getImage();
    Image resizedImage = image.getScaledInstance(postPanel.getWidth(), 300, Image.SCALE_SMOOTH);
    ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

    JLabel photo = new JLabel(resizedImageIcon);
    JPanel innerPostPanel = new JPanel();
    innerPostPanel.setLayout(new BoxLayout(innerPostPanel, BoxLayout.Y_AXIS));
    innerPostPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
    innerPostPanel.add(nameLabel);
    innerPostPanel.add(postLabel);
    innerPostPanel.add(photo);
    innerPostPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

    // Add margin between posts
    innerPostPanel.setBorder(BorderFactory.createCompoundBorder(
            innerPostPanel.getBorder(),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
    ));

    containerPanel.add(innerPostPanel);
}

    public static void UpdateStories(User user, JScrollPane storyPanel) {
        JPanel containerPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        containerPanel.setLayout(flowLayout);

        for (int i = 0; i < user.getFriendsStories().size(); i++) {
            JLabel storyLabel = new JLabel(user.getFriendsStories().get(i).getContent());
            JLabel nameLabel = new JLabel(user.getFriendsStories().get(i).getAuthorUserName());
            long time = user.getFriendsStories().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.MINUTES);
            if (time > 60 && time < 120) {
                time = user.getFriendsStories().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
                nameLabel.setText(nameLabel.getText() + " " + time + " hour ago");
            } else if (time > 120 && time < 1440) {
                time = user.getFriendsStories().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
                nameLabel.setText(nameLabel.getText() + " " + time + " hours ago");
            } else if (time > 1440 && time < 2880) {
                time = user.getFriendsStories().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                nameLabel.setText(nameLabel.getText() + " " + time + " day ago");
            } else if (time > 2880) {
                time = user.getFriendsStories().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                nameLabel.setText(nameLabel.getText() + " " + time + " days ago");
            } else {
                nameLabel.setText(nameLabel.getText() + " " + time + " minutes ago");
            }
            // Resize the image
            ImageIcon imageIcon = new ImageIcon(user.getFriendsStories().get(i).getImagePath());
            Image image = imageIcon.getImage();
            Image resizedImage = image.getScaledInstance(100, storyPanel.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

            JLabel photo = new JLabel(resizedImageIcon);
            JPanel innerStoryPanel = new JPanel();
            innerStoryPanel.setLayout(new BoxLayout(innerStoryPanel, BoxLayout.Y_AXIS));
            innerStoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
            innerStoryPanel.add(nameLabel);
            innerStoryPanel.add(photo);
            innerStoryPanel.add(storyLabel);
            innerStoryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

            // Add margin between stories
            innerStoryPanel.setBorder(BorderFactory.createCompoundBorder(
                    innerStoryPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));

            containerPanel.add(innerStoryPanel);
        }
        storyPanel.setViewportView(containerPanel);
    }

    public static void UpdateNotifications(User user, JScrollPane notificationPanel) {
    JPanel containerPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
    containerPanel.setLayout(boxLayout);

    for (int i = 0; i < user.getNotifications().size(); i++) {
        Notification notification = user.getNotifications().get(i);
        if (notification.getType().equals("Friend Request") && notification instanceof RequestNotification) {
            JLabel notificationLabel = new JLabel(notification.getMessage());
            JButton acceptButton = new JButton("Accept");
            JButton declineButton = new JButton("Decline");

            acceptButton.addActionListener(e -> {
                user.acceptFriendRequest(findUser(((RequestNotification) notification).getUserId()));
                NotificationManager.removeNotification(notification);
                UpdateNotifications(user, notificationPanel);
            });
            declineButton.addActionListener(e -> {
                user.declineFriendRequest(findUser(((RequestNotification) notification).getUserId()));
                NotificationManager.removeNotification(notification);
                UpdateNotifications(user, notificationPanel);
            });
            JPanel innerNotificationPanel = new JPanel();
            innerNotificationPanel.add(notificationLabel);
            innerNotificationPanel.add(acceptButton);
            innerNotificationPanel.add(declineButton);

            innerNotificationPanel.setBorder(BorderFactory.createCompoundBorder(
                    innerNotificationPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));

            containerPanel.add(innerNotificationPanel);

        } else if (notification.getType().equalsIgnoreCase("Default")) {
            JPanel innerNotificationPanel = getJPanel(user, notificationPanel, notification);

            containerPanel.add(innerNotificationPanel);
        }
        else if(notification.getType().equalsIgnoreCase("Group Activity") && notification instanceof GroupNotification){
            ProxyGroup group = new ProxyGroup(GroupManagement.getGroup(((GroupNotification) notification).getGroupId()), user);
            JLabel notificationLabel = new JLabel(notification.getMessage());
            JButton acceptButton = new JButton("Accept");
            JButton declineButton = new JButton("Decline");
            acceptButton.addActionListener(e -> {
                group.approveRequest(findUser(((GroupNotification)notification).getSenderId()));
                NotificationManager.removeNotification(notification);
                NotificationManager.removeAllRelatedNotifications((GroupNotification) notification);
                UpdateNotifications(user, notificationPanel);
            });
            declineButton.addActionListener(e -> {
                group.rejectRequest(findUser(((GroupNotification) notification).getSenderId()));
                NotificationManager.removeNotification(notification);
                UpdateNotifications(user, notificationPanel);
            });
            JPanel innerNotificationPanel = new JPanel();
            innerNotificationPanel.add(notificationLabel);
            innerNotificationPanel.add(acceptButton);
            innerNotificationPanel.add(declineButton);

            innerNotificationPanel.setBorder(BorderFactory.createCompoundBorder(
                    innerNotificationPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));

            containerPanel.add(innerNotificationPanel);
        }
        else if(notification.getType().equalsIgnoreCase("Post") && notification instanceof GroupPostNotifications){
            JLabel notificationLabel = new JLabel(notification.getMessage());
            JButton viewButton = new JButton("View");
            viewButton.addActionListener(e -> {
                // Open the group page
                GroupPage groupPage = new GroupPage(user, GroupManagement.getGroup(((GroupNotification) notification).getGroupId()));
                groupPage.setVisible(true);
                NotificationManager.removeNotification(notification);

            });
            JPanel innerNotificationPanel = new JPanel();
            innerNotificationPanel.add(notificationLabel);
            innerNotificationPanel.add(viewButton);

            innerNotificationPanel.setBorder(BorderFactory.createCompoundBorder(
                    innerNotificationPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));

            containerPanel.add(innerNotificationPanel);
        }
    }

    notificationPanel.setViewportView(containerPanel);
}

    private static JPanel getJPanel(User user, JScrollPane notificationPanel, Notification notification) {
        JLabel notificationLabel = new JLabel(notification.getMessage());
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            NotificationManager.removeNotification(notification);
            UpdateNotifications(user, notificationPanel);
        });
        JPanel innerNotificationPanel = new JPanel();
        innerNotificationPanel.add(notificationLabel);
        innerNotificationPanel.add(okButton);

        innerNotificationPanel.setBorder(BorderFactory.createCompoundBorder(
                innerNotificationPanel.getBorder(),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        return innerNotificationPanel;
    }

    public static void UpdateGroups(User user, JScrollPane groupsList) {

        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);

        for (int i = 0; i < user.getGroups().size(); i++) {
            System.out.println(user.getGroups().size());
            RealGroup group = user.getGroups().get(i);
            JLabel groupLabel = new JLabel(group.getName());
            JButton viewGroupButton = new JButton("View");
            viewGroupButton.addActionListener(e -> {
                // Open the group page
                GroupPage groupPage = new GroupPage(user, group);
                groupPage.setVisible(true);
            });
            JButton leaveGroupButton = new JButton("Leave");
            leaveGroupButton.addActionListener(e -> {
                user.leaveGroup(group);
                UpdateGroups(user, groupsList);
            });
            JPanel innerGroupPanel = new JPanel();
            innerGroupPanel.add(groupLabel);
            innerGroupPanel.add(viewGroupButton);
            innerGroupPanel.add(leaveGroupButton);

            innerGroupPanel.setBorder(BorderFactory.createCompoundBorder(
                    innerGroupPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));

            containerPanel.add(innerGroupPanel);
        }

        groupsList.setViewportView(containerPanel);

    }

    public static void UpdateGroupSuggestions(User user, JScrollPane groupSuggestionList) {
        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);

        for (int i = 0; i < user.getGroupSuggestions().size(); i++) {
            RealGroup group = user.getGroupSuggestions().get(i);
            ProxyGroup proxy = new ProxyGroup(group, user);
            JLabel groupLabel = new JLabel(group.getName());
            JButton joinGroupButton = new JButton("Join");
            joinGroupButton.addActionListener(e -> {
                proxy.addPendingRequest(user);
                UpdateGroupSuggestions(user, groupSuggestionList);
            });
            JPanel innerGroupPanel = new JPanel();
            innerGroupPanel.add(groupLabel);
            innerGroupPanel.add(joinGroupButton);

            innerGroupPanel.setBorder(BorderFactory.createCompoundBorder(
                    innerGroupPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));

            containerPanel.add(innerGroupPanel);
        }

        groupSuggestionList.setViewportView(containerPanel);
    }
}

