package com.example.data.datasource

import com.example.domain.model.ConnectionType
import com.example.domain.model.FerragemInputData
import com.example.domain.model.MotorPhaseType

object FerragemPresets {

    // Carcaça 90L 4 Polos (3 a 5 CV típico)
    val carcase90L4p = FerragemInputData(
        internalDiameterMm = 105.0,
        packageLengthMm = 110.0,
        slotsCount = 36,
        slotDepthMm = 18.0,
        slotWidthTopMm = 8.5,
        slotWidthBottomMm = 6.0,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        inductionTesla = 0.72,
        targetCurrentDensity = 4.5,
        targetFillFactor = 0.38,
        windingFactor = 0.95,
        efficiency = 0.86,
        powerFactor = 0.82,
        isDoubleLayer = true
    )

    // Carcaça 80 2 Polos (1.5 a 2 CV típico - 3600 RPM)
    val carcase802p = FerragemInputData(
        internalDiameterMm = 85.0,
        packageLengthMm = 80.0,
        slotsCount = 24,
        slotDepthMm = 14.0,
        slotWidthTopMm = 7.0,
        slotWidthBottomMm = 5.0,
        poles = 2,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        inductionTesla = 0.70,
        targetCurrentDensity = 4.8,
        targetFillFactor = 0.38,
        windingFactor = 0.955,
        efficiency = 0.84,
        powerFactor = 0.85,
        isDoubleLayer = true
    )

    // Carcaça 100L 4 Polos (5 a 7.5 CV)
    val carcase100L4p = FerragemInputData(
        internalDiameterMm = 120.0,
        packageLengthMm = 130.0,
        slotsCount = 36,
        slotDepthMm = 21.0,
        slotWidthTopMm = 9.8,
        slotWidthBottomMm = 7.2,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        inductionTesla = 0.74,
        targetCurrentDensity = 4.5,
        targetFillFactor = 0.39,
        windingFactor = 0.95,
        efficiency = 0.88,
        powerFactor = 0.83,
        isDoubleLayer = true
    )

    // Ferragem Monofásica 4 Polos (1 CV típico)
    val carcaseMono4p = FerragemInputData(
        internalDiameterMm = 75.0,
        packageLengthMm = 70.0,
        slotsCount = 24,
        slotDepthMm = 13.0,
        slotWidthTopMm = 6.5,
        slotWidthBottomMm = 4.8,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.SINGLE_PHASE,
        connectionType = ConnectionType.DELTA,
        inductionTesla = 0.68,
        targetCurrentDensity = 4.2,
        targetFillFactor = 0.36,
        windingFactor = 0.92,
        efficiency = 0.72,
        powerFactor = 0.75,
        isDoubleLayer = false
    )

    // Preset Carcaça 63 (0.25 - 0.33 CV)
    val carcase63 = FerragemInputData(
        internalDiameterMm = 56.0,
        packageLengthMm = 45.0,
        slotsCount = 24,
        slotDepthMm = 10.5,
        slotWidthTopMm = 5.2,
        slotWidthBottomMm = 3.8,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.STAR,
        inductionTesla = 0.68,
        targetCurrentDensity = 5.0,
        targetFillFactor = 0.35,
        isDoubleLayer = true
    )

    // Preset Carcaça 71 (0.5 - 0.75 CV)
    val carcase71 = FerragemInputData(
        internalDiameterMm = 63.0,
        packageLengthMm = 55.0,
        slotsCount = 24,
        slotDepthMm = 12.0,
        slotWidthTopMm = 6.0,
        slotWidthBottomMm = 4.2,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.STAR,
        inductionTesla = 0.70,
        targetCurrentDensity = 4.8,
        targetFillFactor = 0.36,
        isDoubleLayer = true
    )

    // Preset Carcaça 112M (5 - 7.5 CV)
    val carcase112M = FerragemInputData(
        internalDiameterMm = 100.0,
        packageLengthMm = 110.0,
        slotsCount = 36,
        slotDepthMm = 18.5,
        slotWidthTopMm = 8.8,
        slotWidthBottomMm = 6.5,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        inductionTesla = 0.72,
        targetCurrentDensity = 4.5,
        targetFillFactor = 0.38,
        isDoubleLayer = true
    )

    // Preset Carcaça 132M (10 - 15 CV)
    val carcase132M = FerragemInputData(
        internalDiameterMm = 125.0,
        packageLengthMm = 145.0,
        slotsCount = 36,
        slotDepthMm = 22.0,
        slotWidthTopMm = 10.5,
        slotWidthBottomMm = 7.8,
        poles = 4,
        voltageVolts = 220.0,
        frequencyHz = 60.0,
        phaseType = MotorPhaseType.THREE_PHASE,
        connectionType = ConnectionType.DELTA,
        inductionTesla = 0.74,
        targetCurrentDensity = 4.5,
        targetFillFactor = 0.40,
        isDoubleLayer = true
    )
}
