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
        internal val annotationConfigApplicationContext: AnnotationConfigApplicationContext by lazy {
            AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
        }

        fun <T> fetchBean(clazz: Class<T>): T = annotationConfigApplicationContext.getBean(clazz)
    }

    @Bean
    open fun gameUrl(@Value("\${blackjack.game.url}") gameUrl: String) = GameUrl(gameUrl)

    @Bean
    open fun blackjackConsumer(gameUrl: GameUrl): BlackjackConsumer {
        val restTemplate = restTemplate()
        restTemplate.uriTemplateHandler = uriTemplateHandler(gameUrl)

        return BlackjackConsumer.DefaultBlackjackConsumer(restTemplate)
    }

    @Bean
    open fun uriTemplateHandler(gameUrl: GameUrl) = object : UriTemplateHandler {
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

    data class GameUrl(internal val url: String) {
        fun removeSuffix(suffix: CharSequence) = url.removeSuffix(suffix)
    }
}