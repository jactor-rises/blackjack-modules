package com.gitlab.jactor.blackjack.compose.model

object ExceptionUtil {
    fun initErrorMessagesFrom(error: Throwable): Set<String> {
        return buildSet {
            var printError: Throwable? = error

            do {
                addAll(error.message?.split(";") ?: emptyList())
                printError = printError?.cause
            } while (printError != null)

            add("¯\\_(ツ)_/¯")

            val localStack: MutableMap<String, MutableSet<Int>> = HashMap()
            error.stackTrace.toList()
                .filter { it.fileName != null }
                .filter { it.className.contains("com.gitlab.jactor") }
                .forEach {
                    val filename = it.fileName!!
                    if (!localStack.contains(filename)) {
                        localStack[filename] = mutableSetOf(it.lineNumber)
                    } else {
                        localStack[filename]!!.add(it.lineNumber)
                    }
                }

            localStack.forEach {
                add("${it.key} line${if (it.value.size > 1) "s" else ""} ${it.value.joinToString(separator = ",")}")
            }
        }
    }
}
