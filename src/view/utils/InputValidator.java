package view.utils;

public class InputValidator {

    public static boolean isInt(String str) {
        if (str == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int toInt(String str) {
        if(isInt(str)) {
            return Integer.parseInt(str);
        }
        return -1;
    }

}
