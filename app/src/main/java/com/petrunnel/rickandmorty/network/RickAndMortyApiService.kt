package com.petrunnel.rickandmorty.network

import com.petrunnel.rickandmorty.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.petrunnel.rickandmorty.model.Character

interface RickAndMortyApiService {

    @GET("character")
    suspend fun getCharacter(
        @Query("page") page: Int
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Character

    @GET("character")
    suspend fun getCharacterByName(
        @Query("page") page: Int,
        @Query("name") name: String
    ): CharacterResponse
}




