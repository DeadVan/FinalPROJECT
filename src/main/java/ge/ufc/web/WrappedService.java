package ge.ufc.web;

import ge.ufc.web.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface WrappedService {

    @WebMethod(operationName = "checkMethod")
    @WebResult(name = "checkResult")
    String check(@WebParam(name = "user_id") int id) throws UserNotFoundFault, InternalErrorFault, AgentAuthFailedFault, AgentAccessDeniedFault;

    @WebMethod(operationName = "payMethod")
    @WebResult(name = "sysTransId")
    long pay(@WebParam(name = "agent_transaction_id") String agentTransactionId,@WebParam(name = "user_id") int userId,@WebParam(name = "amount") double amount) throws InternalErrorFault, AgentAuthFailedFault, AgentAccessDeniedFault, DuplicateFault, UserNotFoundFault, AmountNotPositiveFault;

    @WebMethod(operationName = "statusMethod")
    @WebResult(name = "sysTransId")
    long status(@WebParam(name = "agent_transaction_id") String agentTransactionId) throws InternalErrorFault, TransactionNotFoundFault, AgentAuthFailedFault, AgentAccessDeniedFault;
}
