package com.github.jactor.blackjack.aop

import com.github.jactor.blackjack.model.GameOfBlackjack
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class ResultLogger {

    @AfterReturning(pointcut = "execution(* com.github.jactor.blackjack.service.GameService.*(..))", returning = "gameOfBlackjack")
    fun logResultOfBlackjack(gameOfBlackjack: GameOfBlackjack) {
        gameOfBlackjack.logResult()
    }
}