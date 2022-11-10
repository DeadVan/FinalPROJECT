package ge.ufc.web;

public class AgentAuthFailedFault extends Exception {

    public AgentAuthFailedFault() {
        super("Authorization Failed");
    }

    public AgentAuthFailedFault(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
