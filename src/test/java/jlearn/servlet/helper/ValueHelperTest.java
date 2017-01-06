package jlearn.servlet.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValueHelperTest {
    @Test
    public void tryParseInt() throws Exception {
        ValueHelper valueHelper = new ValueHelper();
        int res = valueHelper.tryParseInt("123", 0);
        assertEquals(123, res);

        res = valueHelper.tryParseInt(null, 10);
        assertEquals(10, res);

        res = valueHelper.tryParseInt("wqe", 20);
        assertEquals(20, res);
    }

    @Test
    public void getStringFromYearAndMonth() throws Exception {
        ValueHelper valueHelper = new ValueHelper();

        String s = valueHelper.getStringFromYearAndMonth(2017, 1);
        assertEquals("2017, January", s);

        s = valueHelper.getStringFromYearAndMonth(2017, 0);
        assertEquals("2017", s);
    }
}