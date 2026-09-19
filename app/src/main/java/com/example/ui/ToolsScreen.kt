package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.AwgTable
import com.example.domain.calculator.ConversionEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val awgWires = remember { AwgTable.wires }
    val filteredWires = remember(searchQuery) {
        if (searchQuery.isEmpty()) awgWires
        else awgWires.filter { it.awg.toString().contains(searchQuery) }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Utilidades de Bancada", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar Bitola AWG") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            Spacer(Modifier.height(12.dp))

            Text("Tabela AWG Interativa", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }

        items(filteredWires) { wire ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("AWG ${wire.awg}", fontWeight = FontWeight.Black, fontSize = 18.sp)
                        Text("${wire.sectionMm2} mm²", fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Ø ${wire.bareDiameterMm} mm", fontWeight = FontWeight.Bold)
                        Text("${wire.resistanceOhmPerKm} Ω/km", fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(Modifier.height(16.dp))
            CapacitorCalculatorSection()
            
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(Modifier.height(16.dp))
            WegProceduresSection()
        }
    }
}

@Composable
fun WegProceduresSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Guia de Procedimentos WEG", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        listOf(
            "1. Remoção do Enrolamento Antigo" to "Não queime o estator acima de 350°C para não danificar as chapas silício.",
            "2. Isolamento de Ranhura" to "Utilize filme de poliéster ou Nomex. O isolante deve sobressair 5-10mm de cada lado.",
            "3. Confeção de Bobinas" to "Mantenha a cabeça da bobina o mais curta possível para evitar perdas e aquecimento.",
            "4. Impregnação e Secagem" to "Use verniz de alta qualidade. Seque em estufa conforme tempo do fabricante (típico 2-4h)."
        ).forEach { (title, detail) ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    Text(detail, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun CapacitorCalculatorSection() {
    var powerInput by remember { mutableStateOf("1.0") }
    var selectedVoltage by remember { mutableStateOf(220.0) }
    
    val result = remember(powerInput, selectedVoltage) {
        val cv = powerInput.toDoubleOrNull() ?: 1.0
        ConversionEngine.getCapacitorRecommendation(cv, selectedVoltage)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Calculadora de Capacitores", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        
        Text("Recomendação para Motores Monofásicos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = powerInput,
                onValueChange = { powerInput = it },
                label = { Text("Potência (CV)") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            
            Column(modifier = Modifier.weight(1.2f)) {
                Text("Tensão", style = MaterialTheme.typography.labelSmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = selectedVoltage == 110.0,
                        onClick = { selectedVoltage = 110.0 },
                        label = { Text("110V") }
                    )
                    Spacer(Modifier.width(4.dp))
                    FilterChip(
                        selected = selectedVoltage == 220.0,
                        onClick = { selectedVoltage = 220.0 },
                        label = { Text("220V") }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CapacitorInfoRow("Partida (Eletrolítico):", result.startingCapacitor, MaterialTheme.colorScheme.primary)
                CapacitorInfoRow("Permanente (Trabalho):", result.permanentCapacitor, MaterialTheme.colorScheme.secondary)
                
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f))
                
                Text(
                    "Tensão mín. recomendada: ${result.minVoltage}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        Spacer(Modifier.height(8.dp))
        Text(
            "Nota: Valores médios de mercado. Sempre consulte a placa do fabricante.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CapacitorInfoRow(label: String, value: String, valueColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black, color = valueColor)
    }
}
