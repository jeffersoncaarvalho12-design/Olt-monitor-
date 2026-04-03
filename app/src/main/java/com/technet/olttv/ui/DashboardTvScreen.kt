package com.technet.olttv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.technet.olttv.data.model.CandidateItem
import com.technet.olttv.data.model.DashboardResponse
import com.technet.olttv.data.model.LastDownItem
import com.technet.olttv.data.model.LatestEvent
import com.technet.olttv.data.model.RxItem
import com.technet.olttv.data.model.TopOltItem
import com.technet.olttv.data.model.TopPonItem

@Composable
fun DashboardTvScreen(
    viewModel: DashboardViewModel,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF06101C),
                        Color(0xFF08192B),
                        Color(0xFF06101C)
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        when {
            state.isLoading && state.data == null -> {
                CenterMessage("Carregando dashboard da OLT...")
            }

            state.errorMessage != null && state.data == null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Erro ao carregar",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.errorMessage ?: "",
                        color = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    TvActionButton(
                        text = "Tentar novamente",
                        onClick = { viewModel.manualRefresh() }
                    )
                }
            }

            else -> {
                state.data?.let { data ->
                    DashboardContent(
                        data = data,
                        lastUpdateLabel = state.lastUpdateLabel,
                        errorMessage = state.errorMessage,
                        onRefresh = { viewModel.manualRefresh() },
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    data: DashboardResponse,
    lastUpdateLabel: String,
    errorMessage: String?,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 10.dp)
    ) {
        item {
            HeaderSection(
                title = "OLT TV Monitor V1.2",
                subtitle = "Painel Android TV com proteção por PIN, top RX, eventos e ranking operacional.",
                lastUpdateLabel = lastUpdateLabel,
                onRefresh = onRefresh,
                onLogout = onLogout
            )
        }

        item {
            SummarySection(data = data)
        }

        if (errorMessage != null) {
            item {
                AlertCard(
                    title = "Falha na atualização",
                    message = errorMessage,
                    background = Color(0xFF5F1D1D)
                )
            }
        }

        item { SectionTitle("Top ONUs com RX alterado ou crítico") }
        item { RxSection(items = data.topRx) }

        item { SectionTitle("Últimos eventos") }
        item { EventsSection(items = data.latestEvents) }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    RankingOltSection(items = data.topOffByOlt)
                }
                Box(modifier = Modifier.weight(1f)) {
                    RankingPonSection(items = data.topOffByPon)
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CandidatesSection(items = data.latestCandidates)
                }
                Box(modifier = Modifier.weight(1f)) {
                    LastDownSection(items = data.lastDown)
                }
            }
        }

        item {
            FooterSection(lastUpdateLabel = lastUpdateLabel)
        }
    }
}

@Composable
private fun HeaderSection(
    title: String,
    subtitle: String,
    lastUpdateLabel: String,
    onRefresh: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0C182A)),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                    text = title,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subtitle,
                        color = Color(0xFFBFD8FF),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Última atualização: $lastUpdateLabel",
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TvActionButton(
                        text = "Atualizar",
                        onClick = onRefresh
                    )
                    TvActionButton(
                        text = "Sair",
                        onClick = onLogout,
                        baseColor = Color(0xFF334155)
                    )
                }
            }
        }
    }
}

@Composable
private fun TvActionButton(
    text: String,
    onClick: () -> Unit,
    baseColor: Color = Color(0xFF06B6D4)
) {
    val isFocused = remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = Modifier
            .height(42.dp)
            .onFocusChanged { isFocused.value = it.isFocused }
            .graphicsLayer {
                scaleX = if (isFocused.value) 1.06f else 1f
                scaleY = if (isFocused.value) 1.06f else 1f
            }
            .border(
                width = if (isFocused.value) 2.dp else 0.dp,
                color = if (isFocused.value) Color.White else Color.Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .focusable(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = baseColor
        ),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SummarySection(data: DashboardResponse) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        item { SummaryCard("Total ONUs", data.summary.totalOnus.toString(), Color(0xFF1E40AF)) }
        item { SummaryCard("Online", data.summary.online.toString(), Color(0xFF16A34A)) }
        item { SummaryCard("Offline", data.summary.offline.toString(), Color(0xFFDC2626)) }
        item { SummaryCard("Candidatas", data.summary.candidates.toString(), Color(0xFFF97316)) }
        item { SummaryCard("RX atenção", data.summary.rxAttentionCount.toString(), Color(0xFF7C3AED)) }
        item { SummaryCard("OLTs ativas", data.summary.activeOlts.toString(), Color(0xFF0891B2)) }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    background: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .width(190.dp)
            .height(96.dp)
            .focusable()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.96f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = Color.White,
        fontWeight = FontWeight.Black
    )
}

@Composable
private fun RxSection(items: List<RxItem>) {
    if (items.isEmpty()) {
        EmptyCard("Nenhuma ONU com RX alterado ou crítico.")
        return
    }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items.take(8)) { item ->
            RxCard(item)
        }
    }
}

@Composable
private fun RxCard(item: RxItem) {
    val bg = when (item.rxStatus.lowercase()) {
        "critical" -> Color(0xFF7F1D1D)
        "warning" -> Color(0xFF9A3412)
        else -> Color(0xFF1E293B)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = bg),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .width(285.dp)
            .focusable()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = item.clientName.ifBlank { "Sem nome" },
                color = Color.White,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "RX: ${item.rxPower ?: "--"} dBm",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            StatusBadge(
                text = item.rxLabel,
                background = if (item.rxStatus.lowercase() == "critical") Color(0xFFB91C1C) else Color(0xFFEA580C)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("OLT: ${item.oltName}", color = Color(0xFFF1F5F9), style = MaterialTheme.typography.bodySmall)
            Text("PON: ${item.pon} • ONU: ${item.onuId}", color = Color(0xFFF1F5F9), style = MaterialTheme.typography.bodySmall)
            Text("MAC: ${item.mac.ifBlank { "não informado" }}", color = Color(0xFFE9D5FF), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun EventsSection(items: List<LatestEvent>) {
    if (items.isEmpty()) {
        EmptyCard("Sem eventos recentes.")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.take(4).forEach { item ->
            EventCard(item)
        }
    }
}

@Composable
private fun EventCard(item: LatestEvent) {
    val statusColor = if (item.status.equals("Down", ignoreCase = true)) {
        Color(0xFFDC2626)
    } else {
        Color(0xFF16A34A)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .focusable()
            .border(1.2.dp, statusColor, RoundedCornerShape(18.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.clientName.ifBlank { "Sem nome" },
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("OLT: ${item.oltName}", color = Color(0xFFCBD5E1), style = MaterialTheme.typography.bodySmall)
                Text("PON: ${item.pon} • ONU: ${item.onuId}", color = Color(0xFFCBD5E1), style = MaterialTheme.typography.bodySmall)
                Text("MAC: ${item.mac.ifBlank { "não informado" }}", color = Color(0xFFE9D5FF), style = MaterialTheme.typography.bodySmall)
                Text("RX: ${item.rxPower?.toString() ?: "não informado"} dBm", color = Color(0xFFBFD8FF), style = MaterialTheme.typography.bodySmall)
                Text("Atualizado: ${item.updatedAt}", color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.width(10.dp))

            StatusBadge(
                text = item.status.ifBlank { "N/A" },
                background = statusColor
            )
        }
    }
}

@Composable
private fun RankingOltSection(items: List<TopOltItem>) {
    SectionPanel("Ranking por OLT") {
        if (items.isEmpty()) {
            EmptyInline("Sem OLTs com ONU offline.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.take(5).forEach {
                    RankingLine(
                        title = it.oltName,
                        subtitle = it.oltIp,
                        value = it.total.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun RankingPonSection(items: List<TopPonItem>) {
    SectionPanel("Ranking por PON") {
        if (items.isEmpty()) {
            EmptyInline("Sem PONs com ONU offline.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.take(5).forEach {
                    RankingLine(
                        title = it.oltName,
                        subtitle = "PON ${it.pon}",
                        value = it.total.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun CandidatesSection(items: List<CandidateItem>) {
    SectionPanel("Últimas candidatas") {
        if (items.isEmpty()) {
            EmptyInline("Nenhuma candidata à remoção.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.take(4).forEach { item ->
                    SimpleItemCard(
                        title = item.clientName.ifBlank { "Sem nome" },
                        lines = listOf(
                            "OLT: ${item.oltName}",
                            "PON: ${item.pon} • ONU: ${item.onuId}",
                            "MAC: ${item.mac.ifBlank { "não informado" }}"
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LastDownSection(items: List<LastDownItem>) {
    SectionPanel("Últimas ONUs offline") {
        if (items.isEmpty()) {
            EmptyInline("Nenhuma ONU offline.")
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.take(4).forEach { item ->
                    SimpleItemCard(
                        title = item.clientName.ifBlank { "Sem nome" },
                        lines = listOf(
                            "OLT: ${item.oltName}",
                            "PON: ${item.pon} • ONU: ${item.onuId}",
                            "RX: ${item.rxPower?.toString() ?: "não informado"} dBm",
                            "Dias down: ${item.downDays}"
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FooterSection(lastUpdateLabel: String) {
    Text(
        text = "OLT TV Monitor • Última sincronização: $lastUpdateLabel",
        color = Color(0xFF94A3B8),
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun SectionPanel(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1B2E)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun RankingLine(
    title: String,
    subtitle: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF162235))
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(subtitle, color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = value,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun SimpleItemCard(
    title: String,
    lines: List<String>
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF162235)),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .focusable()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Black, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            lines.forEach { line ->
                Text(line, color = Color(0xFFCBD5E1), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun EmptyCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1B2E)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(18.dp)) {
            Text(message, color = Color(0xFF94A3B8))
        }
    }
}

@Composable
private fun EmptyInline(message: String) {
    Text(message, color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
}

@Composable
private fun AlertCard(
    title: String,
    message: String,
    background: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(message, color = Color(0xFFF8FAFC), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    background: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            color = Color.White,
            fontWeight = FontWeight.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun CenterMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
