package jlearn.servlet.helper;

import java.time.Year;

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

    public int[] getYearsRange()
    {
        int year = Year.now().getValue();
        int[] result = new int[year - 1990 + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = year - i;
        }
        return result;
    }

    public String[] getMonthsNames()
    {
        return new String[]{
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
        };
    }
}
