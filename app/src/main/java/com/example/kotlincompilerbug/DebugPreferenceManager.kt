package com.example.kotlincompilerbug

import io.reactivex.Observable

interface DebugPreferenceManager {
    operator fun <T : Boolean> get(preferenceKey: Any): T
}


fun <T> T.justObservable2(): Observable<T> = Observable.just(this)
