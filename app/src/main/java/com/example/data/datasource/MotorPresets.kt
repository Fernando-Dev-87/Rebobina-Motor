package com.example.data.datasource

import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.PowerUnit

object MotorPresets {
    val weg5cv4p = MotorInputData(
        powerValue = 5.0,
        powerUnit = PowerUnit.CV,
        voltageVolts = 220.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        poles = 4,
        frequencyHz = 60.0,
        slotsCount = 36,
        packageLengthMm = 110.0,
        statorInternalDiameterMm = 105.0,
        targetCurrentDensity = 4.5,
        targetAirGapFluxDensity = 0.72,
        windingFactor = 0.95,
        efficiency = 0.885,
        powerFactor = 0.81,
        isDoubleLayer = true
    )

    val motor2cv2p = MotorInputData(
        powerValue = 2.0,
        powerUnit = PowerUnit.CV,
        voltageVolts = 220.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        poles = 2,
        frequencyHz = 60.0,
        slotsCount = 24,
        packageLengthMm = 80.0,
        statorInternalDiameterMm = 85.0,
        targetCurrentDensity = 4.8,
        targetAirGapFluxDensity = 0.70,
        windingFactor = 0.955,
        efficiency = 0.84,
        powerFactor = 0.85,
        isDoubleLayer = true
    )

    val singlePhase1cv = MotorInputData(
        powerValue = 1.0,
        powerUnit = PowerUnit.CV,
        voltageVolts = 220.0,
        phaseType = MotorPhaseType.SINGLE_PHASE,
        connectionType = ConnectionType.DELTA,
        poles = 4,
        frequencyHz = 60.0,
        slotsCount = 24,
        packageLengthMm = 70.0,
        statorInternalDiameterMm = 75.0,
        targetCurrentDensity = 4.2,
        targetAirGapFluxDensity = 0.68,
        windingFactor = 0.92,
        efficiency = 0.72,
        powerFactor = 0.75,
        isDoubleLayer = false
    )
}
