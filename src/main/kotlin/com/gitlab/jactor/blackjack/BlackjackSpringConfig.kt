package com.gitlab.jactor.blackjack

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate

@Configuration
@OpenAPIDefinition(info = Info(title = "blackjack", description = "Spill blackjack mot \"Magnus\"", version = "v0"))
class BlackjackSpringConfig {

    @Bean
    @Scope("prototype")
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
