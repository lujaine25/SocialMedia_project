package Backend;

public interface Group {
    public void addContent(Content content);
    public void removeContent(Content content);
    public void addPendingRequest(User user);
    public void removePendingRequest(User user);
    public void addUser(User user, String role);
    public void removeUser(User user);
    public void deleteGroup();
    public void promoteUser(User user);
    public void demoteUser(User user);
    public void approveRequest(User user);
    public void rejectRequest(User user);
    public void leaveGroup(User user);
}
