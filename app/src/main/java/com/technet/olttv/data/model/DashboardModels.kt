package com.technet.olttv.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    val ok: Boolean,
    @SerialName("generated_at")
    val generatedAt: String = "",
    val summary: Summary = Summary(),
    @SerialName("rx_thresholds")
    val rxThresholds: RxThresholds = RxThresholds(),
    @SerialName("top_rx")
    val topRx: List<RxItem> = emptyList(),
    @SerialName("latest_events")
    val latestEvents: List<LatestEvent> = emptyList(),
    @SerialName("top_off_by_olt")
    val topOffByOlt: List<TopOltItem> = emptyList(),
    @SerialName("top_off_by_pon")
    val topOffByPon: List<TopPonItem> = emptyList(),
    @SerialName("latest_candidates")
    val latestCandidates: List<CandidateItem> = emptyList(),
    @SerialName("last_down")
    val lastDown: List<LastDownItem> = emptyList(),
    val error: String? = null,
    val message: String? = null
)

@Serializable
data class Summary(
    @SerialName("total_onus")
    val totalOnus: Int = 0,
    val online: Int = 0,
    val offline: Int = 0,
    val candidates: Int = 0,
    val ignored: Int = 0,
    @SerialName("active_olts")
    val activeOlts: Int = 0,
    @SerialName("online_percent")
    val onlinePercent: Double = 0.0,
    @SerialName("offline_percent")
    val offlinePercent: Double = 0.0,
    @SerialName("rx_attention_count")
    val rxAttentionCount: Int = 0
)

@Serializable
data class RxThresholds(
    @SerialName("critical_lte")
    val criticalLte: Double = -27.0,
    @SerialName("warning_lte")
    val warningLte: Double = -24.0
)

@Serializable
data class RxItem(
    val id: Int = 0,
    @SerialName("client_name")
    val clientName: String = "",
    @SerialName("onu_name")
    val onuName: String = "",
    @SerialName("onu_id")
    val onuId: String = "",
    val pon: String = "",
    val mac: String = "",
    @SerialName("rx_power")
    val rxPower: Double? = null,
    @SerialName("rx_status")
    val rxStatus: String = "",
    @SerialName("rx_label")
    val rxLabel: String = "",
    val status: String = "",
    @SerialName("olt_name")
    val oltName: String = "",
    @SerialName("updated_at")
    val updatedAt: String = ""
)

@Serializable
data class LatestEvent(
    val id: Int = 0,
    @SerialName("client_name")
    val clientName: String = "",
    @SerialName("onu_name")
    val onuName: String = "",
    @SerialName("onu_id")
    val onuId: String = "",
    val pon: String = "",
    val mac: String = "",
    @SerialName("rx_power")
    val rxPower: Double? = null,
    val status: String = "",
    @SerialName("olt_name")
    val oltName: String = "",
    @SerialName("updated_at")
    val updatedAt: String = ""
)

@Serializable
data class TopOltItem(
    @SerialName("olt_name")
    val oltName: String = "",
    @SerialName("olt_ip")
    val oltIp: String = "",
    val total: Int = 0
)

@Serializable
data class TopPonItem(
    @SerialName("olt_name")
    val oltName: String = "",
    val pon: String = "",
    val total: Int = 0
)

@Serializable
data class CandidateItem(
    val id: Int = 0,
    @SerialName("client_name")
    val clientName: String = "",
    @SerialName("onu_name")
    val onuName: String = "",
    @SerialName("onu_id")
    val onuId: String = "",
    val pon: String = "",
    val mac: String = "",
    @SerialName("olt_name")
    val oltName: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
    @SerialName("last_seen_at")
    val lastSeenAt: String = "",
    @SerialName("last_offline_at")
    val lastOfflineAt: String = ""
)

@Serializable
data class LastDownItem(
    val id: Int = 0,
    @SerialName("client_name")
    val clientName: String = "",
    @SerialName("onu_name")
    val onuName: String = "",
    @SerialName("onu_id")
    val onuId: String = "",
    val pon: String = "",
    val mac: String = "",
    @SerialName("rx_power")
    val rxPower: Double? = null,
    val status: String = "",
    @SerialName("olt_name")
    val oltName: String = "",
    @SerialName("olt_ip")
    val oltIp: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
    @SerialName("last_seen_at")
    val lastSeenAt: String = "",
    @SerialName("last_offline_at")
    val lastOfflineAt: String = "",
    @SerialName("down_days")
    val downDays: Int = 0
)
