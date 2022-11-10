package ge.ufc.web;

public class UserNotFoundFault extends Exception {

    public UserNotFoundFault() {
        super("The specified user does not exist");
    }

    public UserNotFoundFault(String msg) {
        super(msg);
    }
}
