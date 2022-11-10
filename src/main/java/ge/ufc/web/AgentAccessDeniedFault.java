package ge.ufc.web;

public class AgentAccessDeniedFault extends Exception {

    public AgentAccessDeniedFault() {
        super("Access denied");
    }

    public AgentAccessDeniedFault(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
