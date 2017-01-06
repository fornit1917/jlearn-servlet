package jlearn.servlet.helper;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UrlHelperTest {

    private UrlHelper urlHelper;

    @Before
    public void beforeEachTest() {
        ServletContext servletContextMock = mock(ServletContext.class);
        when(servletContextMock.getContextPath()).thenReturn("");
        urlHelper = new UrlHelper(servletContextMock);
    }

    @Test
    public void path() throws Exception {
        String path = urlHelper.path("/my/path");
        assertEquals("/my/path", path);
    }

    @Test
    public void getRequestUriSegment() throws Exception {
        HttpServletRequest reqMock = mock(HttpServletRequest.class);
        when(reqMock.getRequestURI())
                .thenReturn("/")
                .thenReturn("/one/two/three");


        String part = urlHelper.getRequestUriSegment(reqMock, 1);
        assertEquals("", part);

        part = urlHelper.getRequestUriSegment(reqMock, 2);
        assertEquals("two", part);
    }
}