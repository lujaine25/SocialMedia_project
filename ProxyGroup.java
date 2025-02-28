package Backend;

import javax.swing.*;

public class ProxyGroup implements Group {

    public RealGroup realGroup;
    public User user;

    public ProxyGroup(RealGroup realGroup, User user) {
        this.realGroup = realGroup;
        this.user = user;
    }

    // Helper methods to check roles in the HashMap based on a given user
    public boolean isAdminOrCreator(User user) {
        // Check if the specified user is either an Admin or Creator
        return realGroup.getUserRoles().get(user).equalsIgnoreCase("admin") || realGroup.getUserRoles().get(user).equalsIgnoreCase("creator");
    }

    public boolean isAdmin(User user) {
        // Check if the specified user is an Admin
        return realGroup.getUserRoles().get(user).equalsIgnoreCase("admin");
    }

    public boolean isCreator(User user) {
        // Check if the specified user is the Creator
        return realGroup.getUserRoles().get(user).equalsIgnoreCase("creator");
    }

    @Override
    //anyone can add content
    public void addContent(Content content) {
        realGroup.addContent(content);
    }

    @Override
    //only admin or creator can remove content
    public void removeContent(Content content) {
        if (isAdminOrCreator(user)) {
            realGroup.removeContent(content);
        } else JOptionPane.showMessageDialog(null, "You do not have permission to remove content");
    }

    @Override
    //anyone can add pending request
    public void addPendingRequest(User user1) {
        realGroup.addPendingRequest(user1);
    }

    @Override
    //only admin or creator can remove pending request
    public void removePendingRequest(User user1) {
        if (isAdminOrCreator(user)) {
            realGroup.removePendingRequest(user1);
        } else JOptionPane.showMessageDialog(null, "You do not have permission to remove pending request");
    }

    @Override
    //only admin or creator can add user
    public void addUser(User user1, String role) {
        if (isAdminOrCreator(user)) {
            realGroup.addUser(user1, role);
        } else JOptionPane.showMessageDialog(null, "You do not have permission to add user");
    }

    @Override
    // creator can remove anyone
    //admin can remove only user
    public void removeUser(User user1) {
        if (isCreator(user)) {
            realGroup.removeUser(user1);
        } else if (isAdmin(user) && !isAdminOrCreator(user1)) {
            realGroup.removeUser(user1);
        } else JOptionPane.showMessageDialog(null, "You do not have permission to remove user");
    }


    //only creator can delete group
    @Override
    public void deleteGroup() {
        if (isCreator(user)) {
            realGroup.deleteGroup();
        } else {
            JOptionPane.showMessageDialog(null, "You do not have permission to delete group", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    //only creator can promote user
    public void promoteUser(User user1) {
        if (isCreator(user)) {
            realGroup.promoteUser(user1);
        }else{
            JOptionPane.showMessageDialog(null,"You do not have permission to promote user","Error",JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    //only creator can demote user
    public void demoteUser(User user1) {
        if (isCreator(user)) {
            realGroup.demoteUser(user1);
        } else {
            JOptionPane.showMessageDialog(null, "You do not have permission to demote user", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    //only admin or creator can approve request
    public void approveRequest(User user1) {
        if (isAdminOrCreator(user)) {
            realGroup.approveRequest(user1);
        }else{
            JOptionPane.showMessageDialog(null,"You do not have permission to approve request","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    //only admin or creator can reject request
    public void rejectRequest(User user1) {
        if (isAdminOrCreator(user)) {
            realGroup.rejectRequest(user1);
        }else{
            JOptionPane.showMessageDialog(null,"You do not have permission to reject request","Error",JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    //anyone can leave group
    public void leaveGroup(User user) {
        realGroup.leaveGroup(user);
    }
}
