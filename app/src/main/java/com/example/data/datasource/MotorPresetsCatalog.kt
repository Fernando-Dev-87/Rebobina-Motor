package com.example.data.datasource

import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.PowerUnit
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

data class StandardMotorPresetSpec(
    val cv: Double,
    val poles: Int,
    val rpm: Int,
    val slots: Int,
    val statorDiameterMm: Double,
    val packageLengthMm: Double,
    val efficiency: Double,
    val powerFactor: Double,
    val targetCurrentDensity: Double = 4.5,
    val targetAirGapFluxDensity: Double = 0.72
)

object MotorPresetsCatalog {

    val catalog: List<StandardMotorPresetSpec> = listOf(
        // === 2 POLOS (~3600 RPM) ===
        StandardMotorPresetSpec(cv = 0.25, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 50.0, packageLengthMm = 45.0, efficiency = 0.68, powerFactor = 0.78),
        StandardMotorPresetSpec(cv = 0.33, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 50.0, packageLengthMm = 50.0, efficiency = 0.70, powerFactor = 0.79),
        StandardMotorPresetSpec(cv = 0.5, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 55.0, packageLengthMm = 55.0, efficiency = 0.73, powerFactor = 0.80),
        StandardMotorPresetSpec(cv = 0.75, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 62.0, packageLengthMm = 60.0, efficiency = 0.76, powerFactor = 0.82),
        StandardMotorPresetSpec(cv = 1.0, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 62.0, packageLengthMm = 70.0, efficiency = 0.80, powerFactor = 0.83),
        StandardMotorPresetSpec(cv = 1.5, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 75.0, packageLengthMm = 70.0, efficiency = 0.82, powerFactor = 0.84),
        StandardMotorPresetSpec(cv = 2.0, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 85.0, packageLengthMm = 80.0, efficiency = 0.84, powerFactor = 0.85),
        StandardMotorPresetSpec(cv = 3.0, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 85.0, packageLengthMm = 95.0, efficiency = 0.85, powerFactor = 0.86),
        StandardMotorPresetSpec(cv = 4.0, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 90.0, packageLengthMm = 105.0, efficiency = 0.86, powerFactor = 0.87),
        StandardMotorPresetSpec(cv = 5.0, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 95.0, packageLengthMm = 115.0, efficiency = 0.875, powerFactor = 0.88),
        StandardMotorPresetSpec(cv = 6.0, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 100.0, packageLengthMm = 120.0, efficiency = 0.88, powerFactor = 0.88),
        StandardMotorPresetSpec(cv = 7.5, poles = 2, rpm = 3600, slots = 24, statorDiameterMm = 105.0, packageLengthMm = 130.0, efficiency = 0.89, powerFactor = 0.88),
        StandardMotorPresetSpec(cv = 10.0, poles = 2, rpm = 3600, slots = 36, statorDiameterMm = 120.0, packageLengthMm = 140.0, efficiency = 0.90, powerFactor = 0.89),
        StandardMotorPresetSpec(cv = 12.5, poles = 2, rpm = 3600, slots = 36, statorDiameterMm = 120.0, packageLengthMm = 155.0, efficiency = 0.905, powerFactor = 0.89),
        StandardMotorPresetSpec(cv = 15.0, poles = 2, rpm = 3600, slots = 36, statorDiameterMm = 130.0, packageLengthMm = 165.0, efficiency = 0.91, powerFactor = 0.89),
        StandardMotorPresetSpec(cv = 20.0, poles = 2, rpm = 3600, slots = 36, statorDiameterMm = 145.0, packageLengthMm = 180.0, efficiency = 0.92, powerFactor = 0.90),
        StandardMotorPresetSpec(cv = 25.0, poles = 2, rpm = 3600, slots = 36, statorDiameterMm = 150.0, packageLengthMm = 195.0, efficiency = 0.925, powerFactor = 0.90),
        StandardMotorPresetSpec(cv = 30.0, poles = 2, rpm = 3600, slots = 36, statorDiameterMm = 160.0, packageLengthMm = 210.0, efficiency = 0.93, powerFactor = 0.91),

        // === 4 POLOS (~1800 RPM) ===
        StandardMotorPresetSpec(cv = 0.25, poles = 4, rpm = 1800, slots = 24, statorDiameterMm = 55.0, packageLengthMm = 50.0, efficiency = 0.68, powerFactor = 0.68),
        StandardMotorPresetSpec(cv = 0.33, poles = 4, rpm = 1800, slots = 24, statorDiameterMm = 55.0, packageLengthMm = 55.0, efficiency = 0.70, powerFactor = 0.70),
        StandardMotorPresetSpec(cv = 0.5, poles = 4, rpm = 1800, slots = 24, statorDiameterMm = 62.0, packageLengthMm = 60.0, efficiency = 0.74, powerFactor = 0.72),
        StandardMotorPresetSpec(cv = 0.75, poles = 4, rpm = 1800, slots = 24, statorDiameterMm = 62.0, packageLengthMm = 70.0, efficiency = 0.77, powerFactor = 0.73),
        StandardMotorPresetSpec(cv = 1.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 75.0, packageLengthMm = 75.0, efficiency = 0.825, powerFactor = 0.75),
        StandardMotorPresetSpec(cv = 1.5, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 75.0, packageLengthMm = 90.0, efficiency = 0.84, powerFactor = 0.76),
        StandardMotorPresetSpec(cv = 2.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 85.0, packageLengthMm = 95.0, efficiency = 0.85, powerFactor = 0.78),
        StandardMotorPresetSpec(cv = 3.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 85.0, packageLengthMm = 115.0, efficiency = 0.865, powerFactor = 0.80),
        StandardMotorPresetSpec(cv = 4.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 95.0, packageLengthMm = 120.0, efficiency = 0.875, powerFactor = 0.80),
        StandardMotorPresetSpec(cv = 5.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 105.0, packageLengthMm = 110.0, efficiency = 0.885, powerFactor = 0.81),
        StandardMotorPresetSpec(cv = 6.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 110.0, packageLengthMm = 125.0, efficiency = 0.89, powerFactor = 0.815),
        StandardMotorPresetSpec(cv = 7.5, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 120.0, packageLengthMm = 135.0, efficiency = 0.895, powerFactor = 0.82),
        StandardMotorPresetSpec(cv = 10.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 135.0, packageLengthMm = 145.0, efficiency = 0.91, powerFactor = 0.83),
        StandardMotorPresetSpec(cv = 12.5, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 135.0, packageLengthMm = 165.0, efficiency = 0.915, powerFactor = 0.84),
        StandardMotorPresetSpec(cv = 15.0, poles = 4, rpm = 1800, slots = 36, statorDiameterMm = 145.0, packageLengthMm = 180.0, efficiency = 0.92, powerFactor = 0.85),
        StandardMotorPresetSpec(cv = 20.0, poles = 4, rpm = 1800, slots = 48, statorDiameterMm = 160.0, packageLengthMm = 195.0, efficiency = 0.925, powerFactor = 0.85),
        StandardMotorPresetSpec(cv = 25.0, poles = 4, rpm = 1800, slots = 48, statorDiameterMm = 160.0, packageLengthMm = 225.0, efficiency = 0.93, powerFactor = 0.86),
        StandardMotorPresetSpec(cv = 30.0, poles = 4, rpm = 1800, slots = 48, statorDiameterMm = 180.0, packageLengthMm = 230.0, efficiency = 0.935, powerFactor = 0.86),

        // === 6 POLOS (~1200 RPM) ===
        StandardMotorPresetSpec(cv = 0.5, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 75.0, packageLengthMm = 75.0, efficiency = 0.72, powerFactor = 0.67),
        StandardMotorPresetSpec(cv = 0.75, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 75.0, packageLengthMm = 85.0, efficiency = 0.75, powerFactor = 0.68),
        StandardMotorPresetSpec(cv = 1.0, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 85.0, packageLengthMm = 90.0, efficiency = 0.78, powerFactor = 0.70),
        StandardMotorPresetSpec(cv = 1.5, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 90.0, packageLengthMm = 100.0, efficiency = 0.80, powerFactor = 0.72),
        StandardMotorPresetSpec(cv = 2.0, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 95.0, packageLengthMm = 110.0, efficiency = 0.82, powerFactor = 0.73),
        StandardMotorPresetSpec(cv = 3.0, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 105.0, packageLengthMm = 125.0, efficiency = 0.84, powerFactor = 0.75),
        StandardMotorPresetSpec(cv = 4.0, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 115.0, packageLengthMm = 135.0, efficiency = 0.85, powerFactor = 0.76),
        StandardMotorPresetSpec(cv = 5.0, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 120.0, packageLengthMm = 145.0, efficiency = 0.86, powerFactor = 0.77),
        StandardMotorPresetSpec(cv = 7.5, poles = 6, rpm = 1200, slots = 36, statorDiameterMm = 135.0, packageLengthMm = 165.0, efficiency = 0.88, powerFactor = 0.78),
        StandardMotorPresetSpec(cv = 10.0, poles = 6, rpm = 1200, slots = 54, statorDiameterMm = 150.0, packageLengthMm = 180.0, efficiency = 0.895, powerFactor = 0.80),
        StandardMotorPresetSpec(cv = 15.0, poles = 6, rpm = 1200, slots = 54, statorDiameterMm = 170.0, packageLengthMm = 210.0, efficiency = 0.91, powerFactor = 0.81),

        // === 8 POLOS (~900 RPM) ===
        StandardMotorPresetSpec(cv = 0.5, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 85.0, packageLengthMm = 90.0, efficiency = 0.70, powerFactor = 0.62),
        StandardMotorPresetSpec(cv = 0.75, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 90.0, packageLengthMm = 95.0, efficiency = 0.72, powerFactor = 0.63),
        StandardMotorPresetSpec(cv = 1.0, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 95.0, packageLengthMm = 105.0, efficiency = 0.74, powerFactor = 0.64),
        StandardMotorPresetSpec(cv = 2.0, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 115.0, packageLengthMm = 125.0, efficiency = 0.78, powerFactor = 0.68),
        StandardMotorPresetSpec(cv = 3.0, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 125.0, packageLengthMm = 140.0, efficiency = 0.81, powerFactor = 0.70),
        StandardMotorPresetSpec(cv = 5.0, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 145.0, packageLengthMm = 165.0, efficiency = 0.84, powerFactor = 0.73),
        StandardMotorPresetSpec(cv = 7.5, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 160.0, packageLengthMm = 185.0, efficiency = 0.86, powerFactor = 0.74),
        StandardMotorPresetSpec(cv = 10.0, poles = 8, rpm = 900, slots = 48, statorDiameterMm = 175.0, packageLengthMm = 210.0, efficiency = 0.88, powerFactor = 0.75)
    )

    /**
     * Converte entrada de RPM ou Polos do usuário para quantidade de polos inteira.
     */
    fun parsePolesFromRpmOrPoles(input: String): Int {
        val clean = input.trim().lowercase()
        if (clean == "2" || clean == "2p" || clean == "2 polos") return 2
        if (clean == "4" || clean == "4p" || clean == "4 polos") return 4
        if (clean == "6" || clean == "6p" || clean == "6 polos") return 6
        if (clean == "8" || clean == "8p" || clean == "8 polos") return 8

        val num = clean.filter { it.isDigit() }.toDoubleOrNull() ?: 1800.0
        return when {
            num in 2.0..2.9 -> 2
            num in 4.0..4.9 -> 4
            num in 6.0..6.9 -> 6
            num in 8.0..8.9 -> 8
            num >= 2500.0 -> 2 // ~3600 rpm
            num >= 1350.0 -> 4 // ~1800 rpm
            num >= 950.0 -> 6  // ~1200 rpm
            else -> 8          // ~900 rpm
        }
    }

    /**
     * Retorna a rotação síncrona teórica (60Hz) para o número de polos
     */
    fun getSyncRpm(poles: Int): Int {
        return when (poles) {
            2 -> 3600
            4 -> 1800
            6 -> 1200
            8 -> 900
            else -> 1800
        }
    }

    /**
     * Encontra a especificação padrão mais adequada para a potência (CV) e polos informados.
     */
    fun findSpec(cv: Double, poles: Int): StandardMotorPresetSpec {
        val specsForPoles = catalog.filter { it.poles == poles }
        if (specsForPoles.isEmpty()) {
            return findSpec(cv, 4)
        }

        // Tenta encontrar exato ou o mais próximo
        val closest = specsForPoles.minByOrNull { abs(it.cv - cv) }
            ?: specsForPoles.first()

        // Se for uma potência personalizada muito diferente (ex: 45 CV), faz interpolação proporcional
        if (cv > 0 && abs(closest.cv - cv) / closest.cv > 0.35) {
            val scale = (cv / closest.cv).pow(0.33)
            return closest.copy(
                cv = cv,
                statorDiameterMm = (closest.statorDiameterMm * scale).roundToInt().toDouble(),
                packageLengthMm = (closest.packageLengthMm * scale).roundToInt().toDouble(),
                efficiency = closest.efficiency.coerceIn(0.70, 0.95),
                powerFactor = closest.powerFactor.coerceIn(0.70, 0.92)
            )
        }

        return closest.copy(cv = cv)
    }

    /**
     * Cria os dados de entrada de domínio completos com base em CV, RPM e Tensão
     */
    fun createMotorInput(
        cv: Double,
        rpmOrPolesInput: String,
        voltageVolts: Double,
        phaseType: MotorPhaseType = MotorPhaseType.THREE_PHASE,
        connectionType: ConnectionType = ConnectionType.DELTA
    ): MotorInputData {
        val poles = parsePolesFromRpmOrPoles(rpmOrPolesInput)
        val spec = findSpec(cv, poles)

        return MotorInputData(
            powerValue = cv,
            powerUnit = PowerUnit.CV,
            voltageVolts = if (voltageVolts > 0) voltageVolts else 220.0,
            phaseType = phaseType,
            connectionType = connectionType,
            poles = poles,
            frequencyHz = 60.0,
            slotsCount = spec.slots,
            packageLengthMm = spec.packageLengthMm,
            statorInternalDiameterMm = spec.statorDiameterMm,
            targetCurrentDensity = spec.targetCurrentDensity,
            targetAirGapFluxDensity = spec.targetAirGapFluxDensity,
            windingFactor = 0.95,
            efficiency = spec.efficiency,
            powerFactor = spec.powerFactor,
            isDoubleLayer = true
        )
    }
}
