package ge.ufc.web;

public class DuplicateFault extends Exception {
    public DuplicateFault() {
        super();
    }

    public DuplicateFault(String message) {
        super(message);
    }

    public DuplicateFault(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateFault(Throwable cause) {
        super(cause);
    }

    protected DuplicateFault(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
