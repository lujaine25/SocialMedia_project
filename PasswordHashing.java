package Backend;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {

    public static String hashPassword(String password) {
        // Generate a salt and hash the password
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        // Check if the password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }
}