package jlearn.servlet.service;

public class CommandResult<T>
{
    private ErrorDescriptor error;

    private T result;

    public static <T> CommandResult<T> createOkResult()
    {
        return new CommandResult<T>();
    }

    public static <T> CommandResult<T> createOkResult(T result)
    {
        return new CommandResult<T>(result, null);
    }

    public static <T> CommandResult<T> createErrorResult(ErrorDescriptor error)
    {
        return new CommandResult<T>(null, error);
    }

    private CommandResult() {}

    private CommandResult(T result, ErrorDescriptor error)
    {
        this.result = result;
        this.error = error;
    }

    public boolean isError()
    {
        return error != null;
    }

    public T getResult()
    {
        return result;
    }
}
