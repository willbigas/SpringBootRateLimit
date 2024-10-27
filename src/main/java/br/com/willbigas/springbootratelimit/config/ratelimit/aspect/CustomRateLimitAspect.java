package br.com.willbigas.springbootratelimit.config.ratelimit.aspect;

import br.com.willbigas.springbootratelimit.config.ratelimit.annotation.WithRateLimitProtectionCustomized;
import br.com.willbigas.springbootratelimit.exception.RateLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CustomRateLimitAspect {

	public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";
	private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

	/**
	 * Executed by each call of a method annotated with {@link br.com.willbigas.springbootratelimit.config.ratelimit.annotation.WithRateLimitProtectionCustomized} which should be an HTTP endpoint.
	 * Counts calls per remote address. Calls older than {@link #rateDuration} milliseconds will be forgotten. If there have
	 * been more than {@link #rateLimit} calls within {@link #rateDuration} milliseconds from a remote address, a {@link RateLimitException}
	 * will be thrown.
	 *
	 * @throws RateLimitException iff rate limit for a given remote address has been exceeded
	 */
	@Around("@annotation(br.com.willbigas.springbootratelimit.config.ratelimit.annotation.WithRateLimitProtectionCustomized)")
	public Object rateLimitCustomized(ProceedingJoinPoint joinPoint) throws Throwable {
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		WithRateLimitProtectionCustomized annotation = method.getAnnotation(WithRateLimitProtectionCustomized.class);

		// Definindo limites a partir da anotação ou dos valores padrão
		int rateLimit = annotation.rateLimit();
		long rateDuration = annotation.rateDuration();


		final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		final String key = requestAttributes.getRequest().getRemoteAddr();
		final long currentTime = System.currentTimeMillis();
		requestCounts.putIfAbsent(key, new ArrayList<>());
		requestCounts.get(key).add(currentTime);
		cleanUpRequestCounts(currentTime, rateDuration);
		if (requestCounts.get(key).size() > rateLimit) {
			throw new RateLimitException(String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), key, rateDuration));
		}
		return joinPoint.proceed();
	}

	private void cleanUpRequestCounts(final long currentTime, final long rateDuration) {
		requestCounts.values().forEach(l -> {
			l.removeIf(t -> timeIsTooOld(currentTime, t, rateDuration));
		});
	}

	private boolean timeIsTooOld(final long currentTime, final long timeToCheck, final long rateDuration) {
		return currentTime - timeToCheck > rateDuration;
	}
}
