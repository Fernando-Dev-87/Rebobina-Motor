package com.example.domain.model

import com.example.data.model.AwgWire

data class FerragemInputData(
    val internalDiameterMm: Double = 105.0, // Diâmetro interno do estator (D)
    val packageLengthMm: Double = 110.0,    // Comprimento do pacote de chapas (L)
    val slotsCount: Int = 36,               // Número de ranhuras (S)
    val slotDepthMm: Double = 18.0,          // Altura da ranhura (h)
    val slotWidthTopMm: Double = 8.5,       // Largura superior da ranhura (b1)
    val slotWidthBottomMm: Double = 6.0,    // Largura inferior da ranhura (b2)
    val poles: Int = 4,                     // 2, 4, 6, 8 polos
    val voltageVolts: Double = 220.0,       // Tensão de linha (V)
    val frequencyHz: Double = 60.0,         // Frequência (Hz)
    val phaseType: MotorPhaseType = MotorPhaseType.THREE_PHASE,
    val connectionType: ConnectionType = ConnectionType.DELTA,
    val inductionTesla: Double = 0.72,      // Indução no entreferro Bmax (0.65 - 0.78 T ou 6500 - 7800 Gauss)
    val targetCurrentDensity: Double = 4.5, // Densidade alvo de corrente (A/mm²)
    val targetFillFactor: Double = 0.38,    // Fator de enchimento prático da ranhura (35% a 42%)
    val windingFactor: Double = 0.95,       // kw (fator de enrolamento)
    val efficiency: Double = 0.86,          // Rendimento estimado
    val powerFactor: Double = 0.82,         // Fator de potência estimado (cos φ)
    val isDoubleLayer: Boolean = true       // Enrolamento de dupla camada ou camada simples
)

data class FerragemCalculationResult(
    val estimatedPowerWatts: Double,
    val estimatedPowerCv: Double,
    val estimatedPowerKw: Double,
    val nominalCurrentAmps: Double,
    val phaseCurrentAmps: Double,
    val synchronousRpm: Int,
    val polePitchMm: Double,
    val polePitchSlots: Double,
    val recommendedCoilPitch: String,
    val poleAreaCm2: Double,
    val magneticFluxWeber: Double,
    val magneticFluxMaxwell: Double,
    val phaseVoltageVolts: Double,
    val inducedEmfVolts: Double,
    val seriesTurnsPerPhase: Int,
    val coilsPerPhase: Int,
    val turnsPerCoil: Int,
    val conductorsPerSlot: Int,
    val slotAreaMm2: Double,
    val copperAreaPerSlotMm2: Double,
    val maxWireSectionBySlotMm2: Double,
    val recommendedWire: AwgWire,
    val actualFillFactor: Double,
    val actualCurrentDensity: Double,
    val parallelWireAlternative: Pair<AwgWire, Double>?,
    val connectionType: ConnectionType,
    val phaseType: MotorPhaseType,
    val estimatedCopperWeightKg: Double,
    val coilsPerGroup: Double,
    val phaseStep: Double,
    val copyableSummary: String
)
