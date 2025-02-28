package Frontend;

import Backend.ContentManager;
import Backend.User;
import Backend.UserManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static Backend.UserManager.allUsers;


public class Profile_Updates {
    private static final UserManager userManager = UserManager.getInstance();
    private static final ContentManager contentManager = Backend.ContentManager.getInstance();

    public static void RefreshProfile() {
        contentManager.readContent();
        userManager.loadAllUsers();
        userManager.loadAllFriends();
    }


    public static void UpdateProfilePosts(User user, JScrollPane jScrollPane1) {
        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);

        for (int i = 0; i < user.getPosts().size(); i++) {
            if (!user.getPosts().get(i).getIsStory()) {
                JLabel postLabel = new JLabel(user.getPosts().get(i).getContent());
                JLabel nameLabel = new JLabel(user.getPosts().get(i).getAuthorUserName());
                long time = user.getPosts().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.MINUTES);
                if (time > 60 && time < 120) {
                    time = user.getPosts().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
                    nameLabel.setText(nameLabel.getText() + " " + time + " hour ago");
                } else if (time > 120 && time < 1440) {
                    time = user.getPosts().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
                    nameLabel.setText(nameLabel.getText() + " " + time + " hours ago");
                } else if (time > 1440 && time < 2880) {
                    time = user.getPosts().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                    nameLabel.setText(nameLabel.getText() + " " + time + " day ago");
                } else if (time > 2880) {
                    time = user.getPosts().get(i).getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                    nameLabel.setText(nameLabel.getText() + " " + time + " days ago");
                } else {
                    nameLabel.setText(nameLabel.getText() + " " + time + " minutes ago");
                }
                // Resize the image
                ImageIcon imageIcon = new ImageIcon(user.getPosts().get(i).getImagePath());
                Image image = imageIcon.getImage();
                Image resizedImage = image.getScaledInstance(jScrollPane1.getWidth() - 10, 300, Image.SCALE_SMOOTH);
                ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

                JLabel photo = new JLabel(resizedImageIcon);
                JPanel postPanel = new JPanel();
                postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
                postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// Add padding
                postPanel.add(nameLabel);
                postPanel.add(photo);
                postPanel.add(postLabel);
                postPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

                // Add margin between posts
                postPanel.setBorder(BorderFactory.createCompoundBorder(
                        postPanel.getBorder(),
                        BorderFactory.createEmptyBorder(10, 0, 10, 0)
                ));

                containerPanel.add(postPanel);
            }
            jScrollPane1.setViewportView(containerPanel);
        }
    }
    public static void UpdateProfile(User user, JLabel userName, JLabel bio, JLabel profileImage, JLabel coverImage, JScrollPane friendsList) {
        userName.setText(user.getName());
        bio.setText(user.getUserProfile().getBio());

        ImageIcon profile=new ImageIcon(user.getUserProfile().getProfilePhotoPath());
        Image image = profile.getImage();
        Image newimg = image.getScaledInstance(profileImage.getWidth(), profileImage.getHeight(),  java.awt.Image.SCALE_SMOOTH);
        profile = new ImageIcon(newimg);
        profileImage.setIcon(profile);

        ImageIcon cover=new ImageIcon(user.getUserProfile().getCoverPhotoPath());
        Image image2 = cover.getImage();
        Image newimg2 = image2.getScaledInstance(coverImage.getWidth(), coverImage.getHeight(),  java.awt.Image.SCALE_SMOOTH);
        cover = new ImageIcon(newimg2);
        coverImage.setIcon(cover);

        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);
        for (int i = 0; i < user.getFriendManagement().getFriends().size(); i++) {
            System.out.println(user.getFriendManagement().getFriends().get(i).getName() + " " + user.getFriendManagement().getFriends().get(i).getStatus());
            JLabel friendLabel = new JLabel(user.getFriendManagement().getFriends().get(i).getName());
            JLabel statusLabel = new JLabel(user.getFriendManagement().getFriends().get(i).getStatus());
            JPanel friendPanel = new JPanel();
            friendPanel.add(friendLabel);
            friendPanel.add(statusLabel);
            containerPanel.add(friendPanel);
        }
        friendsList.setViewportView(containerPanel);


    }
}
