package com.example.ui.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.FerragemCalculationResult
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun FerragemResultCard(
    result: FerragemCalculationResult,
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
        modifier = modifier.fillMaxWidth().testTag("ferragem_result_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(32.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Architecture, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("DIMENSIONAMENTO FERRAGEM", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.secondary)
                        Text("Padrão WEG / IEC", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer, border = BorderStroke(1.dp, CobaltBlueLight.copy(alpha = 0.4f))) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ConnectionDiagram(type = result.connectionType, modifier = Modifier.size(16.dp))
                        Text(result.connectionType.symbol, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Potência Estimada
            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("POTÊNCIA ESTIMADA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(String.format(Locale.US, "%.1f", result.estimatedPowerCv), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                            Text(" CV", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${result.synchronousRpm} RPM", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(String.format(Locale.US, "In: %.1f A", result.nominalCurrentAmps), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Fio e Espiras
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ResultBadge(title = "FIO RECOMENDADO", value = "AWG ${result.recommendedWire.awg}", subValue = "${result.recommendedWire.sectionMm2} mm²", color = AmberCopper, modifier = Modifier.weight(1f))
                ResultBadge(title = "ESPIRAS / BOBINA", value = "${result.turnsPerCoil}", subValue = "espiras", color = CobaltBlueLight, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(14.dp))

            // Passo e Cobre
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Passo Recomendado", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(result.recommendedCoilPitch, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text("Cobre Est.", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Text(String.format(Locale.US, "%.2f kg", result.estimatedCopperWeightKg), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Orçamento
            Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f), border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = AmberCopper, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("INTELIGÊNCIA DE ORÇAMENTO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(value = copperPrice, onValueChange = onCopperPriceChange, label = { Text("Preço Cobre/kg", fontSize = 10.sp) }, modifier = Modifier.weight(1f), prefix = { Text("R$ ") }, singleLine = true)
                        val totalCost = (copperPrice.toDoubleOrNull() ?: 85.0) * result.estimatedCopperWeightKg
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("Custo Material", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Text("R$ ${String.format(Locale.US, "%.2f", totalCost)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = AmberCopper)
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Ações
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { clipboardManager.setText(AnnotatedString(result.copyableSummary)); onCopied() }, modifier = Modifier.weight(1.2f).height(48.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = if (copiedFeedback) IndustrialSuccess else MaterialTheme.colorScheme.secondary)) {
                    Icon(if (copiedFeedback) Icons.Default.Check else Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Copiar", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(onClick = {
                    val sendIntent = Intent().apply { action = Intent.ACTION_SEND; putExtra(Intent.EXTRA_TEXT, result.copyableSummary); type = "text/plain"; setPackage("com.whatsapp") }
                    try { context.startActivity(sendIntent) } catch (e: Exception) { context.startActivity(Intent.createChooser(sendIntent, "Enviar via")) }
                }, modifier = Modifier.weight(1f).height(48.dp), shape = RoundedCornerShape(10.dp)) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("WhatsApp", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { showSaveDialog = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Salvar Histórico", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Salvar Serviço") },
            text = { OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Nome do Cliente / OS") }, modifier = Modifier.fillMaxWidth()) },
            confirmButton = { Button(onClick = { if (clientName.isNotBlank()) { onSave(clientName); showSaveDialog = false; clientName = "" } }) { Text("Salvar") } },
            dismissButton = { TextButton(onClick = { showSaveDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun ResultBadge(title: String, value: String, subValue: String, color: Color, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(12.dp), color = color.copy(alpha = 0.12f), border = BorderStroke(1.5.dp, color), modifier = modifier) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = color, fontSize = 9.sp)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = color)
            Text(subValue, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        }
    }
}
