/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static Backend.UserManager.findUser;


/**
 *
 * @author DELL
 */
public class ContentManager {

    public static ArrayList<Content> allContents = new ArrayList<>();
    private static final String CONTENTS_FILE = "databases/content.json";
    private static final DatabaseManager databaseManager = Backend.DatabaseManager.getInstance();
    public static JSONArray contentsArray = databaseManager.readJSONFile(CONTENTS_FILE);

    // Singleton instance
    private static ContentManager instance;

    // Private constructor to prevent instantiation
    private ContentManager() {
        readContent();
    }

    // Public method to provide access to the instance
    public static ContentManager getInstance() {
        if (instance == null) {
            instance = new ContentManager();
        }
        return instance;
    }

    public void addContent(Content content) {
        allContents.add(content);
        writeContent(content);
    }

    public void removeContent(Content content) {
        allContents.remove(content);
        for(int i = 0; i < contentsArray.length(); i++){
            JSONObject contents = contentsArray.getJSONObject(i);
            if(contents.getString("contentId").equals(content.getContentId())){
                contentsArray.remove(i);
                break;
            }
        }
        databaseManager.writeJSONFile(CONTENTS_FILE, contentsArray);
    }

    public void removeStory() {
        Iterator<Content> iterator = allContents.iterator();
        while (iterator.hasNext()) {
            Content content = iterator.next();
            if (content.getIsStory() && content.getTime().isBefore(LocalDateTime.now().minusDays(1))) {
                iterator.remove();
                removeContent(content);
            }
        }
    }

    public static void writeContent(Content content) {

        JSONObject newContentObject = new JSONObject();
        JSONArray contentsArray = databaseManager.readJSONFile(CONTENTS_FILE);
            newContentObject.put("content", content.getContent());
            if(content.getImagePath() == null){
                newContentObject.put("imagePath", "null");
            }
            else{
            newContentObject.put("imagePath", content.getImagePath());}
            newContentObject.put("contentId", content.getContentId());
            newContentObject.put("authorId", content.getAuthorId());
            newContentObject.put("time", content.getTime());
            newContentObject.put("isStory", content.getIsStory());
            contentsArray.put(newContentObject);


        databaseManager.writeJSONFile(CONTENTS_FILE, contentsArray);

    }

    public void readContent(){
      allContents.clear();
        try{
            String json=new String(Files.readAllBytes(Paths.get(CONTENTS_FILE)));
            JSONArray contentsArray = new JSONArray(json);
            DateTimeFormatter formatter=DateTimeFormatter.ISO_DATE;
            for(int i=0;i<contentsArray.length();i++){
                JSONObject content=contentsArray.getJSONObject(i);
                String contentId=content.getString("contentId");
                String authorId=content.getString("authorId");
                String contentText=content.getString("content");
                String imagePath=content.getString("imagePath");
                LocalDateTime time=LocalDateTime.parse(content.getString("time"));
                boolean isStory=content.getBoolean("isStory");
                Content newContent = new Content.Builder()
                        .setContent(contentText)
                        .setImagePath(imagePath)
                        .setContentId(contentId)
                        .setAuthorId(authorId)
                        .setTime(time)
                        .setIsStory(isStory)
                        .setAuthorUserName(Objects.requireNonNull(findUser(authorId)).getUsername())
                        .build();
                allContents.add(newContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Content> UserContent(String userId){
        ArrayList<Content> userContents=new ArrayList<>();
        for(Content content:allContents){
            if(content.getAuthorId().equals(userId)){
                userContents.add(content);
            }
        }
        return userContents;
    }

    public static ArrayList<Content> getUserStories(User user){
        ArrayList<Content> stories=new ArrayList<>();
        ArrayList<Content> allContents = UserContent(user.getUserId());
        for(Content content:allContents){
            if(content.getIsStory() && content.getTime().isAfter(LocalDateTime.now().minusDays(1))){
                stories.add(content);
            }
        }
        return stories;
    }

    public static ArrayList<Content> getFriendsPosts(User user) {
        ArrayList<Content> mainFriendsPosts = new ArrayList<>();
        ArrayList<User> friends = user.getFriendManagement().getFriends();

        for (User friend : friends) {
            ArrayList<Content> friendPosts = UserContent(friend.getUserId());
            for(Content post: friendPosts){
                if(!post.getIsStory()){
                    mainFriendsPosts.add(post);
                }
            }
        }
        mainFriendsPosts.sort((c1, c2) -> c2.getTime().compareTo(c1.getTime()));
        return mainFriendsPosts;
    }

    public static ArrayList<Content> getFriendsStories(User user) {
        ArrayList<Content> mainFriendsStories = new ArrayList<>();
        ArrayList<User> friends = user.getFriendManagement().getFriends();

        for (User friend : friends) {
            ArrayList<Content> friendStories = getUserStories(friend);
            mainFriendsStories.addAll(friendStories);
        }
        mainFriendsStories.sort((c1, c2) -> c2.getTime().compareTo(c1.getTime()));
        return mainFriendsStories;
    }

    public static Content getContent(String contentId){
        for(Content content:allContents){
            if(content.getContentId().equals(contentId)){
                return content;
            }
        }
        return null;
    }
}
