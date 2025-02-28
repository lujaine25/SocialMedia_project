package Backend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GroupMangementTest {


        public static void main(String[] args) {
            // Define the path for the groups file
            String GROUPS_FILE_PATH = "groups.json";

            // Step 1: Create a GroupManagement object using the builder
            RealGroup group = new RealGroup.Builder()
                    .setName("HIIII")
                    .setDescription("A group for testing save functionality.")
                    .setPhotoPath("")  // Set photoPath as an empty string (or provide a valid path if necessary)
                    .setGroupId(null)
                    .setContents(new ArrayList<Content>())  // Initialize contents as an empty list
                    .setPendingRequests(new ArrayList<User>()) // Initialize pendingRequests as an empty list
                    .setUserRoles(new HashMap<User, String>())// Initialize userRoles as an empty HashMap
                    .build();


            // Step 2: Add some sample contents
            group.addContent(new Content.Builder()
                    .setContent("Welcome to the Test Group!")
                    .setImagePath("images/welcome.jpg")
                    .setContentId("content1")
                    .setAuthorId("user123")
                    .setTime(LocalDateTime.now())
                    .setIsStory(false)
                    .setAuthorUserName("admin_user")
                    .build());

            group.addContent(new Content.Builder()
                    .setContent("Group Rules: Be respectful and follow the guidelines.")
                    .setImagePath("images/rules.jpg")
                    .setContentId("content2")
                    .setAuthorId("user456")
                    .setTime(LocalDateTime.now())
                    .setIsStory(false)
                    .setAuthorUserName("moderator")
                    .build());

            // Step 3: Add pending requests
            group.addPendingRequest(new User("John Doe", "user789", "john@example.com", "john_doe", "hashed123", "1990-05-21"));
            group.addPendingRequest(new User("Jane Smith", "user890", "jane@example.com", "jane_smith", "hashed456", "1992-11-13"));

            // Step 4: Add users with roles
            group.addUser(new User("Alice Admin", "user123", "alice@example.com", "admin_user", "hashedAdmin", "1988-01-01"), "creator");
            group.addUser(new User("Bob Member", "user456", "bob@example.com", "bob_member", "hashedMember", "1995-06-15"), "user");
            group.addUser(new User("Charlie Moderator", "user789", "charlie@example.com", "moderator", "hashedMod", "1985-07-15"), "admin");

            // Step 5: Create a loader instance and call the saveGroupToFile method

            GroupManagement.saveGroupToFile(group);
            group.addContent(new Content.Builder()
                    .setContent("modify shaghala?")
                    .setImagePath("")
                    .setContentId("content1")
                    .setAuthorId("user456")
                    .setTime(LocalDateTime.now())
                    .setIsStory(false)
                    .setAuthorUserName("moderator")
                    .build());
        }
    }


