package inf222.aop.measures;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class MeasureAspect {
    private final String regex;
    private final Pattern pattern;

    private final Map<String, Double> toMeter = new HashMap<String, Double>(Map.of(
            "m", 1d,
            "ft", 0.3048d,
            "in", 0.0254d,
            "cm", 0.01d,
            "yd", 0.9144d));

    public MeasureAspect() {
        String elems = String.join("|", toMeter.keySet());
        regex = String.format(".*_(%s)$", elems);
        pattern = Pattern.compile(regex);
    }

    @Before("set(double inf222.aop.measures..*)")
    public void validate(JoinPoint jp) {
        if ((double) jp.getArgs()[0] < 0) {
            throw new Error("Illegal modification");
        }
    }

    @Around("get(double inf222.aop.measures..*)")
    public Object handleAccess(ProceedingJoinPoint jp) throws Throwable {
        double val = (double) jp.proceed();
        Matcher m = pattern.matcher(jp.getSignature().getName());
        return m.matches() ? val * toMeter.get(m.group(1)) : val;
    }

    @Around("set(double inf222.aop.measures..*) && !cflow(execution(*.new(..)))")
    public void handleModification(ProceedingJoinPoint jp) throws Throwable {
        double val = (double) jp.getArgs()[0];
        Matcher m = pattern.matcher(jp.getSignature().getName());
        if (m.matches()) {
            jp.proceed(new Object[]{ val / toMeter.get(m.group(1)) });
        } else {
            jp.proceed();
        }
    }

}
