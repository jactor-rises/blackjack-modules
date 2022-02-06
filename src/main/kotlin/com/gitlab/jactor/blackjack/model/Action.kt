package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.ActionDto

enum class Action {
    START, HIT, END;

    companion object {
        fun valueOf(action: ActionDto): Action? {
            val name = action.value?.name ?: return null
            return values().first { it.name == name }
        }
    }
}
