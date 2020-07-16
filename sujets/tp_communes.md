# TP communes

## Rendre le champ text observable

La dépendance `com.jakewharton.rxbinding4` ajoutée au build.gradle de l'application, met à disposition une fonction `afterTextChangeEvents` sur le champ texte de l'activité:

```kotlin
editTextTextCommune.afterTextChangeEvents() // ceci est un observable
```

- Logger les valeurs saisies par l'utilisateur.

## Brancher l'observable à CommunesService

- "Mapper" le changement du champ texte à un appel à `CommunesService`. S'enregistrer à l'observable obtenu pour mettre à jour la liste.
- Retarder le déclenchement de la recherche à 300ms après la dernière saisie
- Eviter de déclencher la recherche tant le terme saisi fait moins de 3 caractères