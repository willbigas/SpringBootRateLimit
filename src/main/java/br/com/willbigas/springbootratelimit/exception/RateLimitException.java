package br.com.willbigas.springbootratelimit.config.ratelimit.exception;

import br.com.willbigas.springbootratelimit.config.ratelimit.dto.ApiErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends RuntimeException {

	public RateLimitException(String message) {
		super(message);
	}

	public ApiErrorMessage toApiErrorMessage(final String path) {
        return new ApiErrorMessage(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.name(), this.getMessage(), path);
    }
}
