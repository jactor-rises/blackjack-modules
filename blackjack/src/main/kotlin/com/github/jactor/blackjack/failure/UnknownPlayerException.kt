package com.github.jactor.blackjack.failure

class UnknownPlayerException(nick: String) : RuntimeException(initUnknownPlayerMessage(nick)) {
    companion object {
        private fun initUnknownPlayerMessage(nick: String) = "Ingen aktive spill for spiller med kallenavn '{}'".replace("{}", nick)
    }
}
