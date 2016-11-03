package jlearn.servlet.service;

public class CommandResult<T>
{
    private ErrorDescriptor error;

    private T result;

    public static <T> CommandResult<T> createOkResult()
    {
        return new CommandResult<>();
    }

    public static <T> CommandResult<T> createOkResult(T result)
    {
        return new CommandResult<T>(result, null);
    }

    public static <T> CommandResult<T> createErrorResult(ErrorDescriptor error)
    {
        return new CommandResult<>(null, error);
    }

    public static <T> CommandResult<T> createErrorResult(String error)
    {
        return new CommandResult<>(null, new ErrorDescriptor(error));
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

    public ErrorDescriptor getError()
    {
        return error;
    }
}
