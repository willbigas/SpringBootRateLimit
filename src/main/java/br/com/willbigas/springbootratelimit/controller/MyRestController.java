package br.com.willbigas.springbootratelimit.controller;

import br.com.willbigas.springbootratelimit.config.ratelimit.annotation.WithRateLimitProtection;
import br.com.willbigas.springbootratelimit.config.ratelimit.annotation.WithRateLimitProtectionCustomized;
import br.com.willbigas.springbootratelimit.dto.MyResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyRestController {

	@GetMapping
	@WithRateLimitProtection
    public MyResponse processRequest() {
        return MyResponse.builder().name("Teste").build();
    }

	@GetMapping("/custom")
	@WithRateLimitProtectionCustomized(rateLimit = 20 , rateDuration = 10000)
    public MyResponse processRequestCustomized() {
        return MyResponse.builder().name("Customizado").build();
    }
}
