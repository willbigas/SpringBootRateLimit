package br.com.willbigas.springbootratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RateLimitIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testProcessRequestExceedingRateLimit() throws Exception {
		// Chamar a rota várias vezes para exceder o limite de taxa padrão
		int calls = 200; // Chamar 200 vezes para exceder o limite padrão de 50
		for (int i = 0; i < calls; i++) {
			mockMvc.perform(get("/api"))
					.andExpect(status().isOk());
		}

		// A próxima chamada deve exceder o limite
		mockMvc.perform(get("/api"))
				.andExpect(status().isTooManyRequests()); // HTTP 429
	}

	@Test
	public void testProcessRequestCustomizedExceedingRateLimit() throws Exception {
		// Chamar a rota personalizada várias vezes para exceder o limite de taxa definido
		int calls = 20; // Chamar 10 vezes para exceder o limite de 20
		for (int i = 0; i < calls; i++) {
			mockMvc.perform(get("/api/custom"))
					.andExpect(status().isOk());
		}

		// A próxima chamada deve exceder o limite
		mockMvc.perform(get("/api/custom"))
				.andExpect(status().isTooManyRequests()); // HTTP 429
	}
}
