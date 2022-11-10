package ge.ufc.web;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.*;
import java.util.Base64;


@WebService(endpointInterface = "ge.ufc.web.WrappedService")
public class WrappedServiceImpl implements WrappedService {

    private static String ipAddress = getRemoteAddress();
    private Connection conn;
    private HttpServletRequest req = (HttpServletRequest) WrappedServiceImpl.wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);


    @Resource
    public static WebServiceContext wsContext;

    @Override
    public String check(int id) throws UserNotFoundFault, InternalErrorFault, AgentAuthFailedFault, AgentAccessDeniedFault {
        try {
            conn = DatabaseManager.getDatabaseConnection();
            int agentId = Integer.parseInt(req.getHeader("agent"));
            String password;
            try {
                password = new String(Base64.getDecoder().decode(req.getHeader("pass")));
            } catch (Exception e) {
                throw new AgentAuthFailedFault();
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM AGENTS WHERE ID = ? AND PASSWORD = ?")) {
                ps.setInt(1, agentId);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try(PreparedStatement ps2 = conn.prepareStatement("SELECT ALLOWED_IP FROM AGENT_ACCESS WHERE AGENT_ID = ?")) {
                            ps2.setInt(1, agentId);

                            try(ResultSet rs2 = ps2.executeQuery()) {

                                if (rs2.next()) {
                                    String allowedIp = rs2.getString("allowed_ip");
                                    if (!ipAddress.equals(allowedIp)) {
                                        throw new AgentAccessDeniedFault();
                                    }
                                } else {
                                    throw new AgentAccessDeniedFault();
                                }
                            }
                        } catch (SQLException e) {
                            throw new InternalErrorFault("Internal Error", e);
                        }
                    } else {
                        throw new AgentAuthFailedFault();
                    }
                }
            } catch (SQLException e) {
                throw new InternalErrorFault("Internal Error", e);
            }
        } catch (InternalErrorFault e) {
            throw new InternalErrorFault("Internal Error", e);
        } finally {
            DatabaseManager.close(conn);
        }
        conn = DatabaseManager.getDatabaseConnection();
        try(final PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String lastname = rs.getString("lastname");
                    double balance = rs.getDouble("balance");

                    return name.charAt(0) + ". " + lastname.charAt(0) + ". " + balance + "₾";
                } else {
                    throw new UserNotFoundFault("User not Found in the Database");
                }
            }
        } catch (final SQLException e) {
            throw new InternalErrorFault("Unable to get user from the database" , e);
        }
    }

    @Override
    public long pay(String agentTransactionId, int userId, double amount) throws InternalErrorFault, AgentAuthFailedFault, AgentAccessDeniedFault, DuplicateFault, UserNotFoundFault, AmountNotPositiveFault {
        try {
            conn = DatabaseManager.getDatabaseConnection();
            int agentId = Integer.parseInt(req.getHeader("agent"));
            String password;
            try {
                password = new String(Base64.getDecoder().decode(req.getHeader("pass")));
            } catch (Exception e) {
                throw new AgentAuthFailedFault();
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM AGENTS WHERE ID = ? AND PASSWORD = ?")) {
                ps.setInt(1, agentId);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try(PreparedStatement ps2 = conn.prepareStatement("SELECT ALLOWED_IP FROM AGENT_ACCESS WHERE AGENT_ID = ?")) {
                            ps2.setInt(1, agentId);

                            try(ResultSet rs2 = ps2.executeQuery()) {

                                if (rs2.next()) {
                                    String allowedIp = rs2.getString("allowed_ip");
                                    if (!ipAddress.equals(allowedIp)) {
                                        throw new AgentAccessDeniedFault();
                                    }
                                } else {
                                    throw new AgentAccessDeniedFault();
                                }
                            }
                        } catch (SQLException e) {
                            throw new InternalErrorFault("Internal Error", e);
                        }
                    } else {
                        throw new AgentAuthFailedFault();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (InternalErrorFault e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseManager.close(conn);
        }        conn = DatabaseManager.getDatabaseConnection();
        int agentId = Integer.parseInt(req.getHeader("agent"));
        try(final PreparedStatement ps = conn.prepareStatement("INSERT INTO TRANSACTIONS(AGENT_ID, AGENT_TRANSACTION_ID, USER_ID, AMOUNT) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, agentId);
            ps.setString(2, agentTransactionId);
            ps.setInt(3, userId);
            ps.setDouble(4, amount);
            ps.executeUpdate();

            try (PreparedStatement ps2 = conn.prepareStatement("UPDATE USERS SET BALANCE = BALANCE + (SELECT AMOUNT FROM TRANSACTIONS WHERE USER_ID = ? AND AGENT_TRANSACTION_ID = ?) WHERE ID = ?")) {
                ps2.setInt(1, userId);
                ps2.setString(2, agentTransactionId);
                ps2.setInt(3, userId);
                ps2.executeUpdate();
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new InternalErrorFault();
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long status(String agentTransactionId) throws InternalErrorFault, TransactionNotFoundFault, AgentAuthFailedFault, AgentAccessDeniedFault {
        try {
            conn = DatabaseManager.getDatabaseConnection();
            int agentId = Integer.parseInt(req.getHeader("agent"));
            String password;
            try {
                password = new String(Base64.getDecoder().decode(req.getHeader("pass")));
            } catch (Exception e) {
                throw new AgentAuthFailedFault();
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM AGENTS WHERE ID = ? AND PASSWORD = ?")) {
                ps.setInt(1, agentId);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try(PreparedStatement ps2 = conn.prepareStatement("SELECT ALLOWED_IP FROM AGENT_ACCESS WHERE AGENT_ID = ?")) {
                            ps2.setInt(1, agentId);

                            try(ResultSet rs2 = ps2.executeQuery()) {

                                if (rs2.next()) {
                                    String allowedIp = rs2.getString("allowed_ip");
                                    if (!ipAddress.equals(allowedIp)) {
                                        throw new AgentAccessDeniedFault();
                                    }
                                } else {
                                    throw new AgentAccessDeniedFault();
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        throw new AgentAuthFailedFault();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (InternalErrorFault e) {
            throw new InternalErrorFault("Internal Error", e);
        } finally {
            DatabaseManager.close(conn);
        }
        conn = DatabaseManager.getDatabaseConnection();
        final int agentId = Integer.parseInt(req.getHeader("agent"));
        try(final PreparedStatement ps = conn.prepareStatement("SELECT SYSTEM_TRANSACTION_ID FROM TRANSACTIONS WHERE AGENT_TRANSACTION_ID = ? AND AGENT_ID = ?")) {
            ps.setString(1, agentTransactionId);
            ps.setInt(2, agentId);

            try(final ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new TransactionNotFoundFault();
                }
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getRemoteAddress() {
        return ((HttpServletRequest) WrappedServiceImpl.wsContext.getMessageContext()
                .get(MessageContext.SERVLET_REQUEST))
                .getRemoteAddr();
    }
}
