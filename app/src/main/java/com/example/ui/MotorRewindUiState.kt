package com.example.ui

import com.example.domain.model.CalculationResult
import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.PowerUnit
import com.example.domain.model.ValidationReport

data class MotorRewindUiState(
    val powerInput: String = "5.0",
    val powerUnit: PowerUnit = PowerUnit.CV,
    val voltageInput: String = "220",
    val phaseType: MotorPhaseType = MotorPhaseType.THREE_PHASE,
    val connectionType: ConnectionType = ConnectionType.DELTA,
    val poles: Int = 4,
    val rpmInput: String = "1800",
    val frequencyInput: String = "60",
    val slotsInput: String = "36",
    val packageLengthInput: String = "110",
    val statorDiameterInput: String = "105",
    val currentDensityTargetInput: String = "4.5",
    val airGapFluxDensityInput: String = "0.72",
    val efficiencyInput: String = "0.885",
    val powerFactorInput: String = "0.81",
    val isDoubleLayer: Boolean = true,
    val result: CalculationResult? = null,
    val validationReport: ValidationReport = ValidationReport(isValid = true, items = emptyList()),
    val isAdvancedOpen: Boolean = false,
    val copiedFeedback: Boolean = false
) {
    fun toDomainInput(): MotorInputData {
        return MotorInputData(
            powerValue = powerInput.toDoubleOrNull() ?: 5.0,
            powerUnit = powerUnit,
            voltageVolts = voltageInput.toDoubleOrNull() ?: 220.0,
            phaseType = phaseType,
            connectionType = connectionType,
            poles = poles,
            frequencyHz = frequencyInput.toDoubleOrNull() ?: 60.0,
            slotsCount = slotsInput.toIntOrNull() ?: 36,
            packageLengthMm = packageLengthInput.toDoubleOrNull() ?: 110.0,
            statorInternalDiameterMm = statorDiameterInput.toDoubleOrNull() ?: 105.0,
            targetCurrentDensity = currentDensityTargetInput.toDoubleOrNull() ?: 4.5,
            targetAirGapFluxDensity = airGapFluxDensityInput.toDoubleOrNull() ?: 0.72,
            efficiency = efficiencyInput.toDoubleOrNull() ?: 0.885,
            powerFactor = powerFactorInput.toDoubleOrNull() ?: 0.81,
            isDoubleLayer = isDoubleLayer
        )
    }
}
