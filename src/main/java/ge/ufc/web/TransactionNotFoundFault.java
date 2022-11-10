package ge.ufc.web;

public class TransactionNotFoundFault extends Exception {
    public TransactionNotFoundFault() {
        super();
    }

    public TransactionNotFoundFault(String message) {
        super(message);
    }

    public TransactionNotFoundFault(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionNotFoundFault(Throwable cause) {
        super(cause);
    }

    protected TransactionNotFoundFault(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
