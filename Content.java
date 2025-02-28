/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import java.time.LocalDateTime;


/**
 *
 * @author DELL
 */
public class Content {

    private String content;
    private String imagePath;
    private final String contentId;
    private final String authorId;
    private final LocalDateTime time;
    private final boolean isStory;
    private final String authorUserName;

    private Content(Builder builder) {
        this.content = builder.content;
        this.imagePath = builder.imagePath;
        this.contentId = builder.contentId;
        this.authorId = builder.authorId;
        this.time = builder.time;
        this.isStory = builder.isStory;
        this.authorUserName = builder.authorUserName;
    }


    public String getAuthorUserName() {
        return authorUserName;
    }

    public String getContent() {
        return content;
    }
    
    public void setImagePath(String path){
        this.imagePath=path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getContentId() {
        return contentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public boolean getIsStory() {
        return isStory;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString(){
        return "Content: " + content + " ImagePath: " + imagePath + " ContentId: " + contentId + " AuthorId: " + authorId + " Time: " + time + " IsStory: " + isStory;
    }

    public static class Builder {
        private String content;
        private String imagePath;
        private String contentId;
        private String authorId;
        private LocalDateTime time;
        private boolean isStory;
        private String authorUserName;

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder setContentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder setAuthorId(String authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder setTime(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public Builder setIsStory(boolean isStory) {
            this.isStory = isStory;
            return this;
        }

        public Builder setAuthorUserName(String authorUserName) {
            this.authorUserName = authorUserName;
            return this;
        }

        public Content build() {
            return new Content(this);
        }
    }
    
}
