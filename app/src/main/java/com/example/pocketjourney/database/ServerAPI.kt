package com.example.pocketjourney.database


import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun cerca(@Field("query") query: String): Call <JsonObject>
    //"cerca" serve per effettuare delle interrogazioni in cui bisogna semplicemente recuperare dei dati o verificare che siano presenti nel database

    @POST("postInsert/")
    @FormUrlEncoded
    fun inserisci(@Field("query") query: String): Call <JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun modifica(@Field("query") query: String): Call <JsonObject>

    @POST("postRemove/")
    @FormUrlEncoded
    fun remove(@Field("query") query: String): Call <JsonObject>

    @GET
    fun getAvatar(@Url url: String) : Call <ResponseBody>


}