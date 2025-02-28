package Backend;

public class UserProfile {

    private String profilePhotoPath;
    private String coverPhotoPath;
    private String bio;

    private UserProfile(Builder builder) {
        this.profilePhotoPath = builder.profilePhotoPath;
        this.coverPhotoPath = builder.coverPhotoPath;
        this.bio = builder.bio;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getCoverPhotoPath() {
        return coverPhotoPath;
    }

    public void setCoverPhotoPath(String coverPhotoPath) {
        this.coverPhotoPath = coverPhotoPath;
    }

    public static class Builder {
        private String profilePhotoPath = "";
        private String coverPhotoPath = "";
        private String bio = "";

        public Builder setProfilePhotoPath(String profilePhotoPath) {
            this.profilePhotoPath = profilePhotoPath;
            return this;
        }

        public Builder setCoverPhotoPath(String coverPhotoPath) {
            this.coverPhotoPath = coverPhotoPath;
            return this;
        }

        public Builder setBio(String bio) {
            this.bio = bio;
            return this;
        }

        public UserProfile build() {
            return new UserProfile(this);
        }
    }
}
