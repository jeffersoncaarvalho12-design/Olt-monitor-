package com.technet.olttv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PinLoginScreen(
    viewModel: AuthViewModel
) {
    val pin by remember { derivedStateOf { viewModel.enteredPin } }
    val error by remember { derivedStateOf { viewModel.errorMessage } }

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
                .width(640.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xD90D1B2E))
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
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Acesso protegido",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Digite o PIN para abrir o painel",
                    color = Color(0xFFBFD8FF),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF162235))
                ) {
                    Text(
                        text = pin.map { "●" }.joinToString(" ").ifBlank { "○ ○ ○ ○ ○ ○" },
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 14.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (error != null) {
                    Text(
                        text = error ?: "",
                        color = Color(0xFFFCA5A5),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .height(270.dp)
                        .width(390.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(buttons) { item ->
                        when (item) {
                            "Limpar" -> FocusKeyButton(
                                label = item,
                                background = Color(0xFF7C3AED),
                                onClick = { viewModel.clear() }
                            )

                            "Apagar" -> FocusKeyButton(
                                label = item,
                                background = Color(0xFF475569),
                                onClick = { viewModel.onBackspace() }
                            )

                            else -> FocusKeyButton(
                                label = item,
                                background = Color(0xFF1D4ED8),
                                onClick = { viewModel.onDigit(item) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                FocusActionButton(
                    text = "Entrar",
                    onClick = { viewModel.submit() },
                    modifier = Modifier.width(240.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun FocusKeyButton(
    label: String,
    background: Color,
    onClick: () -> Unit
) {
    val isFocused = remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .onFocusChanged { isFocused.value = it.isFocused }
            .graphicsLayer {
                scaleX = if (isFocused.value) 1.08f else 1f
                scaleY = if (isFocused.value) 1.08f else 1f
            }
            .border(
                width = if (isFocused.value) 3.dp else 1.dp,
                color = if (isFocused.value) Color.White else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            )
            .focusable(),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background)
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun FocusActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFocused = remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(52.dp)
            .onFocusChanged { isFocused.value = it.isFocused }
            .graphicsLayer {
                scaleX = if (isFocused.value) 1.08f else 1f
                scaleY = if (isFocused.value) 1.08f else 1f
            }
            .border(
                width = if (isFocused.value) 3.dp else 1.dp,
                color = if (isFocused.value) Color.White else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .focusable(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF06B6D4)
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Black
        )
    }
}
