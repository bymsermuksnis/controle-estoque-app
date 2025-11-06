package com.example.controle_estoque

import com.google.gson.annotations.SerializedName

data class Produto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nome")
    val nome: String,
    @SerializedName("quantidade")
    val quantidade: String,
    @SerializedName("cor")
    val cor: String
)