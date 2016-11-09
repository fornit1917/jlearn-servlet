package jlearn.servlet.service.utility;

import java.util.Map;

public class ErrorDescriptor
{
    private String message;

    public ErrorDescriptor(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
