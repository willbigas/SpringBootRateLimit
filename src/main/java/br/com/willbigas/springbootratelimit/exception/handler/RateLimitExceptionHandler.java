package br.com.willbigas.springbootratelimit.config.handler;

import br.com.willbigas.springbootratelimit.dto.ApiErrorMessage;
import br.com.willbigas.springbootratelimit.exception.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RateLimitExceptionHandler {

	@ExceptionHandler(RateLimitException.class)
	public ResponseEntity<ApiErrorMessage> handleInvalidFieldsInValidJson(final RateLimitException rateLimitException, final HttpServletRequest request) {
		final ApiErrorMessage apiErrorMessage = rateLimitException.toApiErrorMessage(request.getRequestURI());
		logIncomingCallException(rateLimitException, apiErrorMessage);
		return new ResponseEntity<>(apiErrorMessage, HttpStatus.TOO_MANY_REQUESTS);
	}

	private static void logIncomingCallException(final RateLimitException rateLimitException, final ApiErrorMessage apiErrorMessage) {
		log.error(String.format("%s: %s", apiErrorMessage.getId(), rateLimitException.getMessage()), rateLimitException);
	}
}
