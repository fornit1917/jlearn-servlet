package jlearn.servlet.entity;

public enum BookStatus {
    UNREAD, IN_PROGRESS, READED, IN_PROGRESS_REPEAT, UNFINISHED;

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
                return "New, Unread";
            case IN_PROGRESS:
                return "I Read It Now";
            case READED:
                return "Readed";
            case IN_PROGRESS_REPEAT:
                return "I Read It Now Again";
            case UNFINISHED:
                return "I Read It but Stopped";
            default:
                return super.toString();
        }
    }
}
