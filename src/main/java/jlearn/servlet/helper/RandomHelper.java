package jlearn.servlet.helper;

import java.util.concurrent.ThreadLocalRandom;

public class RandomHelper
{
    private static String chars = "qwertyuiop[]asdfghjkl;zxcvbnm,.1234567890!@#$%^&*";

    public String getRandomString(int length)
    {
        char[] randomChars = new char[length];
        for (int i = 0; i < length; i++) {
            randomChars[i] = chars.charAt(ThreadLocalRandom.current().nextInt(0, chars.length()));
        }
        return new String(randomChars);
    }
}
