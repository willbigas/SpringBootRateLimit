package br.com.willbigas.springbootratelimit.config.ratelimit.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WithRateLimitProtectionCustomized {

	int rateLimit() default 200;

	long rateDuration() default 60000;
}
