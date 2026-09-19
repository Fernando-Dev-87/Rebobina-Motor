package com.example

import com.example.data.datasource.MotorPresets
import com.example.domain.calculator.MotorRewindEngine
import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.PowerUnit
import com.example.domain.model.ValidationSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorRewindEngineTest {

    /**
     * CENÁRIO 1: Validação com Motor Trifásico Padrão WEG W22 5 CV / 4 Polos
     * Entrada:
     * - Potência: 5 CV (3677.5 W)
     * - Tensão: 220 V (Triângulo Δ)
     * - Polos: 4 (1800 rpm síncrono a 60 Hz)
     * - Ranhuras: 36
     * - Comprimento Pacote (L): 110 mm
     * - Diâmetro Interno Estator (D): 105 mm
     * - Rendimento (η): 0.885 (88.5%)
     * - Fator de Potência (cos φ): 0.81
     * - Densidade alvo (J): 4.5 A/mm²
     * - Indução no entreferro (Bmax): 0.72 T
     *
     * Saída Esperada:
     * - Corrente Nominal In: ~ 13.5 A
     * - Corrente de Fase If: ~ 7.8 A
     * - Rotação Síncrona: 1800 RPM
     * - Fluxo por polo Φ: ~ 0.0041 Wb
     * - Fio AWG Recomendado: AWG 15 ou AWG 16 (Seção ~ 1.73 mm²)
     * - Opção Paralela: 2x AWG 18 ou 2x AWG 19
     * - Densidade de Corrente segura entre 3.5 e 5.5 A/mm²
     */
    @Test
    fun testWeg5Cv4PolesScenario() {
        val input = MotorPresets.weg5cv4p
        val result = MotorRewindEngine.calculate(input)

        // Verificação da Corrente Nominal: In = P / (sqrt(3) * V * η * cos φ)
        // 3677.5 / (1.73205 * 220 * 0.885 * 0.81) = 3677.5 / 272.96 = ~13.47 A
        assertEquals(13.47, result.nominalCurrentAmps, 0.2)

        // Corrente de fase na ligação triângulo: If = In / sqrt(3) = 13.47 / 1.732 = ~7.78 A
        assertEquals(7.78, result.phaseCurrentAmps, 0.2)

        // Rotação síncrona
        assertEquals(1800, result.synchronousRpm)

        // Fluxo Magnético por Polo (Φ): Bmédio * Ap = (2/π) * 0.72 * (0.08246 m * 0.11 m) = ~0.00416 Wb
        assertEquals(0.00416, result.magneticFluxWeber, 0.0003)

        // Seção teórica requerida: If / J = 7.78 / 4.5 = ~1.73 mm²
        assertEquals(1.73, result.requiredWireSectionMm2, 0.1)

        // Fio selecionado deve ser AWG 15 (1.65 mm²) ou AWG 14 (2.08 mm²)
        assertTrue(result.recommendedWire.awg in listOf(14, 15, 16))

        // Densidade de corrente real deve estar na faixa segura (< 6.0 A/mm²)
        assertTrue(result.actualCurrentDensity < 6.0)
        assertTrue(result.actualCurrentDensity > 3.0)

        // Validação sem erros críticos
        val validation = MotorRewindEngine.validate(input, result)
        assertTrue("Não deve conter erros críticos", validation.isValid)
    }

    /**
     * CENÁRIO 2: Motor Trifásico 2 CV / 2 Polos (3600 rpm)
     * Entrada:
     * - Potência: 2 CV (1471 W)
     * - Tensão: 220 V (Triângulo Δ)
     * - Polos: 2 (3600 rpm síncrono a 60 Hz)
     * - Ranhuras: 24
     * - Comprimento Pacote (L): 80 mm
     * - Diâmetro Interno Estator (D): 85 mm
     * - Rendimento (η): 0.84
     * - Fator de Potência (cos φ): 0.85
     *
     * Saída Esperada:
     * - Corrente Nominal In: ~ 5.4 A
     * - Corrente de Fase If: ~ 3.12 A
     * - Rotação Síncrona: 3600 RPM
     * - Fio AWG Recomendado: AWG 19 ou AWG 20 (Seção ~ 0.65 mm²)
     */
    @Test
    fun testMotor2Cv2PolesScenario() {
        val input = MotorPresets.motor2cv2p
        val result = MotorRewindEngine.calculate(input)

        // Corrente Nominal: 1471 / (sqrt(3) * 220 * 0.84 * 0.85) = 1471 / 271.9 = ~5.41 A
        assertEquals(5.41, result.nominalCurrentAmps, 0.2)

        // Corrente de Fase (Triângulo): 5.41 / 1.732 = ~3.12 A
        assertEquals(3.12, result.phaseCurrentAmps, 0.2)

        // Rotação Síncrona 2 polos
        assertEquals(3600, result.synchronousRpm)

        // Fio selecionado deve ser AWG 19 (0.653 mm²) ou AWG 20 (0.518 mm²)
        assertTrue(result.recommendedWire.awg in listOf(18, 19, 20))

        // Densidade real segura
        assertTrue(result.actualCurrentDensity < 6.0)
    }

    /**
     * TESTE DE REGRA DE SEGURANÇA: Disparo de Alerta de Risco de Queima quando J > 6 A/mm²
     */
    @Test
    fun testBurnRiskValidationRuleTrigger() {
        val input = MotorPresets.weg5cv4p.copy(
            targetCurrentDensity = 7.5 // Valor excessivo e perigoso
        )
        val result = MotorRewindEngine.calculate(input)
        val validation = MotorRewindEngine.validate(input, result)

        val burnWarning = validation.items.find { it.title.contains("Queima", ignoreCase = true) }
        assertNotNull("Deve disparar aviso de risco de queima", burnWarning)
        assertEquals(ValidationSeverity.CRITICAL_ERROR, burnWarning?.severity)
    }
}
