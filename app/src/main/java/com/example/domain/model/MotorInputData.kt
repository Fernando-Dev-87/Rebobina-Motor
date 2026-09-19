package com.example.domain.model

enum class MotorPhaseType(val label: String) {
    THREE_PHASE("Trifásico (3Ø)"),
    SINGLE_PHASE("Monofásico (1Ø)")
}

enum class PowerUnit(val label: String, val toWattsMultiplier: Double) {
    CV("CV (Cavalo-Vapor)", 735.5),
    KW("kW (Quilowatt)", 1000.0)
}

enum class ConnectionType(val label: String, val symbol: String) {
    DELTA("Triângulo", "Δ"),
    STAR("Estrela", "Y")
}

data class MotorInputData(
    val powerValue: Double = 5.0,
    val powerUnit: PowerUnit = PowerUnit.CV,
    val voltageVolts: Double = 220.0,
    val phaseType: MotorPhaseType = MotorPhaseType.THREE_PHASE,
    val connectionType: ConnectionType = ConnectionType.DELTA,
    val poles: Int = 4, // 2, 4, 6, 8
    val frequencyHz: Double = 60.0,
    val slotsCount: Int = 36, // Ranhuras do estator
    val packageLengthMm: Double = 110.0, // Comprimento do pacote de chapas (L)
    val statorInternalDiameterMm: Double = 105.0, // Diâmetro interno do estator (D)
    val targetCurrentDensity: Double = 4.5, // Densidade alvo A/mm² (típico 4.0 - 5.0)
    val targetAirGapFluxDensity: Double = 0.72, // Bmax em Tesla (típico 0.65 - 0.80 T)
    val windingFactor: Double = 0.95, // kw (típico 0.92 a 0.96)
    val efficiency: Double = 0.885, // η (rendimento)
    val powerFactor: Double = 0.81, // cos φ
    val isDoubleLayer: Boolean = true // Enrolamento de dupla camada ou camada simples
)
