package com.example.renaultrx

import org.koin.dsl.module

// Configurer Koin

// Renvoyer un BornesServicesHttp quand
// on demande un IBornesService

val appModule = module {
    single { CommunesServiceHttp() as ICommunesService }
    single { BornesServiceHttp() as IBornesService }
}