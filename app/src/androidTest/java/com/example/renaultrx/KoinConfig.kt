package com.example.renaultrx

import io.reactivex.rxjava3.core.Observable
import org.koin.dsl.module

object CommunesData {
    private val communesToNbBornes = HashMap<String, Int>()

    init {
        communesToNbBornes["Toulouse"] = 168
        communesToNbBornes["Toulon"] = 18
    }

    fun getCommunes(): Iterable<String> {
        return communesToNbBornes.keys
    }

    fun getNbBornes(codeCommune: String): Int {
        return communesToNbBornes[codeCommune]!!
    }
}

class CommunesServiceStub : ICommunesService {
    override fun search(nom: String): Observable<List<CommuneModel>> {
        return Observable.fromIterable(CommunesData.getCommunes())
            .map { nom -> CommuneModel(nom, nom) }
            .toList()
            .toObservable()
    }

}

class BornesServiceStub : IBornesService {
    override fun search(code: String): Observable<BornesResponse> {
        return Observable.just(BornesResponse(CommunesData.getNbBornes(code)))
    }
}

// Configurer Koin

// Renvoyer un BornesServicesStub quand
// on demande un IBornesService

val appModule = module {
    single { CommunesServiceStub() as ICommunesService }
    single { BornesServiceStub() as IBornesService }
}