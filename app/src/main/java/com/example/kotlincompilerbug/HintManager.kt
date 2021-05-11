package com.example.kotlincompilerbug

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class NaviGuidanceHintManager(
    private val userInteractionsProvider: GlobalUserInteractionsProvider,
    private val debugPreferences: DebugPreferenceManager,
    private val mainThread: Scheduler,
    private val preferences: PreferenceStorage,
) {

    private val needToBeShownSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun bind(show: () -> Unit, hide: () -> Unit): Disposable {
        return needToBeShownSubject
            .observeOn(mainThread)
            .switchMap { needToBeShown ->
                if (needToBeShown) {
                    show().justObservable2()
                        .doOnNext {
                            preferences.put("Preferences.NAVI_GUIDANCE_MENU_TOOLTIP_SHOWN", true)
                        }
                        .switchMap { Observable.merge(Observable.timer(4, TimeUnit.SECONDS), userInteractionsProvider.userInteractions()) }
                        .take(1)
                        .delay(100, TimeUnit.MILLISECONDS)
                        .doOnNext { needToBeShownSubject.onNext(false) }
                } else {
                    hide().justObservable2()
                }
            }
            .subscribe()
    }

    fun showMenuTooltip() {
        needToBeShownSubject.onNext(!preferences["Preferences.NAVI_GUIDANCE_MENU_TOOLTIP_SHOWN"] || debugPreferences["DebugPreferences.IntroAndHints.alwaysShowTooltips"])
    }

}
