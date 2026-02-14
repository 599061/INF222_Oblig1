package inf222.aop.account.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import inf222.aop.account.Account;
import inf222.aop.account.annotation.Transfer;

import static java.lang.Math.log;

@Aspect
public class TransferAspect {

    @Around("execution(* *(..)) && @annotation(config)")
    public Object handleTransfer(ProceedingJoinPoint jp, Transfer config) throws Throwable {
        Object[] args = jp.getArgs();
        MethodSignature methodSig = (MethodSignature) jp.getSignature();
        String methodName = methodSig.getName();
        Logger logger = LoggerFactory.getLogger(methodSig.getDeclaringType());

        try {
            Object result = jp.proceed();
            boolean success = (result instanceof Boolean) ? (Boolean) result : true;

            if (config.internationalTransfer()) {
                logger.atLevel(config.value()).log(logInternationalTransfer(args));
            }

            Double amount = (Double) args[2];
            if (amount > config.LogTransferAbove()) {
                logger.atLevel(config.value()).log(logTransferAbove(args, config.LogTransferAbove()));
            }

            if (!success && config.logErrors()) {
                logger.atLevel(config.value()).log(logErrors(args, methodName, methodSig.getParameterNames()));
            }

            return result;
        } catch (Throwable t) {
            if (config.logErrors()) {
                logger.atLevel(config.value()).log(logErrors(args, methodName, methodSig.getParameterNames()));
            }
            throw t;
        }
    }

    private String logInternationalTransfer(Object[] methodArgs) {
        Account from = (Account) methodArgs[0];
        Account to = (Account) methodArgs[1];
        var message = String.format("International transfer from %s to %s, %s %s converted to %s",
                from.getAccountName(), to.getAccountName(), methodArgs[2], from.getCurrency(), to.getCurrency());
        return message;
    }

    private String logTransferAbove(Object[] methodArgs, double value) {
        var message = String.format("Transfer above %s from %s to %s, amount: %s",
                value, ((Account)methodArgs[0]).getAccountName(), ((Account)methodArgs[1]).getAccountName(), methodArgs[2]);
        return message;
    }

    private String logErrors(Object[] methodArgs, String methodName, String[] methodParams) {
        Account from = (Account) methodArgs[0];
        Account to = (Account) methodArgs[1];
        var message = String.format("Error in transfer from %s to %s, amount: %s %s, method: %s(%s)",
                from.getAccountName(), to.getAccountName(), methodArgs[2], from.getCurrency(), methodName, String.join(", ", methodParams));
        return message;
    }
}
