package edu.ecnu.wclong.util;

public class RandomUtil {
    public static int getIntegerRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static int getIntegerRandomNumber() {
        return getIntegerRandomNumber(1, 100000);
    }

}
