package ch.heigvd.pro.b04.android.network

import ch.heigvd.pro.b04.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A singleton class, containing some application-level instances that should probably be shared
 * amongst invocations.
 */
object Rockin {

    /**
     * The [RockinAPI] instance that should be used across the application to make some requests.
     */
    @get:JvmName("api")
    @get:JvmStatic
    val api: RockinAPI = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(RockinAPI::class.java)
}