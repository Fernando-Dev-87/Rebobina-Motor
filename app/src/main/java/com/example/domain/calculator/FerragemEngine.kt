package com.example.domain.calculator

import com.example.data.datasource.AwgTable
import com.example.domain.model.ConnectionType
import com.example.domain.model.FerragemCalculationResult
import com.example.domain.model.FerragemInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.ValidationItem
import com.example.domain.model.ValidationReport
import com.example.domain.model.ValidationSeverity
import java.util.Locale
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sqrt

object FerragemEngine {

    /**
     * Calcula os dados de enrolamento de um motor a partir das medidas da ferragem
     * do estator, seguindo a metodologia da Apostila 018/09 (Iltonn).
     */
    fun calculate(input: FerragemInputData): FerragemCalculationResult {
        // 1. Rotação Síncrona
        val synchronousRpm = ((120.0 * input.frequencyHz) / input.poles).roundToInt()

        // 2. Geometria Polar da Ferragem
        val polePitchMm = (PI * input.internalDiameterMm) / input.poles
        val polePitchSlots = input.slotsCount.toDouble() / input.poles
        val coilPitchEnd = 1 + polePitchSlots.roundToInt()
        val recommendedCoilPitch = "1 a $coilPitchEnd (encurtado: 1 a ${coilPitchEnd - 1})"

        // Área Polar Ap em cm² (usada nos cálculos clássicos da Apostila 018/09)
        val polePitchCm = polePitchMm / 10.0
        val packageLengthCm = input.packageLengthMm / 10.0
        val poleAreaCm2 = polePitchCm * packageLengthCm

        // 3. Fluxo Magnético por Polo (Φ)
        // B em Gauss = B(Tesla) * 10.000
        val fluxDensityGauss = input.inductionTesla * 10_000.0
        // Φ (Maxwell) = B(Gauss) * Ap(cm²) * (2/π)
        val magneticFluxMaxwell = fluxDensityGauss * poleAreaCm2 * (2.0 / PI)
        val magneticFluxWeber = magneticFluxMaxwell / 100_000_000.0

        // 4. Tensão de Fase e FEM Induzida (Ef)
        val phaseVoltage = when (input.phaseType) {
            MotorPhaseType.THREE_PHASE -> when (input.connectionType) {
                ConnectionType.DELTA -> input.voltageVolts
                ConnectionType.STAR -> input.voltageVolts / sqrt(3.0)
            }
            MotorPhaseType.SINGLE_PHASE -> input.voltageVolts
        }
        val inducedEmf = 0.95 * phaseVoltage

        // 5. Espiras em Série por Fase (Nf) - Fórmula fundamental da Apostila 018/09
        // Nf = (Ef * 10^8) / (4.44 * f * Φ_Maxwell * kw)
        val seriesTurnsPerPhaseRaw = (inducedEmf * 100_000_000.0) /
                (4.44 * input.frequencyHz * magneticFluxMaxwell * input.windingFactor)
        val seriesTurnsPerPhase = seriesTurnsPerPhaseRaw.roundToInt().coerceAtLeast(1)

        // 6. Espiras por Bobina (Nb) e Condutores por Ranhura (Zr)
        val phasesCount = if (input.phaseType == MotorPhaseType.THREE_PHASE) 3 else 1
        val coilsPerPhase = if (input.isDoubleLayer) {
            (input.slotsCount.toDouble() / phasesCount).roundToInt().coerceAtLeast(1)
        } else {
            (input.slotsCount.toDouble() / (2.0 * phasesCount)).roundToInt().coerceAtLeast(1)
        }

        val turnsPerCoilRaw = seriesTurnsPerPhase.toDouble() / coilsPerPhase
        val turnsPerCoil = turnsPerCoilRaw.roundToInt().coerceAtLeast(1)

        val conductorsPerSlot = if (input.isDoubleLayer) {
            turnsPerCoil * 2
        } else {
            turnsPerCoil
        }

        // 7. Área da Ranhura e Dimensionamento da Bitola (AWG da Ferragem)
        val slotAreaMm2 = ((input.slotWidthTopMm + input.slotWidthBottomMm) / 2.0) * input.slotDepthMm
        val copperAreaPerSlotMm2 = slotAreaMm2 * input.targetFillFactor

        // Seção máxima de cobre por condutor que a ranhura suporta
        val maxWireSectionBySlotMm2 = if (conductorsPerSlot > 0) {
            copperAreaPerSlotMm2 / conductorsPerSlot
        } else {
            0.5
        }

        // Fio recomendado da tabela AWG
        val recommendedWire = AwgTable.findClosestSingleWire(maxWireSectionBySlotMm2)

        // Fator de enchimento real com o fio adotado
        val actualFillFactor = (conductorsPerSlot * recommendedWire.sectionMm2) / slotAreaMm2

        // 8. Capacidade de Corrente e Potência Estimada Suportada pela Ferragem
        // Corrente de fase admitida pelo fio AWG na densidade J alvo
        val phaseCurrent = recommendedWire.sectionMm2 * input.targetCurrentDensity
        val actualCurrentDensity = phaseCurrent / recommendedWire.sectionMm2

        val nominalCurrent = when (input.phaseType) {
            MotorPhaseType.THREE_PHASE -> when (input.connectionType) {
                ConnectionType.DELTA -> phaseCurrent * sqrt(3.0)
                ConnectionType.STAR -> phaseCurrent
            }
            MotorPhaseType.SINGLE_PHASE -> phaseCurrent
        }

        // Potência mecânica útil calculada: P = sqrt(3) * V * In * η * cos φ
        val estimatedPowerWatts = when (input.phaseType) {
            MotorPhaseType.THREE_PHASE -> sqrt(3.0) * input.voltageVolts * nominalCurrent * input.efficiency * input.powerFactor
            MotorPhaseType.SINGLE_PHASE -> input.voltageVolts * nominalCurrent * input.efficiency * input.powerFactor
        }
        val estimatedPowerCv = estimatedPowerWatts / 735.5
        val estimatedPowerKw = estimatedPowerWatts / 1000.0

        // Alternativa em 2 fios em paralelo para enrolamento manual mais macio
        val parallelAlternative = if (recommendedWire.sectionMm2 >= 0.8) {
            AwgTable.findTwoParallelWires(recommendedWire.sectionMm2)
        } else {
            null
        }

        // 9. Inteligência Manual WEG: Padrões de Bobinagem
        val coilsPerGroup = WegStandards.calculateCoilsPerGroup(input.slotsCount, input.poles, phasesCount)
        val phaseStep = WegStandards.calculatePhaseStep(input.slotsCount, input.poles)

        // 10. Estimativa de Peso de Cobre (kg)
        val estimatedCopperWeightKg = ConversionEngine.calculateCopperWeight(
            internalDiameterMm = input.internalDiameterMm,
            packageLengthMm = input.packageLengthMm,
            poles = input.poles,
            totalSlots = input.slotsCount,
            conductorsPerSlot = conductorsPerSlot,
            wireSectionMm2 = recommendedWire.sectionMm2
        )

        // Resumo de oficina para cópia de bancada
        val summary = buildString {
            appendLine("=== CÁLCULO A PARTIR DA FERRAGEM (PADRÃO WEG/IEC) ===")
            appendLine("Ferragem: D=${input.internalDiameterMm.roundToInt()} mm | L=${input.packageLengthMm.roundToInt()} mm | Ranhuras: ${input.slotsCount}")
            appendLine("Ranhura: Área Calc: ${String.format(Locale.US, "%.1f", slotAreaMm2)} mm²")
            appendLine(String.format(Locale.US, "POTÊNCIA ESTIMADA DA CARCAÇA: %.1f CV (%.2f kW)", estimatedPowerCv, estimatedPowerKw))
            appendLine("Tensão: ${input.voltageVolts.roundToInt()} V | Ligação: ${input.connectionType.symbol}")
            appendLine("Polos: ${input.poles}P (${synchronousRpm} RPM a ${input.frequencyHz.roundToInt()}Hz)")
            
            appendLine("--------------------------------------------------")
            appendLine("ESQUEMA DE BOBINAGEM (MANUAL WEG):")
            appendLine("Bobinas por Grupo: ${if (coilsPerGroup % 1.0 == 0.0) coilsPerGroup.toInt().toString() else String.format(Locale.US, "%.1f", coilsPerGroup)}")
            appendLine("Passo de Fase (Defasagem): ${String.format(Locale.US, "%.1f", phaseStep)} ranhuras")
            appendLine("Passo Polar: ${String.format(Locale.US, "%.1f", polePitchSlots)} | Passo Recomendado: $recommendedCoilPitch")
            
            appendLine("--------------------------------------------------")
            appendLine("DADOS DO BOBINADO:")
            appendLine("Espiras por Bobina: $turnsPerCoil espiras (${if (input.isDoubleLayer) "Camada Dupla" else "Camada Simples"})")
            appendLine("Condutores por Ranhura: $conductorsPerSlot fios")
            appendLine("Espiras Totais em Série por Fase: $seriesTurnsPerPhase")
            appendLine("PESO ESTIMADO DE COBRE: ${String.format(Locale.US, "%.2f", estimatedCopperWeightKg)} kg")
            
            appendLine("--------------------------------------------------")
            appendLine("CONDUTOR RECOMENDADO:")
            appendLine("Fio: AWG ${recommendedWire.awg} (Seção: ${recommendedWire.sectionMm2} mm²)")
            appendLine(String.format(Locale.US, "Fator de Enchimento Real: %.1f%% (Limite: 45%%)", actualFillFactor * 100.0))
            appendLine(String.format(Locale.US, "Densidade de Corrente: %.2f A/mm²", actualCurrentDensity))
            if (parallelAlternative != null) {
                appendLine("Opção Prática (2 fios): 2x AWG ${parallelAlternative.first.awg} (Tot: ${String.format(Locale.US, "%.3f", parallelAlternative.second)} mm²)")
            }
            appendLine("==================================================")
        }

        return FerragemCalculationResult(
            estimatedPowerWatts = estimatedPowerWatts,
            estimatedPowerCv = estimatedPowerCv,
            estimatedPowerKw = estimatedPowerKw,
            nominalCurrentAmps = nominalCurrent,
            phaseCurrentAmps = phaseCurrent,
            synchronousRpm = synchronousRpm,
            polePitchMm = polePitchMm,
            polePitchSlots = polePitchSlots,
            recommendedCoilPitch = recommendedCoilPitch,
            poleAreaCm2 = poleAreaCm2,
            magneticFluxWeber = magneticFluxWeber,
            magneticFluxMaxwell = magneticFluxMaxwell,
            phaseVoltageVolts = phaseVoltage,
            inducedEmfVolts = inducedEmf,
            seriesTurnsPerPhase = seriesTurnsPerPhase,
            coilsPerPhase = coilsPerPhase,
            turnsPerCoil = turnsPerCoil,
            conductorsPerSlot = conductorsPerSlot,
            slotAreaMm2 = slotAreaMm2,
            copperAreaPerSlotMm2 = copperAreaPerSlotMm2,
            maxWireSectionBySlotMm2 = maxWireSectionBySlotMm2,
            recommendedWire = recommendedWire,
            actualFillFactor = actualFillFactor,
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
     * Valida os limites mecânicos e elétricos da ferragem conforme diretrizes de bobinagem.
     */
    fun validate(input: FerragemInputData, result: FerragemCalculationResult?): ValidationReport {
        val items = mutableListOf<ValidationItem>()

        if (result != null) {
            // Validação de Enchimento da Ranhura
            if (result.actualFillFactor > 0.45) {
                items.add(
                    ValidationItem(
                        title = "Ranhura Excessivamente Cheia (> 45%)",
                        message = String.format(
                            Locale.US,
                            "Fator de enchimento de %.1f%% dificulta ou impossibilita a inserção manual dos condutores e da cunha de fechamento. Recomenda-se reduzir uma bitola AWG ou ajustar espiras.",
                            result.actualFillFactor * 100.0
                        ),
                        severity = ValidationSeverity.WARNING
                    )
                )
            } else if (result.actualFillFactor < 0.25) {
                items.add(
                    ValidationItem(
                        title = "Subaproveitamento da Ranhura (< 25%)",
                        message = String.format(
                            Locale.US,
                            "Fator de enchimento de apenas %.1f%% deixa muito espaço vazio na ranhura, reduzindo a potência que a ferragem poderia entregar.",
                            result.actualFillFactor * 100.0
                        ),
                        severity = ValidationSeverity.WARNING
                    )
                )
            }

            // Validação de Densidade de Corrente
            if (result.actualCurrentDensity > 6.0) {
                items.add(
                    ValidationItem(
                        title = "Risco de Queima Térmica",
                        message = String.format(
                            Locale.US,
                            "Densidade de %.2f A/mm² excede o limite térmico seguro de 6.0 A/mm² para enrolamentos classe F.",
                            result.actualCurrentDensity
                        ),
                        severity = ValidationSeverity.CRITICAL_ERROR
                    )
                )
            }
        }

        // Validação da Ferragem (Geometria)
        if (input.internalDiameterMm <= 0 || input.packageLengthMm <= 0) {
            items.add(
                ValidationItem(
                    title = "Medidas da Ferragem Inválidas",
                    message = "O diâmetro interno e o comprimento do pacote devem ser maiores que zero.",
                    severity = ValidationSeverity.CRITICAL_ERROR
                )
            )
        }

        val phasesCount = if (input.phaseType == MotorPhaseType.THREE_PHASE) 3 else 1
        if (input.slotsCount < input.poles * phasesCount) {
            items.add(
                ValidationItem(
                    title = "Ranhuras Insuficientes",
                    message = "A ferragem de ${input.slotsCount} ranhuras não comporta ${input.poles} polos em $phasesCount fase(s). Mínimo necessário: ${input.poles * phasesCount} ranhuras.",
                    severity = ValidationSeverity.CRITICAL_ERROR
                )
            )
        }

        val hasCritical = items.any { it.severity == ValidationSeverity.CRITICAL_ERROR }
        return ValidationReport(isValid = !hasCritical, items = items)
    }
}
