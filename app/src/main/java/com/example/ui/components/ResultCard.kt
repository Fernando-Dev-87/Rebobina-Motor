package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CalculationResult
import com.example.domain.model.ConnectionType
import com.example.ui.theme.CopperAmber
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.IndustrialCritical
import com.example.ui.theme.IndustrialSuccess
import com.example.ui.theme.IndustrialWarning
import java.util.Locale

@Composable
fun ResultCard(
    result: CalculationResult,
    copiedFeedback: Boolean,
    onCopied: () -> Unit,
    onSave: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var showSaveDialog by remember { mutableStateOf(false) }
    var clientName by remember { mutableStateOf("") }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("result_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cabeçalho com Badge de Fio AWG e Ligação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RESULTADO DE REBOBINAGEM",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Especificações de Bancada",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Badge de Ligação (Estrela / Triângulo) - Apenas o Símbolo
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = result.connectionType.symbol,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SEÇÃO 1: OPÇÕES DE BANCADA (MOSTRADO PRIMEIRO)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "OPÇÕES DE BANCADA",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Destaque de 1 Fio e 2 Fios em Paralelo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Opção 1: Fio Simples
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "OPÇÃO 1 (1 FIO)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "AWG ${result.recommendedWire.awg}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CopperAmber
                                )
                                Text(
                                    text = String.format(Locale.US, "%.3f mm²", result.recommendedWire.sectionMm2),
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Opção 2: Fio Duplo em Paralelo
                        if (result.parallelWireAlternative != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "OPÇÃO 2 (2 FIOS)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "2x AWG ${result.parallelWireAlternative.first.awg}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CopperAmber
                                    )
                                    Text(
                                        text = String.format(Locale.US, "Tot: %.3f mm²", result.parallelWireAlternative.second),
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Espiras por Bobina na Bancada
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "ESPIRAS / BOBINA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${result.conductorsPerSlot} condutores por ranhura",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${result.turnsPerCoil} espiras",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = ElectricCyan
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Indicador de Densidade de Corrente (J)
            val densityColor = when {
                result.actualCurrentDensity > 6.0 -> IndustrialCritical
                result.actualCurrentDensity > 5.5 -> IndustrialWarning
                result.actualCurrentDensity < 3.0 -> IndustrialWarning
                else -> IndustrialSuccess
            }

            val densityStatusLabel = when {
                result.actualCurrentDensity > 6.0 -> "PERIGO: Risco de queima por sobreaquecimento"
                result.actualCurrentDensity > 5.5 -> "Atenção: Densidade no limite superior"
                result.actualCurrentDensity < 3.0 -> "Aviso: Subdimensionado (ranhura cheia)"
                else -> "Densidade Segura (Norma ABNT/NEMA)"
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = densityColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, densityColor.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(densityColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = String.format(Locale.US, "Densidade Real (J): %.2f A/mm²", result.actualCurrentDensity),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = densityColor
                        )
                        Text(
                            text = densityStatusLabel,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            // Grid de dados técnicos detalhados
            Row(modifier = Modifier.fillMaxWidth()) {
                TechParamItem(
                    label = "Corrente Nominal (In)",
                    value = String.format(Locale.US, "%.2f A", result.nominalCurrentAmps),
                    modifier = Modifier.weight(1f)
                )
                TechParamItem(
                    label = "Corrente de Fase (If)",
                    value = String.format(Locale.US, "%.2f A", result.phaseCurrentAmps),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TechParamItem(
                    label = "Fluxo Magnético / Polo",
                    value = String.format(Locale.US, "%.4f Wb", result.magneticFluxWeber),
                    modifier = Modifier.weight(1f)
                )
                TechParamItem(
                    label = "Peso de Cobre Est.",
                    value = String.format(Locale.US, "%.2f kg", result.estimatedCopperWeightKg),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Seção de Esquema WEG
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("ESQUEMA TÉCNICO WEG", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        Text("Bobinas por Grupo: ${if (result.coilsPerGroup % 1.0 == 0.0) result.coilsPerGroup.toInt() else String.format(Locale.US, "%.1f", result.coilsPerGroup)}", fontSize = 12.sp)
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text("Passo de Fase (Defasagem)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${String.format(Locale.US, "%.1f", result.phaseStep)} ranhuras", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Botão de Cópia para Clipboard
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(result.copyableSummary))
                    onCopied()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("copy_summary_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (copiedFeedback) IndustrialSuccess else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (copiedFeedback) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = if (copiedFeedback) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = "Copiar resumo para área de transferência",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (copiedFeedback) "Resumo Copiado para a Bancada!" else "Copiar Resumo dos Dados",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { showSaveDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salvar no Histórico", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Salvar Serviço") },
            text = {
                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Nome do Cliente / OS") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (clientName.isNotBlank()) {
                        onSave(clientName)
                        showSaveDialog = false
                        clientName = ""
                    }
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TechParamItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
