package com.grocery.flash.network

//import com.example.flash.Data.InternetApiItems
import com.grocery.flash.Data.InternetApiItems
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BaseUrl = "https://training-uploads.internshala.com"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(
        "application/json".toMediaType() //now here inorder to convert json format to
    // text/Images need to use application/json.toMediaType()
    ))
    //ScalarsConverterFactory.create() this convert json into string/Int basic-DT
    .baseUrl(BaseUrl)
    .build()

interface FlashApiService{

    @GET("android/grocery_delivery_app/items.json")
    suspend fun getItems():List<InternetApiItems> //suspend keyword doesn't block the thread which it is running on
}

object FlashApi{
    // this is used to call interface
    //singleton class object can be created only once
    val retrofitService:FlashApiService by lazy {
        retrofit.create(
            FlashApiService::class.java
        )
    }
}