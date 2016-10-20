package jlearn.servlet.service;

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
