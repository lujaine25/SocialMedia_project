package Frontend;

import Backend.*;
import Backend.Notifications.GroupNotification;
import Backend.Notifications.NotificationManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static Backend.UserManager.findUser;

public class Group_Updates {



    public static void updateGroupPageDetails(RealGroup group,User mainUser ,JLabel groupPhoto, JLabel groupName, JLabel groupDescription, JScrollPane groupMembersScroller) {
        ProxyGroup groupProxy = new ProxyGroup(group,mainUser);
        groupName.setText(group.getName());
        groupDescription.setText(group.getDescription());
        ImageIcon icon = new ImageIcon(group.getPhotoPath());
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(groupPhoto.getWidth(), groupPhoto.getHeight(), java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        groupPhoto.setIcon(icon);

        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);
        for (User user : group.getUserRoles().keySet()) {
            if(!user.equals(mainUser)){

                System.out.println(user.getName());
                JLabel userLabel = new JLabel(user.getUsername());
                JButton promoteButton = new JButton("Promote");
                promoteButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                    if(groupProxy.isAdminOrCreator(user))
                    {
                        JOptionPane.showMessageDialog(null, "User is already an admin");
                    }
                    else {
                        groupProxy.promoteUser(user);
                        if (groupProxy.isCreator(mainUser)) {
                            JOptionPane.showMessageDialog(null, "User promoted successfully");
                            promoteButton.setEnabled(false);
                            promoteButton.setText("Promoted");
                        }
                    }});
                JButton demoteButton = new JButton("Demote");
                demoteButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                    if(groupProxy.isCreator(user)){
                        JOptionPane.showMessageDialog(null, "You can't demote the creator");
                    }
                    else if(!groupProxy.isAdminOrCreator(user)){
                        JOptionPane.showMessageDialog(null, "User is already a member");
                    }
                    else{
                        groupProxy.demoteUser(user);
                        if(groupProxy.isCreator(mainUser) ){
                            JOptionPane.showMessageDialog(null, "User demoted successfully");
                            demoteButton.setEnabled(false);
                            demoteButton.setText("Demoted");}
                    }});
                JButton removeButton = new JButton("Remove");
                removeButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                if(groupProxy.isAdminOrCreator(mainUser) && !groupProxy.isCreator(user)){
                    groupProxy.removeUser(user);
                    JOptionPane.showMessageDialog(null, user.getName()+" removed successfully");
                    removeButton.setEnabled(false);
                    removeButton.setText("Removed");}
                });
                JPanel userPanel = new JPanel();
                userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
                userPanel.add(userLabel);
                userPanel.add(Box.createHorizontalStrut(10));
                userPanel.add(promoteButton);
                userPanel.add(demoteButton);
                userPanel.add(removeButton);
                containerPanel.add(userPanel);
                containerPanel.add(Box.createVerticalStrut(10));
            }
        }
        groupMembersScroller.setViewportView(containerPanel);
    }

    public static void updateGroupPosts(RealGroup group,User mainUser, JScrollPane postsScroller){
        ProxyGroup groupProxy = new ProxyGroup(group, mainUser);
        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);
        for(Content content : group.getContents()){
            JLabel postLabel = new JLabel(content.getContent());
            JLabel userNameLabel = new JLabel(content.getAuthorUserName());
            Long time = content.getTime().until(LocalDateTime.now(), ChronoUnit.MINUTES);
            if (time > 60 && time < 120) {
                time = content.getTime().until(LocalDateTime.now(), ChronoUnit.HOURS);
                userNameLabel.setText(userNameLabel.getText() + " " + time + " hour ago");
            } else if (time > 120 && time < 1440) {
                time = content.getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                userNameLabel.setText(userNameLabel.getText() + " " + time + " hours ago");
            } else if (time > 1440 && time < 2880) {
                time = content.getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                userNameLabel.setText(userNameLabel.getText() + " " + time + " day ago");
            } else if (time > 2880) {
                time = content.getTime().until(LocalDateTime.now(), ChronoUnit.DAYS);
                userNameLabel.setText(userNameLabel.getText() + " " + time + " days ago");
            } else {
                userNameLabel.setText(userNameLabel.getText() + " " + time + " minutes ago");
            }
            ImageIcon icon = new ImageIcon(content.getImagePath());
            Image image = icon.getImage();
            Image newimg = image.getScaledInstance(postsScroller.getWidth() - 10, 300, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newimg);
            JLabel imageLabel = new JLabel(newIcon);
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                groupProxy.removeContent(content);
                if(groupProxy.isAdminOrCreator(mainUser)){
                JOptionPane.showMessageDialog(null, "Post deleted successfully");
                deleteButton.setEnabled(false);
                deleteButton.setText("Deleted");}
            });
            JButton editButton = new JButton("Edit");
            editButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                if(groupProxy.isAdminOrCreator(mainUser)){
                    new EditPostForGroup(content,group).setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "You do not have permission to edit this post");
                }
            });
            JPanel postPanel = new JPanel();
            postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
            postPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            postPanel.add(userNameLabel);
            postPanel.add(postLabel);
            postPanel.add(imageLabel);
            postPanel.add(deleteButton);
            postPanel.add(editButton);
            postPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            postPanel.setBorder(BorderFactory.createCompoundBorder(
                    postPanel.getBorder(),
                    BorderFactory.createEmptyBorder(10, 0, 10, 0)
            ));
            containerPanel.add(postPanel);
        }
        postsScroller.setViewportView(containerPanel);
    }

    public static void updateGroupMembersRequests(RealGroup group, User user, JScrollPane membersRequests) {
        ProxyGroup groupProxy = new ProxyGroup(group, user);
        JPanel containerPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(containerPanel, BoxLayout.Y_AXIS);
        containerPanel.setLayout(boxLayout);
        for(User member : group.getPendingRequests()){
            JLabel userLabel = new JLabel(member.getName());
            JButton acceptButton = new JButton("Accept");
            JButton rejectButton = new JButton("Reject");
            acceptButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                groupProxy.approveRequest(member);
                acceptButton.setEnabled(false);
                updateGroupMembersRequests(group, user, membersRequests);
            });
            rejectButton.addActionListener((java.awt.event.ActionEvent evt) -> {
                groupProxy.rejectRequest(member);
                rejectButton.setEnabled(false);
                updateGroupMembersRequests(group, user, membersRequests);
            });
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
            userPanel.add(userLabel);
            userPanel.add(Box.createHorizontalStrut(10));
            userPanel.add(acceptButton);
            userPanel.add(rejectButton);
            containerPanel.add(userPanel);
            containerPanel.add(Box.createVerticalStrut(10));
        }
        membersRequests.setViewportView(containerPanel);
    }
}