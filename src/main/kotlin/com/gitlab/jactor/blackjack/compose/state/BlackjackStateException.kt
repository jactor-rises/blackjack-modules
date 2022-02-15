package com.gitlab.jactor.blackjack.compose.state

import com.gitlab.jactor.blackjack.compose.dto.ErrorDto

class GameOfBlackjackException(error: ErrorDto) : Throwable("Error: '${error.message}' by '${error.provider}'!")
