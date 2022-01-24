package com.github.jactor.blackjack.blackjack

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate

@Configuration
class BlackjackSpringConfig {

    @Bean
    @Scope("prototype")
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
