/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import javax.swing.*;
import java.io.IOException;

import static Backend.UserManager.findUser;

/**
 * @author DELL
 */
public class UpdateProfile {

    public void updateProfilePhoto(String profilePhotoPath, String userId) throws IOException {
        User user = findUser(userId);
        if (user != null) {
            user.getUserProfile().setProfilePhotoPath(profilePhotoPath);
            UserManager.saveUserToDatabase(user);
            JOptionPane.showMessageDialog(null, "Profile photo updated successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void updateCoverPhoto(String coverPhotoPath, String userId) throws IOException {

        User user = findUser(userId);
        if (user != null) {
            user.getUserProfile().setCoverPhotoPath(coverPhotoPath);
            UserManager.saveUserToDatabase(user);
            JOptionPane.showMessageDialog(null, "Cover photo updated successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    public void updateBio(String bio, String userId) throws IOException {

        User user = findUser(userId);
        if (user != null) {
            user.getUserProfile().setBio(bio);
            UserManager.saveUserToDatabase(user);
            JOptionPane.showMessageDialog(null, "Bio updated successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    public void updatePassword(String password, String userId) throws IOException {

        User user = findUser(userId);
        if (user != null) {
            user.setHashedPassword(PasswordHashing.hashPassword(password));
            UserManager.saveUserToDatabase(user);
            JOptionPane.showMessageDialog(null, "Password updated successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
