package Backend;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupManagement {
    public static ArrayList<RealGroup> allgroups = new ArrayList<>();
    private static final String GROUPS_FILE_PATH = "databases/groups.json";
    private static final DatabaseManager databaseManager = Backend.DatabaseManager.getInstance();
    public static JSONArray groupsArray = databaseManager.readJSONFile(GROUPS_FILE_PATH);


    private static GroupManagement instance;

    private GroupManagement() {
        loadGroups();
    }

    public static GroupManagement getInstance() {
        if (instance == null) {
            instance = new GroupManagement();
        }
        return instance;
    }

    public static JSONObject toJSONObject(RealGroup group) {
        JSONArray usersArray = new JSONArray();
        JSONArray adminsArray = new JSONArray();
        JSONArray pendingRequestsArray = new JSONArray();
        String creator = "";

        for (User user : group.getUserRoles().keySet()) {
            if (group.getUserRoles().get(user).equals("user")) {
                usersArray.put(user.getUserId());
            } else if (group.getUserRoles().get(user).equals("admin")) {
                adminsArray.put(user.getUserId());
            } else if (group.getUserRoles().get(user).equals("creator")) {
                creator = user.getUserId();
            }
        }
        for (User pendingUser : group.getPendingRequests()) {
            pendingRequestsArray.put(pendingUser.getUserId());
        }

        JSONObject groupJson = new JSONObject();
        groupJson.put("name", group.getName());
        groupJson.put("description", group.getDescription());
        groupJson.put("contents", group.getContents());
        groupJson.put("pendingRequests", pendingRequestsArray);
        groupJson.put("groupId", group.getGroupId());
        groupJson.put("photoPath", group.getPhotoPath());
        groupJson.put("users", usersArray);
        groupJson.put("admins", adminsArray);
        groupJson.put("creator", creator);

        return groupJson;
    }

    public static synchronized void saveGroupToFile(RealGroup group) {
        // Convert the Group object to a JSON object
        JSONObject groupJson = toJSONObject(group);

        // Flag to check if the group is found
        boolean groupFound = false;

        // Iterate over the groups array
        for (int i = 0; i < groupsArray.length(); i++) {
            JSONObject currentGroup = groupsArray.getJSONObject(i);

            // Check if the groupId matches
            if (currentGroup.getString("groupId").equals(group.getGroupId())) {
                // Replace the existing group
                groupsArray.put(i, groupJson);
                groupFound = true;
                break; // Exit the loop as we found the group
            }
        }

        // If the group was not found, optionally add it to the array
        if (!groupFound) {
            groupsArray.put(groupJson);
            JOptionPane.showMessageDialog(null, "Group saved successfully!");
        }
        allgroups.add(group);
        // Write the updated array back to the file
        databaseManager.writeJSONFile(GROUPS_FILE_PATH, groupsArray);
    }

    public static synchronized ArrayList<RealGroup> searchForGroupsByName(String name) {
        ArrayList<RealGroup> searchGroups = new ArrayList<>();
        for (RealGroup search: allgroups) {
            if (search.getName().toLowerCase().contains(name.toLowerCase())) {
                searchGroups.add(search);
            }
        }
        return searchGroups;
    }

    public static synchronized ArrayList<RealGroup> getGroups(User user) {
        ArrayList<RealGroup> userGroups = new ArrayList<>();
        System.out.println(allgroups.size() + " groups");
        for (RealGroup group : allgroups) {
            if (group.getUserRoles().containsKey(user)) {
                userGroups.add(group);
            }
        }
        return userGroups;
    }

    public static synchronized RealGroup getGroup(String groupId) {
        for (RealGroup group : allgroups) {
            if (group.getGroupId().equals(groupId)) {
                return group;
            }
        }
        return null;
    }

    public static synchronized ArrayList<RealGroup> getGroupSuggestions(User user) {
        ArrayList<RealGroup> suggestions = new ArrayList<>();
        for (RealGroup group : allgroups) {
            if (!group.getUserRoles().containsKey(user) && !group.getPendingRequests().contains(user)) {
                suggestions.add(group);
            }
        }
        return suggestions;
    }

    public synchronized void loadGroups() {
        allgroups.clear(); // Clear the current list of groups

        File file = new File(GROUPS_FILE_PATH);
        JSONArray groupArray = databaseManager.readJSONFile(GROUPS_FILE_PATH);
        if (file.exists() && groupArray != null) {
            for (int i = 0; i < groupArray.length(); i++) {
                JSONObject groupJson = groupArray.getJSONObject(i);

                // Extract group details

                String groupId = groupJson.getString("groupId");
                String name = groupJson.getString("name");
                String description = groupJson.getString("description");
                String photoPath = groupJson.has("photoPath") ? groupJson.getString("photoPath") : "";

                //extract pending requests
                JSONArray pendingRequestsArray = groupJson.getJSONArray("pendingRequests");

                //load the pending requests
                {
                ArrayList<User> pendingRequests = new ArrayList<>();
                for (int j = 0; j < pendingRequestsArray.length(); j++) {
                    String userId = pendingRequestsArray.getString(j);
                    User user = UserManager.findUser(userId); // Convert userId to a User object
                    if (user != null) { // Ensure the user exists
                        pendingRequests.add(user);
                    }
                }
                    HashMap<User, String> userRoles = new HashMap<>();

                    //load creator
                    {
                        String creator = groupJson.getString("creator");
                        User user = UserManager.findUser(creator);
                        if (user != null) {
                            userRoles.put(user, "creator");
                        }
                    }

                    // Load users
                    {
                        JSONArray usersArray = groupJson.getJSONArray("users");
                        for (int j = 0; j < usersArray.length(); j++) {

                            User user = UserManager.findUser(usersArray.getString(j));
                            if (user != null) {
                                userRoles.put(user, "user");
                            }

                        }
                    }
                    {
                        // Load admins
                        JSONArray adminsArray = groupJson.getJSONArray("admins");
                        for (int j = 0; j < adminsArray.length(); j++) {
                            User user = UserManager.findUser(adminsArray.getString(j));
                            if (user != null) {
                                userRoles.put(user, "admin");
                            }

                        }
                    }


                    // Extract group contents
                    JSONArray contentsArray = groupJson.getJSONArray("contents");
                    ArrayList<Content> contents = new ArrayList<>();

                    for (int j = 0; j < contentsArray.length(); j++) {
                        JSONObject contentJson = contentsArray.getJSONObject(j);
                        String contentId = contentJson.getString("contentId");
                        String authorId = contentJson.getString("authorId");
                        String authorUserName = contentJson.getString("authorUserName");
                        String contentText = contentJson.getString("content");
                        String imagePath = contentJson.has("imagePath") ? contentJson.getString("imagePath") : "";                        boolean isStory = contentJson.getBoolean("isStory");
                        LocalDateTime time=LocalDateTime.parse(contentJson.getString("time"));

                        Content content = new Content
                                .Builder()
                                .setContent(contentText)
                                .setImagePath(imagePath)
                                .setContentId(contentId)
                                .setAuthorId(authorId)
                                .setTime(time)
                                .setIsStory(isStory)
                                .setAuthorUserName(authorUserName)
                                .build();

//                        group.addContent(content);
                        contents.add(content);
                    }

                    RealGroup group = new RealGroup.Builder()
                                .setGroupId(groupId)
                                .setName(name)
                                .setDescription(description)
                                .setPhotoPath(photoPath)
                                .setUserRoles(userRoles)
                                .setContents(contents)
                                .setPendingRequests(pendingRequests)
                                .build();


                    allgroups.add(group);
                }
            }

        }
    }
}