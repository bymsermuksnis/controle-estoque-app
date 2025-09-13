package com.example.controle_estoque

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

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
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = quantidade.value,
                            onValueChange = { novoTexto -> quantidade.value = novoTexto },
                            label = { Text("Quantidade") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )

                        OutlinedTextField(
                            value = cor.value,
                            onValueChange = { novoTexto -> cor.value = novoTexto },
                            label = { Text("Cor") },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
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
                                        val response = RetrofitClient.apiService.createProduto(novoProduto)

                                        if (response.isSuccessful) {
                                            val produtoCriado = response.body()
                                            Toast.makeText(context, "Produto criado com sucesso", Toast.LENGTH_SHORT).show()
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