package jlearn.servlet.helper;

import java.time.Year;

public class ValueHelper
{
    private static final String[] monthsNames = new String[]{
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
        return ValueHelper.monthsNames;
    }

    public String getStringFromYearAndMonth(int year, int monthNum)
    {
        StringBuilder sb = new StringBuilder();
        if (year > 0) {
            sb.append(year);
            if (monthNum > 0 && monthNum < 13) {
                sb.append(", ");
            }
        }
        if (monthNum > 0 && monthNum < 13) {
            sb.append(ValueHelper.monthsNames[monthNum - 1]);
        }
        return sb.toString();
    }
}
