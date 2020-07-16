package com.example.renaultrx

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Test


class LogObserver<T> : Observer<T> {
    override fun onComplete() {
        println("Completed in ${Thread.currentThread()}")
    }

    override fun onSubscribe(d: Disposable?) {
        println("subscription in ${Thread.currentThread()}")
    }

    override fun onNext(t: T) {
        println("Received value $t in ${Thread.currentThread()}")
    }

    override fun onError(e: Throwable?) {
        println("Error $e in ${Thread.currentThread()}")
    }
}

class RxUnitTest {

    @Test
    fun `tp1#1`() {
        println("coucou")
    }

    @Test
    fun `tp1#2`() {
        // Créer l'observable
        val observable = Observable.create(
            ObservableOnSubscribe<String> { observer ->
                // afficher thread courant
                println("Subscribing in thread ${Thread.currentThread()}")

                // envoyer des événements à observer
                observer.onNext("hello")

                // attendre ...
                Thread.sleep(200)

                observer.onNext("world")

                // terminer
                observer.onComplete()
            })
        println("je suis dans le thread ${Thread.currentThread()}")

        // Creér l'observer
        val observer = LogObserver<String>()

        // Abonner l'observer
        observable.subscribe(observer)
    }
}