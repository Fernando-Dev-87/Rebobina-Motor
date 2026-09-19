package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CalculationResult
import com.example.domain.model.ConnectionType
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun ResultCard(
    result: CalculationResult,
    copiedFeedback: Boolean,
    onCopied: () -> Unit,
    onSave: (String) -> Unit = {},
    copperPrice: String = "85.00",
    onCopperPriceChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
            // Cabeçalho
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

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    border = BorderStroke(1.dp, CobaltBlueLight.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ConnectionDiagram(type = result.connectionType, modifier = Modifier.size(20.dp))
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

            // SEÇÃO 1: OPÇÕES DE BANCADA
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Opção 1
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("OPÇÃO 1 (1 FIO)", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("AWG ${result.recommendedWire.awg}", fontSize = 17.sp, fontWeight = FontWeight.Black, color = AmberCopper)
                                Text(String.format(Locale.US, "%.3f mm²", result.recommendedWire.sectionMm2), fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        // Opção 2
                        if (result.parallelWireAlternative != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("OPÇÃO 2 (2 FIOS)", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("2x AWG ${result.parallelWireAlternative.first.awg}", fontSize = 17.sp, fontWeight = FontWeight.Black, color = AmberCopper)
                                    Text(String.format(Locale.US, "Tot: %.3f mm²", result.parallelWireAlternative.second), fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Text("ESPIRAS / BOBINA:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text("${result.turnsPerCoil} espiras", fontSize = 18.sp, fontWeight = FontWeight.Black, color = CobaltBlueLight)
                                Text("${result.conductorsPerSlot} cond./ranhura", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Indicador de Densidade
            val densityColor = when {
                result.actualCurrentDensity > 6.0 -> IndustrialCritical
                result.actualCurrentDensity > 5.5 -> IndustrialWarning
                else -> IndustrialSuccess
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = densityColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, densityColor.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(densityColor))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(String.format(Locale.US, "Densidade Real (J): %.2f A/mm²", result.actualCurrentDensity), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = densityColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Grid Técnico
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                TechParamItem("Corrente Nominal (In)", String.format(Locale.US, "%.2f A", result.nominalCurrentAmps), Modifier.weight(1f))
                TechParamItem("Cobre Estimado", String.format(Locale.US, "%.2f kg", result.estimatedCopperWeightKg), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // SEÇÃO DE ORÇAMENTO
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = AmberCopper, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("INTELIGÊNCIA DE ORÇAMENTO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = copperPrice,
                            onValueChange = onCopperPriceChange,
                            label = { Text("Preço Cobre/kg", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            prefix = { Text("R$ ") },
                            singleLine = true
                        )
                        val totalCost = (copperPrice.toDoubleOrNull() ?: 85.0) * result.estimatedCopperWeightKg
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("Custo Material", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Text("R$ ${String.format(Locale.US, "%.2f", totalCost)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = AmberCopper)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ações
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(result.copyableSummary))
                        onCopied()
                    },
                    modifier = Modifier.weight(1.2f).height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (copiedFeedback) IndustrialSuccess else MaterialTheme.colorScheme.primary)
                ) {
                    Icon(if (copiedFeedback) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copiar", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, result.copyableSummary)
                            type = "text/plain"
                            setPackage("com.whatsapp")
                        }
                        try { context.startActivity(sendIntent) } catch (e: Exception) {
                            context.startActivity(Intent.createChooser(sendIntent, "Enviar via"))
                        }
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(onClick = { showSaveDialog = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Salvar Histórico", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Salvar Serviço") },
            text = {
                OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Nome do Cliente / OS") }, modifier = Modifier.fillMaxWidth())
            },
            confirmButton = {
                Button(onClick = {
                    if (clientName.isNotBlank()) {
                        onSave(clientName)
                        showSaveDialog = false
                        clientName = ""
                    }
                }) { Text("Salvar") }
            },
            dismissButton = { TextButton(onClick = { showSaveDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun TechParamItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurface)
    }
}
