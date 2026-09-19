package com.example.ui

import com.example.domain.model.ConnectionType
import com.example.domain.model.FerragemCalculationResult
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.ValidationReport

data class FerragemUiState(
    val internalDiameterInput: String = "105.0",
    val packageLengthInput: String = "110.0",
    val slotsInput: String = "36",
    val slotDepthInput: String = "18.0",
    val slotWidthTopInput: String = "8.5",
    val slotWidthBottomInput: String = "6.0",
    val poles: Int = 4,
    val voltageInput: String = "220.0",
    val frequencyInput: String = "60.0",
    val phaseType: MotorPhaseType = MotorPhaseType.THREE_PHASE,
    val connectionType: ConnectionType = ConnectionType.DELTA,
    val inductionTeslaInput: String = "0.72",
    val targetCurrentDensityInput: String = "4.5",
    val targetFillFactorInput: String = "38", // em porcentagem 38%
    val isDoubleLayer: Boolean = true,
    val isAdvancedOpen: Boolean = false,
    val result: FerragemCalculationResult? = null,
    val validationReport: ValidationReport = ValidationReport(isValid = true, items = emptyList()),
    val copiedFeedback: Boolean = false,
    val copperPriceInput: String = "85.00"
)
