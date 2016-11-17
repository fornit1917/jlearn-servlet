package jlearn.servlet.helper;

public class ValueHelper
{
    public int tryParseInt(String s, int defaultValue)
    {
        if (s == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int tryParseInt(String s)
    {
        return tryParseInt(s, 0);
    }
}
