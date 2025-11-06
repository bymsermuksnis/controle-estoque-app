package com.example.controle_estoque

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/produto")
    suspend fun createProduto(@Body produto: Produto): Response<Produto>

    @DELETE("/produto/{id}")
    suspend fun deleteProduto(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("/produtos")
    suspend fun getProduto(): Response<List<Produto>>
}