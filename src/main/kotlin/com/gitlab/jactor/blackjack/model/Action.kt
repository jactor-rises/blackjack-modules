package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.ActionDto

enum class Action {
    START, HIT, END;

    companion object {
        fun valueOf(action: ActionDto): Action {
            val enum = action.value ?: throw IllegalStateException("Cannot map when value is null")
            return values().first { it.name == enum.name }
        }
    }
}
