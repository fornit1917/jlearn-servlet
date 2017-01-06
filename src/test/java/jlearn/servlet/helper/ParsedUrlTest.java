package jlearn.servlet.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParsedUrlTest {
    @Test
    public void setQueryParam() throws Exception {
        ParsedUrl url = new ParsedUrl("/some/path?p1=1&p2=2");
        url.setQueryParam("p3", "3");
        assertEquals("/some/path?p1=1&p2=2&p3=3", url.toString());

        url.setQueryParam("p3", "33");
        assertEquals("/some/path?p1=1&p2=2&p3=33", url.toString());
    }
}