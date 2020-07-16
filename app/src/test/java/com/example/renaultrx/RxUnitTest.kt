package com.example.renaultrx

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.observers.DisposableObserver
import org.junit.Test
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class LogObserver<T> : DisposableObserver<T>() {
    override fun onComplete() {
        println("Completed in ${Thread.currentThread()}")
    }

//    override fun onSubscribe(subscription: Disposable?) {
//        println("subscription in ${Thread.currentThread()}")
//    }

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

    @Test
    fun `tp1#3`() {
        // Créer l'observable
        val observable = Observable.create(
            ObservableOnSubscribe<String> { observer ->
                thread(name = "monThread") {
                    // afficher thread courant
                    println("Subscribing in thread ${Thread.currentThread()}")

                    // envoyer des événements à observer
                    observer.onNext("hello")

                    // attendre ...
                    Thread.sleep(1000)

                    if (!observer.isDisposed)
                    observer.onNext("world")

                    // terminer
                    observer.onComplete()
                }
            })
        println("je suis dans le thread ${Thread.currentThread()}")

        // Creér l'observer
        val observer = LogObserver<String>()

        // Abonner l'observer
        observable.subscribe(observer)
        Thread.sleep(300)
        observer.dispose()
    }

    @Test
    fun `tp2#1`() {
        Observable.just("hello", "world").subscribe(::println)
    }

    @Test
    fun `tp2#2`() {
        Observable.error<String>(RuntimeException("boom")).subscribe(
            Functions.emptyConsumer(), // Consumer<String> {},
            Consumer { e -> println("Error: $e") }
        )
    }

    @Test
    fun `tp2#3`() {
        // Test Observable.empty
        val subscription = Observable.empty<String>().subscribe(
            Functions.emptyConsumer(),
            Consumer { e -> println("Error: $e") },
            Action { println("Completed") }
        )

        subscription.dispose()
    }

    fun search(term: String): Observable<String> {
        return Observable
            .just("$term: Toulouse")
            .doOnNext { println("Emmitting $term: Toulouse") }
            .delay(1, TimeUnit.SECONDS)
            .doOnDispose { println("Disposing $term: Toulouse") }
            .doOnComplete{ println("Completing $term: Toulouse") }
    }

    @Test
    fun flatMap() {
        Observable.just("t", "to", "tou")
            .switchMap { v -> search(v) }
            .blockingSubscribe({result -> println("Result: $result") })
    }
}