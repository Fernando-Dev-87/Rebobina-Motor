package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.MotorPresets
import com.example.data.datasource.MotorPresetsCatalog
import com.example.domain.calculator.MotorRewindEngine
import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorInputData
import com.example.domain.model.MotorPhaseType
import com.example.domain.model.PowerUnit
import com.example.data.local.AppDatabase
import com.example.data.model.RewindService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.util.Locale

class MotorRewindViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val rewindDao = db.rewindDao()

    private val _uiState = MutableStateFlow(MotorRewindUiState())
    val uiState: StateFlow<MotorRewindUiState> = _uiState.asStateFlow()

    init {
        // Realiza o cálculo inicial com os dados padrão (Motor WEG 5 CV)
        calculatePreset()
    }

    fun updatePower(value: String) {
        val cv = value.toDoubleOrNull() ?: 0.0
        val kw = cv * 0.7355
        val hp = cv * 0.9863
        _uiState.update { it.copy(
            powerInput = value,
            kwInput = String.format(Locale.US, "%.2f", kw),
            hpInput = String.format(Locale.US, "%.2f", hp)
        ) }
        recalculate()
    }

    fun updateKw(value: String) {
        val kw = value.toDoubleOrNull() ?: 0.0
        val cv = kw / 0.7355
        val hp = cv * 0.9863
        _uiState.update { it.copy(
            kwInput = value,
            powerInput = String.format(Locale.US, "%.2f", cv),
            hpInput = String.format(Locale.US, "%.2f", hp)
        ) }
        recalculate()
    }

    fun updateHp(value: String) {
        val hp = value.toDoubleOrNull() ?: 0.0
        val cv = hp / 0.9863
        val kw = cv * 0.7355
        _uiState.update { it.copy(
            hpInput = value,
            powerInput = String.format(Locale.US, "%.2f", cv),
            kwInput = String.format(Locale.US, "%.2f", kw)
        ) }
        recalculate()
    }

    fun updatePowerUnit(unit: PowerUnit) {
        _uiState.update { it.copy(powerUnit = unit) }
        recalculate()
    }

    fun updateVoltage(value: String) {
        _uiState.update { it.copy(voltageInput = value) }
        recalculate()
    }

    fun updatePhaseType(type: MotorPhaseType) {
        _uiState.update {
            it.copy(
                phaseType = type,
                // Se monofásico, ligação triângulo/estrela não se aplica
                connectionType = if (type == MotorPhaseType.SINGLE_PHASE) ConnectionType.DELTA else it.connectionType
            )
        }
        recalculate()
    }

    fun updateConnectionType(connection: ConnectionType) {
        _uiState.update { it.copy(connectionType = connection) }
        recalculate()
    }

    fun updatePoles(poles: Int) {
        _uiState.update { it.copy(poles = poles) }
        recalculate()
    }

    fun updateSlots(slots: String) {
        _uiState.update { it.copy(slotsInput = slots) }
        recalculate()
    }

    fun updatePackageLength(length: String) {
        _uiState.update { it.copy(packageLengthInput = length) }
        recalculate()
    }

    fun updateStatorDiameter(diameter: String) {
        _uiState.update { it.copy(statorDiameterInput = diameter) }
        recalculate()
    }

    fun updateCurrentDensityTarget(density: String) {
        _uiState.update { it.copy(currentDensityTargetInput = density) }
        recalculate()
    }

    fun updateAirGapFluxDensity(flux: String) {
        _uiState.update { it.copy(airGapFluxDensityInput = flux) }
        recalculate()
    }

    fun updateEfficiency(eff: String) {
        _uiState.update { it.copy(efficiencyInput = eff) }
        recalculate()
    }

    fun updatePowerFactor(pf: String) {
        _uiState.update { it.copy(powerFactorInput = pf) }
        recalculate()
    }

    fun updateDoubleLayer(isDouble: Boolean) {
        _uiState.update { it.copy(isDoubleLayer = isDouble) }
        recalculate()
    }

    fun toggleAdvanced() {
        _uiState.update { it.copy(isAdvancedOpen = !it.isAdvancedOpen) }
    }

    fun updatePresetCv(cvStr: String) {
        _uiState.update { it.copy(powerInput = cvStr) }
    }

    fun updatePresetRpm(rpmStr: String) {
        val poles = MotorPresetsCatalog.parsePolesFromRpmOrPoles(rpmStr)
        _uiState.update { it.copy(rpmInput = rpmStr, poles = poles) }
    }

    fun updatePresetVoltage(voltageStr: String) {
        _uiState.update { it.copy(voltageInput = voltageStr) }
    }

    fun calculatePreset() {
        applyPresetFromInput(
            cvStr = _uiState.value.powerInput,
            rpmStr = _uiState.value.rpmInput,
            voltageStr = _uiState.value.voltageInput
        )
    }

    fun applyPresetQuick(cv: Double, rpm: Int, voltage: Double) {
        val cvText = if (cv % 1.0 == 0.0) cv.toInt().toString() else cv.toString()
        val rpmText = rpm.toString()
        val voltText = voltage.toInt().toString()
        _uiState.update {
            it.copy(
                powerInput = cvText,
                rpmInput = rpmText,
                voltageInput = voltText,
                poles = MotorPresetsCatalog.parsePolesFromRpmOrPoles(rpmText)
            )
        }
        applyPresetFromInput(cvStr = cvText, rpmStr = rpmText, voltageStr = voltText)
    }

    private fun applyPresetFromInput(cvStr: String, rpmStr: String, voltageStr: String) {
        val cv = cvStr.toDoubleOrNull() ?: return recalculate()
        val voltage = voltageStr.toDoubleOrNull() ?: 220.0
        val poles = MotorPresetsCatalog.parsePolesFromRpmOrPoles(rpmStr)
        val spec = MotorPresetsCatalog.findSpec(cv, poles)

        _uiState.update {
            it.copy(
                poles = poles,
                slotsInput = spec.slots.toString(),
                packageLengthInput = spec.packageLengthMm.toInt().toString(),
                statorDiameterInput = spec.statorDiameterMm.toInt().toString(),
                efficiencyInput = spec.efficiency.toString(),
                powerFactorInput = spec.powerFactor.toString(),
                currentDensityTargetInput = spec.targetCurrentDensity.toString(),
                airGapFluxDensityInput = spec.targetAirGapFluxDensity.toString()
            )
        }
        recalculate()
    }

    fun applyPresetWeg5cv() {
        applyPresetQuick(cv = 5.0, rpm = 1800, voltage = 220.0)
    }

    fun applyPreset2cv() {
        applyPresetQuick(cv = 2.0, rpm = 3600, voltage = 220.0)
    }

    fun applyPresetMonofasico() {
        applyPreset(MotorPresets.singlePhase1cv)
    }

    private fun applyPreset(preset: MotorInputData) {
        _uiState.update {
            it.copy(
                powerInput = preset.powerValue.toString(),
                powerUnit = preset.powerUnit,
                voltageInput = preset.voltageVolts.toInt().toString(),
                phaseType = preset.phaseType,
                connectionType = preset.connectionType,
                poles = preset.poles,
                rpmInput = MotorPresetsCatalog.getSyncRpm(preset.poles).toString(),
                slotsInput = preset.slotsCount.toString(),
                packageLengthInput = preset.packageLengthMm.toInt().toString(),
                statorDiameterInput = preset.statorInternalDiameterMm.toInt().toString(),
                currentDensityTargetInput = preset.targetCurrentDensity.toString(),
                airGapFluxDensityInput = preset.targetAirGapFluxDensity.toString(),
                efficiencyInput = preset.efficiency.toString(),
                powerFactorInput = preset.powerFactor.toString(),
                isDoubleLayer = preset.isDoubleLayer
            )
        }
        recalculate()
    }

    fun recalculate() {
        val currentState = _uiState.value
        val domainInput = currentState.toDomainInput()

        val canCompute = domainInput.powerValue > 0 &&
                domainInput.voltageVolts > 0 &&
                domainInput.packageLengthMm > 0 &&
                domainInput.statorInternalDiameterMm > 0 &&
                domainInput.slotsCount > 0

        val calcResult = if (canCompute) {
            MotorRewindEngine.calculate(domainInput)
        } else {
            null
        }

        val validation = MotorRewindEngine.validate(domainInput, calcResult)

        _uiState.update {
            it.copy(
                result = calcResult,
                validationReport = validation
            )
        }
    }

    fun notifyCopied() {
        _uiState.update { it.copy(copiedFeedback = true) }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { it.copy(copiedFeedback = false) }
        }
    }

    fun calculate() {
        recalculate()
    }

    fun updateCopperPrice(value: String) {
        _uiState.update { it.copy(copperPriceInput = value) }
    }

    fun saveCalculation(clientName: String) {
        val result = _uiState.value.result ?: return
        val motorDesc = "Motor ${_uiState.value.powerInput} CV ${_uiState.value.rpmInput} RPM"
        
        viewModelScope.launch {
            rewindDao.insertService(
                RewindService(
                    clientName = clientName,
                    motorDescription = motorDesc,
                    technicalSummary = result.copyableSummary,
                    isFerragem = false,
                    powerCv = _uiState.value.powerInput.toDoubleOrNull() ?: 0.0,
                    rpm = _uiState.value.poles,
                    voltage = _uiState.value.voltageInput.toDoubleOrNull() ?: 0.0
                )
            )
        }
    }
}
