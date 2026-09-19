package com.example.domain.model

import com.example.data.model.AwgWire

data class CalculationResult(
    val nominalPowerWatts: Double,
    val nominalCurrentAmps: Double,
    val phaseCurrentAmps: Double,
    val conductorCurrentAmps: Double,
    val synchronousRpm: Int,
    val polePitchMm: Double,
    val poleAreaCm2: Double,
    val magneticFluxWeber: Double,
    val magneticFluxMaxwell: Double,
    val phaseVoltageVolts: Double,
    val seriesTurnsPerPhase: Int,
    val coilsPerPhase: Int,
    val turnsPerCoil: Int,
    val conductorsPerSlot: Int,
    val requiredWireSectionMm2: Double,
    val recommendedWire: AwgWire,
    val actualCurrentDensity: Double,
    val parallelWireAlternative: Pair<AwgWire, Double>?,
    val connectionType: ConnectionType,
    val phaseType: MotorPhaseType,
    val estimatedCopperWeightKg: Double,
    val coilsPerGroup: Double,
    val phaseStep: Double,
    val copyableSummary: String
)
