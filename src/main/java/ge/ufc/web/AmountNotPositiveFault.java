package ge.ufc.web;

public class AmountNotPositiveFault extends Exception {
    public AmountNotPositiveFault() {
        super();
    }

    public AmountNotPositiveFault(String message) {
        super(message);
    }

    public AmountNotPositiveFault(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountNotPositiveFault(Throwable cause) {
        super(cause);
    }

    protected AmountNotPositiveFault(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
