package com.example.renaultrx

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://medium.com/@riz_maulana/part-1-of-3-networking-with-rxjava-on-android-like-a-boss-b9d151fd0c17

interface CommunesApi {
  @GET("communes?fields=nom,code")
  fun search(@Query("nom") nom: String): Observable<List<CommuneModel>>
}

class CommunesService {
  private val LOG_TAG = CommunesService::class.simpleName

  private val communesApi = Retrofit.Builder()
    .client(
      OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()
    )
    .baseUrl("https://geo.api.gouv.fr")
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .build()
    .create(CommunesApi::class.java)

  fun search(nom: String): Observable<List<CommuneModel>> {
    Log.d(LOG_TAG, "Searching $nom...")
    val time = System.currentTimeMillis()
    return communesApi.search(nom)
      .doOnNext({ r -> Log.d(LOG_TAG, "Got ${r.size} in ${System.currentTimeMillis() - time}ms") })
      .doOnError({ e -> Log.e(LOG_TAG, "Failed to search $nom", e) })
      .doOnComplete { Log.d(LOG_TAG, "Retrofit call complete") }
      .doOnDispose { Log.d(LOG_TAG, "Disposing retrofit call") }
  }
}