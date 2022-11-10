package ge.ufc.web;

public class InternalErrorFault extends Exception {

    public InternalErrorFault(String message) {
        super(message);
    }

    public InternalErrorFault(Throwable cause) {
        super(cause);
    }

    protected InternalErrorFault(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InternalErrorFault() {
        super("Internal Error");
    }

    public InternalErrorFault(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
