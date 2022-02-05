package com.gitlab.jactor.blackjack.compose

import com.gitlab.jactor.blackjack.compose.consumer.BlackjackConsumer
import com.gitlab.jactor.blackjack.compose.service.BlackjackService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriTemplateHandler
import java.net.URI

@Configuration
@PropertySource(value = ["classpath:blackjack.properties"])
open class ApplicationConfiguration {
    companion object {
        private val annotationConfigApplicationContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)

        fun <T> fetchBean(clazz: Class<T>): T = annotationConfigApplicationContext.getBean(clazz)
    }

    @Value("\${blackjack.game.url}")
    private val gameUrl: String = ""

    @Bean
    open fun blackjackFonsumer(): BlackjackConsumer {
        val restTemplate = restTemplate()
        restTemplate.uriTemplateHandler = uriTemplateHandler()

        return BlackjackConsumer(restTemplate)
    }

    @Bean
    open fun uriTemplateHandler() = object : UriTemplateHandler {
        override fun expand(uriTemplate: String, uriVariables: MutableMap<String, *>): URI {
            if (uriVariables.isNotEmpty()) {
                TODO(reason = "#1) Uri variables are not supported")
            }

            val baseUrl = gameUrl.removeSuffix("/")
            val contextPath = uriTemplate.removePrefix("/")

            return URI.create("$baseUrl/$contextPath")
        }

        override fun expand(uriTemplate: String, vararg uriVariables: Any?): URI {
            if (uriVariables.isNotEmpty() && (uriVariables.size != 1 && uriVariables.first() != null)) {
                TODO(reason = "#2) Uri variables are not supported")
            }

            val baseUrl = gameUrl.removeSuffix("/")
            val contextPath = uriTemplate.removePrefix("/")

            return URI.create("$baseUrl/$contextPath")
        }
    }

    @Bean
    open fun blackjackService(blackjackConsumer: BlackjackConsumer) = BlackjackService.DefaultBlackjackService(blackjackConsumer)

    @Bean
    @Scope("prototype")
    open fun restTemplate() = RestTemplate()
}
