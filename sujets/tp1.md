# TP1 : 1er observable

## Mise en place du projet

- Créer un nouveau Android qui servira pour tous les TPs.
- Ajouter la dépendance RxJava au projet: https://github.com/ReactiveX/RxJava/#getting-started
- Ajouter un test unitaire dans lequel faire tourner le Hello World de la doc référencé ci-avant


## Classe LogObserver

Définir une classe `LogObserver` implémentant `Observer` qui écrit dans la console les événements reçus ainsi que le thread courant.


## Création d'un observable synchrone

Dans un nouveau test unitaire:
- Créer une instance d'Observable qui
  - affiche le thread courant,
  - émet un message,
  - attend un peu puis se termine
- Afficher un message dans la console après l'instanciation de l'observable
- Abonner un `LogObserver`
- Afficher un message dans la console

Quand s'exécute l'Observable ?

## Création d'un observable asynchrone

Dans un nouveau test unitaire:
- Faire comme précédemment mais l'Observable fera son traitement dans nouveau thread
- Faire en sorte que l'observer ait le temps d'être appelé en utilisant `Thread.sleep`

Dans quel thread sont reçus les différents événements ?

Pour créer un thread avec kotlin:
```kotlin
import kotlin.concurrent.thread
...
thread(name="mythread") {
	println("Running ${Thread.currentThread()}")
}
```