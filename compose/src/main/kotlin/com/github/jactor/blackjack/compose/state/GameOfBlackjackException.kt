package com.github.jactor.blackjack.compose.state

import com.github.jactor.blackjack.dto.ErrorDto

class GameOfBlackjackException(error: ErrorDto) : Throwable("Error: '${error.message}' by '${error.provider}'!")
