package com.example.kotlincompilerbug


interface PreferenceStorage {
    operator fun <P : Any> get(preference: P): Boolean

    fun <T : Any, P : Any> put(preference: P, value: T)
}