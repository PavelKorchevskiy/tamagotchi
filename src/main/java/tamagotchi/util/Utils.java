package tamagotchi.util;

public class Utils {

    public static String pathWithHappiness(String path, int happiness) {
        return path + happiness + ".jpg";
    }
    public static String pathWithSizePng(String path, int size) {
        return path + "_size_" + size + ".png";
    }
}