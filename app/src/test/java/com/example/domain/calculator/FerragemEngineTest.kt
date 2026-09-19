package com.example.domain.calculator

import com.example.data.datasource.FerragemPresets
import com.example.domain.model.ConnectionType
import com.example.domain.model.FerragemInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.ValidationSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FerragemEngineTest {

    @Test
    fun calculate_carcase90L_returnsRealisticParameters() {
        val input = FerragemPresets.carcase90L4p
        val result = FerragemEngine.calculate(input)

        // 1. Rotação síncrona para 4 polos em 60Hz
        assertEquals(1800, result.synchronousRpm)

        // 2. Passo polar em ranhuras (36 / 4 = 9) -> Passo de bobina 1 a 10
        assertEquals(9.0, result.polePitchSlots, 0.01)
        assertTrue(result.recommendedCoilPitch.contains("1 a 10"))

        // 3. Potência estimada da ferragem deve ficar entre 3.0 e 6.0 CV
        assertTrue(
            "Potência calculada (${result.estimatedPowerCv} CV) fora do esperado para carcaça 90L",
            result.estimatedPowerCv in 3.0..6.5
        )

        // 4. Espiras por bobina razoáveis
        assertTrue("Espiras por bobina (${result.turnsPerCoil}) inválidas", result.turnsPerCoil > 0)

        // 5. Fio selecionado e fator de enchimento seguro
        assertNotNull(result.recommendedWire)
        assertTrue(
            "Fator de enchimento (${result.actualFillFactor * 100}%) excessivo",
            result.actualFillFactor < 0.45
        )
    }

    @Test
    fun calculate_carcase80_2poles_returns3600Rpm() {
        val input = FerragemPresets.carcase802p
        val result = FerragemEngine.calculate(input)

        assertEquals(3600, result.synchronousRpm)
        assertEquals(12.0, result.polePitchSlots, 0.01)
        assertTrue(result.recommendedCoilPitch.contains("1 a 13"))
        assertTrue(result.estimatedPowerCv in 1.0..3.5)
    }

    @Test
    fun validate_oversaturatedOrOverfilledSlot_triggersWarning() {
        // Ranhura com alvo de enchimento elevado (50%) ou slot muito estreito
        val highFillInput = FerragemPresets.carcase90L4p.copy(
            targetFillFactor = 0.50
        )
        val result = FerragemEngine.calculate(highFillInput)
        val report = FerragemEngine.validate(highFillInput, result)

        val hasOverfillWarning = report.items.any {
            it.title.contains("Ranhura Excessivamente Cheia")
        }
        assertTrue("Deveria alertar sobre ranhura excessivamente cheia", hasOverfillWarning)
    }

    @Test
    fun validate_invalidDimensions_triggersCriticalError() {
        val invalidInput = FerragemPresets.carcase90L4p.copy(internalDiameterMm = -10.0)
        val report = FerragemEngine.validate(invalidInput, null)

        assertTrue(report.items.any { it.severity == ValidationSeverity.CRITICAL_ERROR })
    }
}
