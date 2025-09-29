package com.example.controle_estoque

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(context: Context?) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Controle de Estoque") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        val coroutineScope = rememberCoroutineScope()
        var nome = remember { mutableStateOf("") }
        var quantidade = remember { mutableStateOf("") }
        var cor = remember { mutableStateOf("") }

        var produtos = remember { mutableStateOf<List<Produto>?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column {
                Column {
                    OutlinedTextField(
                        value = nome.value,
                        onValueChange = { novoTexto -> nome.value = novoTexto },
                        label = { Text("Nome do Produto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )

                    OutlinedTextField(
                        value = quantidade.value,
                        onValueChange = { novoTexto -> quantidade.value = novoTexto },
                        label = { Text("Quantidade") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    OutlinedTextField(
                        value = cor.value,
                        onValueChange = { novoTexto -> cor.value = novoTexto },
                        label = { Text("Cor") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                Row {
                    Button(
                        modifier = Modifier.padding(10.dp, 10.dp),
                        shape = RectangleShape,
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val novoProduto = Produto(
                                        nome = nome.value,
                                        quantidade = quantidade.value,
                                        cor = cor.value
                                    )
                                    val response =
                                        RetrofitClient.apiService.createProduto(novoProduto)

                                    if (response.isSuccessful) {
                                        val produtoCriado = response.body()
                                        Toast.makeText(
                                            context,
                                            "Produto criado com sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        nome.value = ""
                                        quantidade.value = ""
                                        cor.value = ""
                                    } else {
                                        Log.e("API", "Erro na API: ${response.code()}")
                                    }
                                } catch (e: Exception) {
                                    Log.e("API", "Erro na requisição: ${e.message}", e)
                                }
                            }
                        }) {
                        Text(text = "Cadastrar Produto")
                    }

                    Button(
                        modifier = Modifier.padding(10.dp, 10.dp),
                        shape = RectangleShape,
                        onClick = {
                            coroutineScope.launch {
                                try {
                                    val response = RetrofitClient.apiService.getProduto()

                                    if (response.isSuccessful) {
                                        produtos.value = response.body()?.reversed()

                                    } else {
                                        produtos.value = emptyList()
                                    }
                                } catch (e: Exception) {
                                    Log.e("API", "Erro de rede/conexão: ${e.message}", e)
                                }
                            }
                        }) {
                        Text(text = "Listar Produtos")
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyColumn(Modifier.weight(1f)) {
                        items(produtos.value ?: emptyList()) { produto ->
                            Column(
                                modifier = Modifier
                                    .border(
                                        width = 1.5.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(0.dp)
                                    )
                                    .height(90.dp).width(395.dp)
                            ) {
                                Text("Nome: ${produto.nome}", Modifier.padding(5.dp))
                                Text("Quantidade: ${produto.quantidade}", Modifier.padding(5.dp))
                                Text("Cor: ${produto.cor}", Modifier.padding(5.dp))
                            }
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(context = null)
}