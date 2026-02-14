package inf222.aop.account.aspect;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import inf222.aop.account.Account;
import inf222.aop.account.annotation.Transfer;

/* TODO: annotation */
public class TransferAspect {

    /* TODO: add the join point & pointcut & advice */
    public ................................................ {
        /* TODO */

        Logger logger = LoggerFactory.getLogger(......................);

        /* TODO */
        logger.atLevel(.............).log(.......................);

        /* TODO */
    }

    private String logInternationalTransfer(Object[] methodArgs) {
        /* TODO */
        var message = String.format("International transfer from %s to %s, %s %s converted to %s", /* TODO */);
        return message;
    }

    private String logTransferAbove(Object[] methodArgs, double value) {
        /* TODO */
        var message = String.format("Transfer above %s from %s to %s, amount: %s", /* TODO */);
        return message;
    }

    private String logErrors(Object[] methodArgs, String methodName, String[] methodParams) {
        /* TODO */
        var message = String.format("Error in transfer from %s to %s, amount: %s %s, method: %s(%s)", /* TODO */);
        return message;
    }
}
