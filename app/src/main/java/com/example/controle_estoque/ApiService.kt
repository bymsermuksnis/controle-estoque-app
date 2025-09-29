package com.example.controle_estoque

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/produto")
    suspend fun createProduto(@Body produto: Produto): Response<Produto>

    @GET("/produtos")
    suspend fun getProduto(): Response<List<Produto>>
}