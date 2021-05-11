package com.example.kotlincompilerbug

import io.reactivex.Observable

interface GlobalUserInteractionsProvider {

    enum class Source {
        ON_START, DISPATCH_TOUCH_EVENT, ON_USER_INTERACTION
    }

    fun userInteractions(): Observable<Source>
}