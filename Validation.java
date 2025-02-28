package Backend;

public class Validation {

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.com$");
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }


}
