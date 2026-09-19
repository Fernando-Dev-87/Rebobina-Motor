package com.example.domain.calculator

import com.example.data.datasource.AwgTable
import com.example.data.model.AwgWire
import kotlin.math.PI
import kotlin.math.sqrt

/**
 * Engine para cálculos avançados de oficina: Conversão de Tensão, 
 * Estimativa de Peso de Cobre e Cálculo de Capacitores.
 */
object ConversionEngine {

    private const val COPPER_DENSITY_G_MM3 = 0.00896

    /**
     * Calcula o peso estimado de cobre (kg) para o bobinado.
     * Peso = (Comprimento Médio da Espira * Condutores Totais * Seção do Fio * Densidade)
     */
    fun calculateCopperWeight(
        internalDiameterMm: Double,
        packageLengthMm: Double,
        poles: Int,
        totalSlots: Int,
        conductorsPerSlot: Int,
        wireSectionMm2: Double
    ): Double {
        // Estimativa WEG do comprimento médio de uma espira (Lme)
        // Lme = 2 * (L + K * PassoPolar) -> K varia conforme polos
        val polePitchMm = (PI * internalDiameterMm) / poles
        val meanTurnFactor = when (poles) {
            2 -> 1.45
            4 -> 1.30
            6 -> 1.25
            else -> 1.20
        }
        val meanTurnLengthMm = 2.0 * (packageLengthMm + meanTurnFactor * polePitchMm)
        
        val totalConductors = totalSlots * conductorsPerSlot
        val totalLengthMm = meanTurnLengthMm * (totalConductors / 2.0)
        
        val volumeMm3 = totalLengthMm * wireSectionMm2
        val weightGrams = volumeMm3 * COPPER_DENSITY_G_MM3
        
        return weightGrams / 1000.0 // Retorna em kg
    }

    /**
     * Retorna a recomendação de capacitores (Partida e Permanente) para motores monofásicos.
     */
    fun getCapacitorRecommendation(powerCv: Double, voltage: Double): CapacitorResult {
        val is220V = voltage > 150.0
        
        val starting = when {
            powerCv <= 0.25 -> "189-227 µF"
            powerCv <= 0.33 -> "216-259 µF"
            powerCv <= 0.50 -> "270-324 µF"
            powerCv <= 0.75 -> "340-408 µF"
            powerCv <= 1.00 -> "430-516 µF"
            powerCv <= 1.50 -> "540-648 µF"
            powerCv <= 2.00 -> "600-720 µF"
            else -> "800-950 µF"
        }
        
        val permanent = if (is220V) {
            when {
                powerCv <= 0.25 -> "8 - 10 µF"
                powerCv <= 0.33 -> "12 - 15 µF"
                powerCv <= 0.50 -> "15 - 20 µF"
                powerCv <= 0.75 -> "20 - 25 µF"
                powerCv <= 1.00 -> "25 - 35 µF"
                powerCv <= 1.50 -> "35 - 45 µF"
                else -> "45 - 60 µF"
            }
        } else {
            when {
                powerCv <= 0.25 -> "25 - 30 µF"
                powerCv <= 0.33 -> "35 - 40 µF"
                powerCv <= 0.50 -> "45 - 50 µF"
                powerCv <= 0.75 -> "60 - 70 µF"
                powerCv <= 1.00 -> "80 - 100 µF"
                else -> "N/A"
            }
        }

        return CapacitorResult(
            startingCapacitor = starting,
            permanentCapacitor = permanent,
            minVoltage = if (is220V) "380V / 440V" else "250V"
        )
    }
}

data class CapacitorResult(
    val startingCapacitor: String,
    val permanentCapacitor: String,
    val minVoltage: String
)
