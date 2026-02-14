package inf222.aop.measures;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

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

    @Around("get(double inf222.aop.measures..*)")
    public Object handleFieldAccess(ProceedingJoinPoint jp) throws Throwable {
        double value = (double) jp.proceed();
        Matcher matcher = pattern.matcher(jp.getSignature().getName());

        if (matcher.matches()) {
            return value * toMeter.get(matcher.group(1));
        }
        return value;
    }

    @Around("set(double inf222.aop.measures..*) && !cflow(execution(*.new(..)))")
    public void handleFieldModification(ProceedingJoinPoint jp) throws Throwable {

        double newValue = (double) jp.getArgs()[0];

        if (newValue < 0) {
            throw new Error("Illegal modification");
        }

        String fieldName = jp.getSignature().getName();
        Matcher matcher = pattern.matcher(fieldName);

        if (matcher.matches()) {
            String unit = matcher.group(1);
            double rate = toMeter.get(unit);

            double correctedValue = newValue / rate;

            jp.proceed(new Object[] {correctedValue});
        } else {
            jp.proceed();
        }
    }

}
