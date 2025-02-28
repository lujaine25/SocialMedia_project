package Backend;

import javax.swing.*;
import java.util.ArrayList;

import static Backend.UserManager.allUsers;

public class Search {
    private static final ArrayList<User> searchResults = new ArrayList<>();

    public JPanel returnNewFriends(User user)
    {
        ArrayList<User> newFriends = new ArrayList<>();
        for (User search : searchResults) {
            if (!user.getFriendManagement().getFriends().contains(search) && !user.getFriendManagement().getSentRequests().contains(search) && !user.getFriendManagement().getReceivedRequests().contains(search) && !user.getFriendManagement().getBlockedUsers().contains(search)) {
                newFriends.add(search);
            }
        }
        // Create a panel to hold the search results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Display the search results
        for (User selected : newFriends) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));

            JLabel nameLabel = new JLabel(selected.getName() + " - " + selected.getUsername());
            JButton addButton = new JButton("Add Friend");
            JButton blockButton = new JButton("Block User");
            JButton viewButton = new JButton("View Profile");
            if(!user.getFriendManagement().isFriend(selected)){
                addButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.sendFriendRequest(selected);
                        JOptionPane.showMessageDialog(null, "Friend request sent to " + selected.getName());
                        addButton.setEnabled(false);
                        addButton.setText("Pending");
                    }
                });
                blockButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().blockUser(selected);
                        JOptionPane.showMessageDialog(null, "User blocked");
                        blockButton.setEnabled(false);
                    }
                });
                viewButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        // Open the profile of the selected user
                    }
                });
                userPanel.add(nameLabel);
                userPanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and button
                userPanel.add(addButton);
                userPanel.add(blockButton);
                userPanel.add(viewButton);

                resultsPanel.add(userPanel);
                resultsPanel.add(Box.createVerticalStrut(10)); // Add some space between user panels
            }
        }
        return resultsPanel;
    }

    public JPanel returnSearchedUserFriends(User user){
        ArrayList<User> friends = new ArrayList<>();
        for (User search : searchResults) {
            if (user.getFriendManagement().getFriends().contains(search)) {
                friends.add(search);
            }
        }
        // Create a panel to hold the search results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Display the search results
        for (User selected : friends) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));


            JLabel nameLabel = new JLabel(selected.getName() + " - " + selected.getUsername());
            JButton addButton = new JButton("Remove Friend");
            JButton blockButton = new JButton("Block User");
            JButton viewButton = new JButton("View Profile");
            if(!user.getFriendManagement().isFriend(selected)){
                addButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().removeFriend(selected);
                        JOptionPane.showMessageDialog(null, "Friend request sent to " + selected.getName());
                        addButton.setEnabled(false);
                        addButton.setText("Pending");
                    }
                });
                blockButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().blockUser(selected);
                        JOptionPane.showMessageDialog(null, "User blocked");
                        blockButton.setEnabled(false);
                    }
                });
                viewButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        // Open the profile of the selected user
                    }
                });
                userPanel.add(nameLabel);
                userPanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and button
                userPanel.add(addButton);
                userPanel.add(blockButton);
                userPanel.add(viewButton);

                resultsPanel.add(userPanel);
                resultsPanel.add(Box.createVerticalStrut(10)); // Add some space between user panels
            }

        }
        return resultsPanel;
    }

    public JPanel returnUserSentRequests(User user){
        ArrayList<User> sentRequests = new ArrayList<>();
        for (User search : searchResults) {
            if (user.getFriendManagement().getSentRequests().contains(search)) {
                sentRequests.add(search);
            }
        }
        // Create a panel to hold the search results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Display the search results
        for (User selected : sentRequests) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));


            JLabel nameLabel = new JLabel(selected.getName() + " - " + selected.getUsername());
            JButton addButton = new JButton("Cancel Request");
            JButton blockButton = new JButton("Block User");
            JButton viewButton = new JButton("View Profile");
            if(!user.getFriendManagement().isFriend(selected)){
                addButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().cancelFriendRequest(selected);
                        JOptionPane.showMessageDialog(null, "Friend request cancelled");
                        addButton.setEnabled(false);
                    }
                });
                blockButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().blockUser(selected);
                        JOptionPane.showMessageDialog(null, "User blocked");
                        blockButton.setEnabled(false);
                    }
                });
                viewButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        // Open the profile of the selected user
                    }
                });
                userPanel.add(nameLabel);
                userPanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and button
                userPanel.add(addButton);
                userPanel.add(blockButton);
                userPanel.add(viewButton);

                resultsPanel.add(userPanel);
                resultsPanel.add(Box.createVerticalStrut(10)); // Add some space between user panels
            }
    }
        return resultsPanel;
    }

    public JPanel returnUserReceivedRequests(User user){
        ArrayList<User> receivedRequests = new ArrayList<>();
        for (User search : searchResults) {
            if (user.getFriendManagement().getReceivedRequests().contains(search)) {
                receivedRequests.add(search);
            }
        }

        // Create a panel to hold the search results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Display the search results

        for (User selected : receivedRequests) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));

            JLabel nameLabel = new JLabel(selected.getName() + " - " + selected.getUsername());
            JButton addButton = new JButton("Accept Request");
            JButton declineButton = new JButton("Decline Request");
            JButton blockButton = new JButton("Block User");
            JButton viewButton = new JButton("View Profile");
            if(!user.getFriendManagement().isFriend(selected)){
                addButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.acceptFriendRequest(selected);
                        JOptionPane.showMessageDialog(null, "Friend request accepted");
                        addButton.setEnabled(false);
                        declineButton.setEnabled(false);
                    }
                });
                declineButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().declineFriendRequest(selected);
                        JOptionPane.showMessageDialog(null, "Friend request declined");
                        addButton.setEnabled(false);
                        declineButton.setEnabled(false);
                    }
                });
                blockButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().blockUser(selected);
                        JOptionPane.showMessageDialog(null, "User blocked");
                        blockButton.setEnabled(false);
                    }
                });
                viewButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        // Open the profile of the selected user
                    }
                });
                userPanel.add(nameLabel);
                userPanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and button
                userPanel.add(addButton);
                userPanel.add(declineButton);
                userPanel.add(blockButton);
                userPanel.add(viewButton);

                resultsPanel.add(userPanel);
                resultsPanel.add(Box.createVerticalStrut(10)); // Add some space between user panels
            }
        }
        return resultsPanel;
    }

    public JPanel returnUserBlockedUsers(User user){
        ArrayList<User> blockedUsers = new ArrayList<>();
        for (User search : searchResults) {
            if (user.getFriendManagement().getBlockedUsers().contains(search)) {
                blockedUsers.add(search);
            }
        }
        // Create a panel to hold the search results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Display the search results

        for (User selected : blockedUsers) {
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));

            JLabel nameLabel = new JLabel(selected.getName() + " - " + selected.getUsername());
            JButton addButton = new JButton("Unblock User");
            JButton viewButton = new JButton("View Profile");
            if(!user.getFriendManagement().isFriend(selected)){
                addButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        user.getFriendManagement().unblockUser(selected);
                        JOptionPane.showMessageDialog(null, "User unblocked");
                        addButton.setEnabled(false);
                    }
                });
                viewButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        // Open the profile of the selected user
                    }
                });
                userPanel.add(nameLabel);
                userPanel.add(Box.createHorizontalStrut(10)); // Add some space between the label and button
                userPanel.add(addButton);
                userPanel.add(viewButton);

                resultsPanel.add(userPanel);
                resultsPanel.add(Box.createVerticalStrut(10)); // Add some space between user panels
            }
        }
        return resultsPanel;
    }

    public static ArrayList<User> searchByName(String name, User user) {
        for (User search : allUsers) {
            if ((search.getName().toLowerCase().contains(name.toLowerCase()) || search.getUsername().toLowerCase().contains(name.toLowerCase())) && !user.equals(search)) {
                searchResults.add(search);
            }
        }
        return searchResults;
    }


}
