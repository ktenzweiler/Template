package com.kodingwithkyle.template.authentication.services

import com.google.gson.Gson
import com.kodingwithkyle.template.authentication.models.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface AuthenticationService {

    @FormUrlEncoded
    @POST("/users/")
    suspend fun registerNewUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): retrofit2.Response<User>

    @FormUrlEncoded
    @POST("/users/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): retrofit2.Response<User>

    @PATCH("/users/logout")
    suspend fun logout(@Header("Authorization") authToken: String): retrofit2.Response<Void>

    object AuthServiceCreator {

        fun newService(): AuthenticationService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpBuilder = OkHttpClient.Builder()
            httpBuilder.addInterceptor(interceptor)
            httpBuilder.addInterceptor(object : Interceptor {

                override fun intercept(chain: Interceptor.Chain): Response {
                    val r = chain.request()
                    val builder = r.newBuilder()
                    builder.addHeader("Accept", "application/json")
                    builder.addHeader("Content-Type", "application/x-www-form-urlencoded")
                    builder.method(r.method, r.body)
                    return chain.proceed(builder.build())
                }
            })
            httpBuilder.connectTimeout(30, TimeUnit.SECONDS)
            httpBuilder.readTimeout(30, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://server-template-1.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .client(httpBuilder.build())
                .build()
            return retrofit.create(AuthenticationService::class.java)
        }
    }
}