package com.technet.olttv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PinLoginScreen(
    viewModel: AuthViewModel
) {
    val pin by androidx.compose.runtime.remember { androidx.compose.runtime.derivedStateOf { viewModel.enteredPin } }
    val error by androidx.compose.runtime.remember { androidx.compose.runtime.derivedStateOf { viewModel.errorMessage } }

    val buttons = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "Limpar", "0", "Apagar")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF08111F),
                        Color(0xFF0B1730),
                        Color(0xFF09111F)
                    )
                )
            )
    ) {
        Card(
            modifier = Modifier
                .width(760.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xCC0D1B2E))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF1D4ED8), Color(0xFF06B6D4))
                            )
                        )
                ) {
                    Text(
                        text = "OLT TV Monitor",
                        modifier = Modifier.align(Alignment.CenterStart).padding(24.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Acesso protegido",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Digite o PIN para abrir o painel",
                    color = Color(0xFFBFD8FF)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF162235))
                ) {
                    Text(
                        text = pin.map { "●" }.joinToString(" ").ifBlank { "○ ○ ○ ○ ○ ○" },
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 18.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (error != null) {
                    Text(
                        text = error ?: "",
                        color = Color(0xFFFCA5A5),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(320.dp).width(420.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(buttons) { item ->
                        when (item) {
                            "Limpar" -> KeyButton(
                                label = item,
                                background = Color(0xFF7C3AED),
                                onClick = { viewModel.clear() }
                            )

                            "Apagar" -> KeyButton(
                                label = item,
                                background = Color(0xFF475569),
                                onClick = { viewModel.onBackspace() }
                            )

                            else -> KeyButton(
                                label = item,
                                background = Color(0xFF1D4ED8),
                                onClick = { viewModel.onDigit(item) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { viewModel.submit() },
                    modifier = Modifier.width(280.dp).focusable()
                ) {
                    Text("Entrar")
                }

                Spacer(modifier = Modifier.height(26.dp))
            }
        }
    }
}

@Composable
private fun KeyButton(
    label: String,
    background: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .focusable(),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
