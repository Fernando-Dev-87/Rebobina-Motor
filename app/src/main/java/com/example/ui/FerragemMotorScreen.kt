package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.datasource.MotorFrameCatalog
import com.example.domain.model.ConnectionType
import com.example.domain.model.MotorPhaseType
import com.example.ui.components.FerragemResultCard
import com.example.ui.components.IndustrialNumberInput
import com.example.ui.components.RebobinadorFilesDialog
import com.example.ui.components.SegmentedPolesSelector
import com.example.ui.components.ValidationAlertBanner
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FerragemMotorScreen(
    viewModel: FerragemViewModel,
    onNavigateToPlacaScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var showRebobinadorFilesDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var carcaçaInput by remember { mutableStateOf("") }

    val allCarcaças = remember { MotorFrameCatalog.getFullList() }
    val filteredCarcaças = remember(carcaçaInput) {
        if (carcaçaInput.isEmpty()) allCarcaças
        else allCarcaças.filter { it.contains(carcaçaInput, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Architecture,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Cálculo pela Ferragem",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Apostila 018/09 • Estator sem Placa",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateToPlacaScreen,
                        modifier = Modifier.testTag("back_to_placa_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar para cálculo por placa"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { showRebobinadorFilesDialog = true },
                        modifier = Modifier.testTag("ferragem_open_rebobinador_files_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Arquivos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Botão em destaque para alternar/voltar de página
            OutlinedButton(
                onClick = onNavigateToPlacaScreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .testTag("switch_to_placa_page_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ir para Modo: Rebobinagem por Dados de Placa",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            // Botão para arquivos 'O Rebobinador'
            Button(
                onClick = { showRebobinadorFilesDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("ferragem_banner_rebobinador_files_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("O Rebobinador (Arquivos & Download)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }

            // Presets Rápidos de Carcaças de Ferragem
            Text(
                text = "CARREGAR CARCAÇAS DE FERRAGEM PADRÃO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp)
            ) {
                OutlinedTextField(
                    value = carcaçaInput,
                    onValueChange = { 
                        carcaçaInput = it
                        expanded = true
                    },
                    label = { Text("Escolha ou Digite a Carcaça (Ex: W22 90L)", fontSize = 12.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                    shape = RoundedCornerShape(10.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded && filteredCarcaças.isNotEmpty(),
                    onDismissRequest = { expanded = false }
                ) {
                    filteredCarcaças.take(50).forEach { name ->
                        DropdownMenuItem(
                            text = { Text(name, fontWeight = FontWeight.Medium) },
                            onClick = {
                                carcaçaInput = name
                                // Tenta aplicar preset se existir nome exato mapeado
                                when {
                                    name.contains("90L") -> viewModel.applyPreset90L()
                                    name.contains("80") -> viewModel.applyPreset80()
                                    name.contains("100L") -> viewModel.applyPreset100L()
                                    name.contains("63") -> viewModel.applyPreset63()
                                    name.contains("71") -> viewModel.applyPreset71()
                                    name.contains("112") -> viewModel.applyPreset112M()
                                    name.contains("132") -> viewModel.applyPreset132M()
                                }
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            // Formulário de Medições com Paquímetro na Ferragem (MOVIDO PARA CIMA)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. MEDIDAS FÍSICAS DO ESTATOR (PAQUÍMETRO)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IndustrialNumberInput(
                            label = "Diâmetro Interno (D)",
                            value = uiState.internalDiameterInput,
                            onValueChange = { viewModel.updateInternalDiameter(it) },
                            suffix = "mm",
                            modifier = Modifier.weight(1f),
                            testTag = "ferragem_input_d"
                        )

                        IndustrialNumberInput(
                            label = "Compr. Pacote (L)",
                            value = uiState.packageLengthInput,
                            onValueChange = { viewModel.updatePackageLength(it) },
                            suffix = "mm",
                            modifier = Modifier.weight(1f),
                            testTag = "ferragem_input_l"
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IndustrialNumberInput(
                        label = "Quantidade de Ranhuras (S)",
                        value = uiState.slotsInput,
                        onValueChange = { viewModel.updateSlots(it) },
                        suffix = "ranhuras",
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "ferragem_input_slots"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dimensões da Ranhura
                    Text(
                        text = "2. GEOMETRIA DA RANHURA DO ESTATOR",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val slotAreaCalculated = (
                            ((uiState.slotWidthTopInput.toDoubleOrNull() ?: 8.5) +
                                    (uiState.slotWidthBottomInput.toDoubleOrNull() ?: 6.0)) / 2.0
                            ) * (uiState.slotDepthInput.toDoubleOrNull() ?: 18.0)

                    Text(
                        text = "Área da Ranhura Calculada: ${String.format(Locale.US, "%.1f", slotAreaCalculated)} mm²",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IndustrialNumberInput(
                            label = "Altura (h)",
                            value = uiState.slotDepthInput,
                            onValueChange = { viewModel.updateSlotDepth(it) },
                            suffix = "mm",
                            modifier = Modifier.weight(1f),
                            testTag = "ferragem_input_h"
                        )
                        IndustrialNumberInput(
                            label = "Largura Sup. (b1)",
                            value = uiState.slotWidthTopInput,
                            onValueChange = { viewModel.updateSlotWidthTop(it) },
                            suffix = "mm",
                            modifier = Modifier.weight(1f),
                            testTag = "ferragem_input_b1"
                        )
                        IndustrialNumberInput(
                            label = "Largura Inf. (b2)",
                            value = uiState.slotWidthBottomInput,
                            onValueChange = { viewModel.updateSlotWidthBottom(it) },
                            suffix = "mm",
                            modifier = Modifier.weight(1f),
                            testTag = "ferragem_input_b2"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Parâmetros Elétricos Desejados para a Ferragem
                    Text(
                        text = "3. ESPECIFICAÇÃO ELÉTRICA DA REDE",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.phaseType == MotorPhaseType.THREE_PHASE,
                            onClick = { viewModel.updatePhaseType(MotorPhaseType.THREE_PHASE) },
                            label = { Text("Trifásico (3Ø)", fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ferragem_phase_three_chip")
                        )
                        FilterChip(
                            selected = uiState.phaseType == MotorPhaseType.SINGLE_PHASE,
                            onClick = { viewModel.updatePhaseType(MotorPhaseType.SINGLE_PHASE) },
                            label = { Text("Monofásico (1Ø)", fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ferragem_phase_single_chip")
                        )
                    }

                    if (uiState.phaseType == MotorPhaseType.THREE_PHASE) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = uiState.connectionType == ConnectionType.DELTA,
                                onClick = { viewModel.updateConnectionType(ConnectionType.DELTA) },
                                label = { Text("Δ", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("ferragem_connection_delta_chip")
                            )
                            FilterChip(
                                selected = uiState.connectionType == ConnectionType.STAR,
                                onClick = { viewModel.updateConnectionType(ConnectionType.STAR) },
                                label = { Text("Y", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("ferragem_connection_star_chip")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    IndustrialNumberInput(
                        label = "Tensão Nominal da Rede",
                        value = uiState.voltageInput,
                        onValueChange = { viewModel.updateVoltage(it) },
                        suffix = "V",
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "ferragem_input_voltage"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Polos e Rotação Síncrona
                    SegmentedPolesSelector(
                        selectedPoles = uiState.poles,
                        onPolesSelected = { viewModel.updatePoles(it) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Ajustes Avançados Eletromagnéticos (B, J, ke, Camada)
                    OutlinedButton(
                        onClick = { viewModel.toggleAdvanced() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ferragem_toggle_advanced_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.isAdvancedOpen) "Ocultar Ajustes da Apostila" else "Ajustes Avançados (Indução B, Densidade J, Enchimento %)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (uiState.isAdvancedOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }

                    AnimatedVisibility(
                        visible = uiState.isAdvancedOpen,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IndustrialNumberInput(
                                    label = "Indução Bmax (Tesla)",
                                    value = uiState.inductionTeslaInput,
                                    onValueChange = { viewModel.updateInduction(it) },
                                    suffix = "T",
                                    modifier = Modifier.weight(1f),
                                    testTag = "ferragem_input_induction"
                                )
                                IndustrialNumberInput(
                                    label = "Densidade Alvo (J)",
                                    value = uiState.targetCurrentDensityInput,
                                    onValueChange = { viewModel.updateTargetCurrentDensity(it) },
                                    suffix = "A/mm²",
                                    modifier = Modifier.weight(1f),
                                    testTag = "ferragem_input_j"
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            IndustrialNumberInput(
                                label = "Fator de Enchimento Alvo (%)",
                                value = uiState.targetFillFactorInput,
                                onValueChange = { viewModel.updateTargetFillFactor(it) },
                                suffix = "% (35-42)",
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "ferragem_input_fill_factor"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Enrolamento de Dupla Camada",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (uiState.isDoubleLayer) "2 lados de bobina por ranhura" else "Camada simples (1 bobina/ranhura)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = uiState.isDoubleLayer,
                                    onCheckedChange = { viewModel.updateDoubleLayer(it) },
                                    modifier = Modifier.testTag("ferragem_double_layer_switch")
                                )
                            }
                        }
                    }
                }
            }

            // Banners de Alerta e Limites Físicos
            ValidationAlertBanner(
                report = uiState.validationReport,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Card Principal de Resultados da Ferragem
            uiState.result?.let { result ->
                FerragemResultCard(
                    result = result,
                    copiedFeedback = uiState.copiedFeedback,
                    onCopied = { viewModel.notifyCopied() },
                    onSave = { viewModel.saveCalculation(it) },
                    copperPrice = uiState.copperPriceInput,
                    onCopperPriceChange = { viewModel.updateCopperPrice(it) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showRebobinadorFilesDialog) {
        RebobinadorFilesDialog(
            onDismiss = { showRebobinadorFilesDialog = false }
        )
    }
}
