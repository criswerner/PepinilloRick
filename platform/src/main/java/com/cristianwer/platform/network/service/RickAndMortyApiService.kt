package com.cristianwer.platform.network.service

import com.cristianwer.platform.data.remote.dto.CharacterResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponseDto
}
