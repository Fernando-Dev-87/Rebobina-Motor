package com.example.ui

import androidx.lifecycle.ViewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.FerragemPresets
import com.example.data.local.AppDatabase
import com.example.data.model.RewindService
import com.example.domain.calculator.FerragemEngine
import com.example.domain.model.ConnectionType
import com.example.domain.model.FerragemInputData
import com.example.domain.model.MotorPhaseType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FerragemViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val rewindDao = db.rewindDao()

    private val _uiState = MutableStateFlow(FerragemUiState())
    val uiState: StateFlow<FerragemUiState> = _uiState.asStateFlow()

    init {
        recalculate()
    }

    fun updateInternalDiameter(value: String) {
        _uiState.update { it.copy(internalDiameterInput = value) }
        recalculate()
    }

    fun updatePackageLength(value: String) {
        _uiState.update { it.copy(packageLengthInput = value) }
        recalculate()
    }

    fun updateSlots(value: String) {
        _uiState.update { it.copy(slotsInput = value) }
        recalculate()
    }

    fun updateSlotDepth(value: String) {
        _uiState.update { it.copy(slotDepthInput = value) }
        recalculate()
    }

    fun updateSlotWidthTop(value: String) {
        _uiState.update { it.copy(slotWidthTopInput = value) }
        recalculate()
    }

    fun updateSlotWidthBottom(value: String) {
        _uiState.update { it.copy(slotWidthBottomInput = value) }
        recalculate()
    }

    fun updatePoles(poles: Int) {
        _uiState.update { it.copy(poles = poles) }
        recalculate()
    }

    fun updateVoltage(value: String) {
        _uiState.update { it.copy(voltageInput = value) }
        recalculate()
    }

    fun updatePhaseType(phaseType: MotorPhaseType) {
        _uiState.update { it.copy(phaseType = phaseType) }
        recalculate()
    }

    fun updateConnectionType(connectionType: ConnectionType) {
        _uiState.update { it.copy(connectionType = connectionType) }
        recalculate()
    }

    fun updateInduction(value: String) {
        _uiState.update { it.copy(inductionTeslaInput = value) }
        recalculate()
    }

    fun updateTargetCurrentDensity(value: String) {
        _uiState.update { it.copy(targetCurrentDensityInput = value) }
        recalculate()
    }

    fun updateTargetFillFactor(value: String) {
        _uiState.update { it.copy(targetFillFactorInput = value) }
        recalculate()
    }

    fun updateDoubleLayer(isDoubleLayer: Boolean) {
        _uiState.update { it.copy(isDoubleLayer = isDoubleLayer) }
        recalculate()
    }

    fun toggleAdvanced() {
        _uiState.update { it.copy(isAdvancedOpen = !it.isAdvancedOpen) }
    }

    fun applyPreset(preset: FerragemInputData) {
        _uiState.update {
            it.copy(
                internalDiameterInput = preset.internalDiameterMm.toString(),
                packageLengthInput = preset.packageLengthMm.toString(),
                slotsInput = preset.slotsCount.toString(),
                slotDepthInput = preset.slotDepthMm.toString(),
                slotWidthTopInput = preset.slotWidthTopMm.toString(),
                slotWidthBottomInput = preset.slotWidthBottomMm.toString(),
                poles = preset.poles,
                voltageInput = preset.voltageVolts.toString(),
                phaseType = preset.phaseType,
                connectionType = preset.connectionType,
                inductionTeslaInput = preset.inductionTesla.toString(),
                targetCurrentDensityInput = preset.targetCurrentDensity.toString(),
                targetFillFactorInput = (preset.targetFillFactor * 100.0).toInt().toString(),
                isDoubleLayer = preset.isDoubleLayer
            )
        }
        recalculate()
    }

    fun applyPreset90L() = applyPreset(FerragemPresets.carcase90L4p)
    fun applyPreset80() = applyPreset(FerragemPresets.carcase802p)
    fun applyPreset100L() = applyPreset(FerragemPresets.carcase100L4p)
    fun applyPresetMono() = applyPreset(FerragemPresets.carcaseMono4p)
    fun applyPreset63() = applyPreset(FerragemPresets.carcase63)
    fun applyPreset71() = applyPreset(FerragemPresets.carcase71)
    fun applyPreset112M() = applyPreset(FerragemPresets.carcase112M)
    fun applyPreset132M() = applyPreset(FerragemPresets.carcase132M)

    fun notifyCopied() {
        _uiState.update { it.copy(copiedFeedback = true) }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { it.copy(copiedFeedback = false) }
        }
    }

    fun saveCalculation(clientName: String) {
        val result = _uiState.value.result ?: return
        val motorDesc = "Ferragem D=${_uiState.value.internalDiameterInput} L=${_uiState.value.packageLengthInput}"
        
        viewModelScope.launch {
            rewindDao.insertService(
                RewindService(
                    clientName = clientName,
                    motorDescription = motorDesc,
                    technicalSummary = result.copyableSummary,
                    isFerragem = true,
                    powerCv = result.estimatedPowerCv,
                    rpm = result.synchronousRpm,
                    voltage = _uiState.value.voltageInput.toDoubleOrNull() ?: 0.0
                )
            )
        }
    }

    private fun recalculate() {
        val s = _uiState.value
        val d = s.internalDiameterInput.toDoubleOrNull() ?: 105.0
        val l = s.packageLengthInput.toDoubleOrNull() ?: 110.0
        val slots = s.slotsInput.toIntOrNull() ?: 36
        val depth = s.slotDepthInput.toDoubleOrNull() ?: 18.0
        val wTop = s.slotWidthTopInput.toDoubleOrNull() ?: 8.5
        val wBottom = s.slotWidthBottomInput.toDoubleOrNull() ?: 6.0
        val volt = s.voltageInput.toDoubleOrNull() ?: 220.0
        val freq = s.frequencyInput.toDoubleOrNull() ?: 60.0
        val induction = s.inductionTeslaInput.toDoubleOrNull() ?: 0.72
        val jTarget = s.targetCurrentDensityInput.toDoubleOrNull() ?: 4.5
        val fillFactorPercent = s.targetFillFactorInput.toDoubleOrNull() ?: 38.0
        val fillFactor = (fillFactorPercent / 100.0).coerceIn(0.15, 0.60)

        val input = FerragemInputData(
            internalDiameterMm = d,
            packageLengthMm = l,
            slotsCount = slots,
            slotDepthMm = depth,
            slotWidthTopMm = wTop,
            slotWidthBottomMm = wBottom,
            poles = s.poles,
            voltageVolts = volt,
            frequencyHz = freq,
            phaseType = s.phaseType,
            connectionType = s.connectionType,
            inductionTesla = induction,
            targetCurrentDensity = jTarget,
            targetFillFactor = fillFactor,
            isDoubleLayer = s.isDoubleLayer
        )

        val result = try {
            FerragemEngine.calculate(input)
        } catch (_: Exception) {
            null
        }

        val report = FerragemEngine.validate(input, result)

        _uiState.update {
            it.copy(
                result = result,
                validationReport = report
            )
        }
    }
}
