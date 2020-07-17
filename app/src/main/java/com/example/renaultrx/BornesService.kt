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


class BornesResponse(val nhits: Int) {
}

interface BornesApi {
  @GET("?rows=0&dataset=fichier-consolide-des-bornes-de-recharge-pour-vehicules-electriques-irve")
  fun search(@Query("refine.code_insee") code: String): Observable<BornesResponse>
}

class BornesService {
  private val LOG_TAG = BornesService::class.simpleName

  private val bornesApi = Retrofit.Builder()
    .client(
      OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()
    )
    .baseUrl("https://public.opendatasoft.com/api/records/1.0/search/")
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .build()
    .create(BornesApi::class.java)

  fun search(code: String): Observable<BornesResponse> {
    Log.d(LOG_TAG, "Searching $code...")
    val time = System.currentTimeMillis()
    return bornesApi.search(code)
      .doOnNext({ r -> Log.d(LOG_TAG, "Response in ${System.currentTimeMillis() - time}ms") })
      .doOnError({ e -> Log.e(LOG_TAG, "Failed to search $code", e) })
      .doOnComplete { Log.d(LOG_TAG, "Retrofit call complete") }
      .doOnDispose { Log.d(LOG_TAG, "Disposing retrofit call") }
  }
}