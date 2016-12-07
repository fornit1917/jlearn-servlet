package jlearn.servlet.helper;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Date;

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

    public Integer tryParseIfNotZero(String s)
    {
        int value = tryParseInt(s, 0);
        if (value == 0) {
            return null;
        } else {
            return new Integer(value);
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

    public int getCurrentYear()
    {
        return Year.now().getValue();
    }

    public int getCurrentMonthNum()
    {
        return LocalDate.now().getMonth().getValue();
    }
}
