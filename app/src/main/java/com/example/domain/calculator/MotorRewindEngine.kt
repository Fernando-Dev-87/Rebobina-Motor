package com.example.domain.calculator

import com.example.data.datasource.AwgTable
import com.example.domain.model.CalculationResult
import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.ValidationItem
import com.example.domain.model.ValidationReport
import com.example.domain.model.ValidationSeverity
import java.util.Locale
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sqrt

object MotorRewindEngine {

    /**
     * Realiza o cálculo físico e eletromagnético completo para rebobinagem de estator.
     */
    fun calculate(input: MotorInputData): CalculationResult {
        val powerWatts = input.powerValue * input.powerUnit.toWattsMultiplier

        // 1. Corrente Nominal (In)
        val nominalCurrent = when (input.phaseType) {
            MotorPhaseType.THREE_PHASE -> {
                powerWatts / (sqrt(3.0) * input.voltageVolts * input.efficiency * input.powerFactor)
            }
            MotorPhaseType.SINGLE_PHASE -> {
                powerWatts / (input.voltageVolts * input.efficiency * input.powerFactor)
            }
        }

        // 2. Corrente e Tensão de Fase
        val (phaseVoltage, phaseCurrent) = when (input.phaseType) {
            MotorPhaseType.THREE_PHASE -> {
                when (input.connectionType) {
                    ConnectionType.DELTA -> Pair(input.voltageVolts, nominalCurrent / sqrt(3.0))
                    ConnectionType.STAR -> Pair(input.voltageVolts / sqrt(3.0), nominalCurrent)
                }
            }
            MotorPhaseType.SINGLE_PHASE -> Pair(input.voltageVolts, nominalCurrent)
        }

        // Corrente no condutor (considerando 1 circuito paralelo padrão)
        val conductorCurrent = phaseCurrent

        // 3. Rotação Síncrona (Ns)
        val synchronousRpm = ((120.0 * input.frequencyHz) / input.poles).roundToInt()

        // 4. Geometria Polar
        val diameterMeters = input.statorInternalDiameterMm / 1000.0
        val lengthMeters = input.packageLengthMm / 1000.0

        val polePitchMm = (PI * input.statorInternalDiameterMm) / input.poles
        val polePitchMeters = polePitchMm / 1000.0
        val poleAreaCm2 = (polePitchMm / 10.0) * (input.packageLengthMm / 10.0)

        // 5. Fluxo Magnético por Polo (Φ)
        // Φ = Bmédio * Ap = (2/π) * Bmax * (polePitchMeters * lengthMeters)
        // Com Bmax em Tesla:
        val magneticFluxWeber = (2.0 / PI) * input.targetAirGapFluxDensity * (polePitchMeters * lengthMeters)
        val magneticFluxMaxwell = magneticFluxWeber * 100_000_000.0

        // 6. F.E.M induzida por fase (Ef) ~ 0.96 * Vfase
        val inducedEmf = 0.96 * phaseVoltage

        // 7. Número de Espiras em Série por Fase (Nf)
        // Ef = 4.44 * f * Nf * Φ * kw  =>  Nf = Ef / (4.44 * f * Φ * kw)
        val seriesTurnsPerPhaseRaw = inducedEmf / (4.44 * input.frequencyHz * magneticFluxWeber * input.windingFactor)
        val seriesTurnsPerPhase = seriesTurnsPerPhaseRaw.roundToInt().coerceAtLeast(1)

        // 8. Espiras por Bobina (Nb) e Condutores por Ranhura (Zr)
        val phasesCount = if (input.phaseType == MotorPhaseType.THREE_PHASE) 3 else 1
        val coilsPerPhase = if (input.isDoubleLayer) {
            (input.slotsCount.toDouble() / phasesCount).roundToInt().coerceAtLeast(1)
        } else {
            (input.slotsCount.toDouble() / (2.0 * phasesCount)).roundToInt().coerceAtLeast(1)
        }

        val rawTurnsPerCoil = seriesTurnsPerPhase.toDouble() / coilsPerPhase
        val turnsPerCoil = rawTurnsPerCoil.roundToInt().coerceAtLeast(1)

        val conductorsPerSlot = if (input.isDoubleLayer) {
            turnsPerCoil * 2
        } else {
            turnsPerCoil
        }

        // 9. Seção Teórica e Escolha do Fio AWG
        val requiredWireSectionMm2 = conductorCurrent / input.targetCurrentDensity
        val recommendedWire = AwgTable.findClosestSingleWire(requiredWireSectionMm2)
        val actualCurrentDensity = conductorCurrent / recommendedWire.sectionMm2

        // Alternativa em 2 fios em paralelo para seções grandes (AWG 16 ou maior)
        val parallelAlternative = if (requiredWireSectionMm2 >= 1.0) {
            AwgTable.findTwoParallelWires(requiredWireSectionMm2)
        } else {
            null
        }

        // 10. Inteligência Manual WEG: Padrões de Bobinagem
        val coilsPerGroup = WegStandards.calculateCoilsPerGroup(input.slotsCount, input.poles, phasesCount)
        val phaseStep = WegStandards.calculatePhaseStep(input.slotsCount, input.poles)

        // 11. Estimativa de Peso de Cobre (kg)
        val estimatedCopperWeightKg = ConversionEngine.calculateCopperWeight(
            internalDiameterMm = input.statorInternalDiameterMm,
            packageLengthMm = input.packageLengthMm,
            poles = input.poles,
            totalSlots = input.slotsCount,
            conductorsPerSlot = conductorsPerSlot,
            wireSectionMm2 = recommendedWire.sectionMm2
        )

        // Resumo formatado para cópia de bancada
        val summary = buildString {
            appendLine("=== FICHA TÉCNICA DE REBOBINAGEM (PADRÃO WEG) ===")
            appendLine("Motor: ${input.powerValue} ${input.powerUnit.name} | ${input.phaseType.label}")
            appendLine("Tensão: ${input.voltageVolts.roundToInt()}V | Ligação: ${input.connectionType.symbol}")
            appendLine("Polos: ${input.poles}P (${synchronousRpm} RPM síncrono)")
            
            appendLine("------------------------------------")
            appendLine("ESQUEMA TÉCNICO (MANUAL WEG):")
            appendLine("Bobinas por Grupo: ${if (coilsPerGroup % 1.0 == 0.0) coilsPerGroup.toInt().toString() else String.format(Locale.US, "%.1f", coilsPerGroup)}")
            appendLine("Passo de Fase (Defasagem): ${String.format(Locale.US, "%.1f", phaseStep)} ranhuras")
            appendLine("Ranhuras Estator: ${input.slotsCount} | Camada: ${if (input.isDoubleLayer) "Dupla" else "Simples"}")
            
            appendLine("------------------------------------")
            appendLine("DADOS DO BOBINADO:")
            appendLine("Espiras por Bobina: $turnsPerCoil espiras")
            appendLine("Condutores por Ranhura: $conductorsPerSlot")
            appendLine("Espiras Totais por Fase: $seriesTurnsPerPhase")
            appendLine("PESO ESTIMADO DE COBRE: ${String.format(Locale.US, "%.2f", estimatedCopperWeightKg)} kg")
            
            appendLine("------------------------------------")
            appendLine("CONDUTOR RECOMENDADO:")
            appendLine("Bitola: AWG ${recommendedWire.awg} (${recommendedWire.sectionMm2} mm²)")
            appendLine(String.format(Locale.US, "Densidade de Corrente Efetiva: %.2f A/mm²", actualCurrentDensity))
            if (parallelAlternative != null) {
                appendLine("Opção Prática (2 em paralelo): 2x AWG ${parallelAlternative.first.awg} (Seção Total: ${String.format(Locale.US, "%.3f", parallelAlternative.second)} mm²)")
            }
            appendLine("====================================")
        }

        return CalculationResult(
            nominalPowerWatts = powerWatts,
            nominalCurrentAmps = nominalCurrent,
            phaseCurrentAmps = phaseCurrent,
            conductorCurrentAmps = conductorCurrent,
            synchronousRpm = synchronousRpm,
            polePitchMm = polePitchMm,
            poleAreaCm2 = poleAreaCm2,
            magneticFluxWeber = magneticFluxWeber,
            magneticFluxMaxwell = magneticFluxMaxwell,
            phaseVoltageVolts = phaseVoltage,
            seriesTurnsPerPhase = seriesTurnsPerPhase,
            coilsPerPhase = coilsPerPhase,
            turnsPerCoil = turnsPerCoil,
            conductorsPerSlot = conductorsPerSlot,
            requiredWireSectionMm2 = requiredWireSectionMm2,
            recommendedWire = recommendedWire,
            actualCurrentDensity = actualCurrentDensity,
            parallelWireAlternative = parallelAlternative,
            connectionType = input.connectionType,
            phaseType = input.phaseType,
            estimatedCopperWeightKg = estimatedCopperWeightKg,
            coilsPerGroup = coilsPerGroup,
            phaseStep = phaseStep,
            copyableSummary = summary
        )
    }

    /**
     * Valida os parâmetros de entrada e os limites físicos/operacionais do motor.
     */
    fun validate(input: MotorInputData, result: CalculationResult?): ValidationReport {
        val items = mutableListOf<ValidationItem>()

        // Validação 1: Densidade de Corrente
        if (result != null) {
            if (result.actualCurrentDensity > 6.0) {
                items.add(
                    ValidationItem(
                        title = "Risco Crítico de Queima Térmica",
                        message = String.format(
                            Locale.US,
                            "Densidade de corrente de %.2f A/mm² excede o limite seguro de 6.0 A/mm². Risco de degradação acelerada do isolamento classe F/H e queima por sobreaquecimento.",
                            result.actualCurrentDensity
                        ),
                        severity = ValidationSeverity.CRITICAL_ERROR
                    )
                )
            } else if (result.actualCurrentDensity < 3.0) {
                items.add(
                    ValidationItem(
                        title = "Subdimensionamento de Cobre",
                        message = String.format(
                            Locale.US,
                            "Densidade de corrente de %.2f A/mm² está abaixo da faixa recomendada (3.5 - 5.5 A/mm²). O condutor pode não caber nas ranhuras ou gerar custo desnecessário.",
                            result.actualCurrentDensity
                        ),
                        severity = ValidationSeverity.WARNING
                    )
                )
            }
        }

        // Validação 2: Indução Magnética no Entreferro (Bmax)
        if (input.targetAirGapFluxDensity > 0.85) {
            items.add(
                ValidationItem(
                    title = "Saturação Magnética do Estator",
                    message = String.format(
                        Locale.US,
                        "Indução magnética configurada em %.2f T ultrapassa o joelho de saturação da chapa de aço silício (0.85 T). Causará corrente a vazio elevada e zumbido magnético excessivo.",
                        input.targetAirGapFluxDensity
                    ),
                    severity = ValidationSeverity.CRITICAL_ERROR
                )
            )
        } else if (input.targetAirGapFluxDensity < 0.55) {
            items.add(
                ValidationItem(
                    title = "Subutilização do Pacote Magnético",
                    message = String.format(
                        Locale.US,
                        "Indução de %.2f T é muito baixa (padrão industrial 0.65 - 0.80 T), exigindo número excessivo de espiras para atingir a fem induzida.",
                        input.targetAirGapFluxDensity
                    ),
                    severity = ValidationSeverity.WARNING
                )
            )
        }

        // Validação 3: Consistência Geométrica de Ranhuras e Polos
        val phasesCount = if (input.phaseType == MotorPhaseType.THREE_PHASE) 3 else 1
        val minSlotsRequired = input.poles * phasesCount
        if (input.slotsCount < minSlotsRequired) {
            items.add(
                ValidationItem(
                    title = "Geometria de Ranhuras Incompatível",
                    message = "Número de ranhuras (${input.slotsCount}) é insuficiente para acomodar ${input.poles} polos em $phasesCount fase(s). Mínimo requerido: $minSlotsRequired ranhuras.",
                    severity = ValidationSeverity.CRITICAL_ERROR
                )
            )
        } else {
            val slotsPerPolePerPhase = input.slotsCount.toDouble() / (input.poles * phasesCount)
            if (slotsPerPolePerPhase < 1.0) {
                items.add(
                    ValidationItem(
                        title = "Ranhuras por Polo e Fase Inválidas",
                        message = "q = $slotsPerPolePerPhase é menor que 1 ranhura por polo e fase, impedindo a montagem física do bobinado.",
                        severity = ValidationSeverity.CRITICAL_ERROR
                    )
                )
            }
        }

        // Validação adicional de dimensões mecânicas
        if (input.packageLengthMm <= 0 || input.statorInternalDiameterMm <= 0) {
            items.add(
                ValidationItem(
                    title = "Dimensões Físicas Inválidas",
                    message = "O comprimento do pacote e o diâmetro interno do estator devem ser maiores que zero.",
                    severity = ValidationSeverity.CRITICAL_ERROR
                )
            )
        }

        val hasCritical = items.any { it.severity == ValidationSeverity.CRITICAL_ERROR }
        return ValidationReport(isValid = !hasCritical, items = items)
    }
}
