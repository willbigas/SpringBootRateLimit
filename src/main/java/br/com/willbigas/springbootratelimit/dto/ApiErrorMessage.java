package br.com.willbigas.springbootratelimit.config.ratelimit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@RequiredArgsConstructor
@Getter
public class ApiErrorMessage {

	private final UUID id = UUID.randomUUID();
	private final int status;
	private final String error;
	private final String message;
	private final LocalDateTime timestamp = LocalDateTime.now(Clock.systemUTC());
	private final String path;
}
