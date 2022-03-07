package com.github.jactor.blackjack.model

data class GameId(val id: String = java.lang.Long.toHexString(System.currentTimeMillis()))
