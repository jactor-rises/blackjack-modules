package com.gitlab.jactor.blackjack.model

import com.gitlab.jactor.blackjack.dto.ActionDto

data class Action(
    var isDrawNewCard: Boolean = false
) {
    constructor(action: ActionDto): this(action.isDrawNewCard)
}
