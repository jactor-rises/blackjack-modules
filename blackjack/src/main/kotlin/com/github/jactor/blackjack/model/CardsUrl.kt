package com.github.jactor.blackjack.model

class CardsUrl(defaultUrl: String) {
    init {
        url = defaultUrl
    }

    companion object {
        lateinit var url: String
    }
}
