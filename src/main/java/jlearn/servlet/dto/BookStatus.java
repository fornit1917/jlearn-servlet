package jlearn.servlet.dto;

public enum BookStatus {
    UNREAD, IN_PROGRESS, FINISHED, ABORTED;

    public static BookStatus getByValue(int value)
    {
        return values()[value - 1];
    }

    public int getValue()
    {
        return ordinal() + 1;
    }

    public String toString()
    {
        switch (this) {
            case UNREAD:
                return "Unread";
            case IN_PROGRESS:
                return "In progress";
            case FINISHED:
                return "Finished";
            case ABORTED:
                return "Aborted";
            default:
                return super.toString();
        }
    }
}
