package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ValidationItem
import com.example.domain.model.ValidationReport
import com.example.domain.model.ValidationSeverity
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.IndustrialCritical
import com.example.ui.theme.IndustrialWarning

@Composable
fun ValidationAlertBanner(
    report: ValidationReport,
    modifier: Modifier = Modifier
) {
    if (report.items.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        report.items.forEach { item ->
            val (bgColor, borderColor, iconColor, icon) = when (item.severity) {
                ValidationSeverity.CRITICAL_ERROR -> Quadruple(
                    IndustrialCritical.copy(alpha = 0.12f),
                    IndustrialCritical.copy(alpha = 0.5f),
                    IndustrialCritical,
                    Icons.Default.Dangerous
                )
                ValidationSeverity.WARNING -> Quadruple(
                    IndustrialWarning.copy(alpha = 0.12f),
                    IndustrialWarning.copy(alpha = 0.5f),
                    IndustrialWarning,
                    Icons.Default.Warning
                )
                ValidationSeverity.INFO -> Quadruple(
                    ElectricCyan.copy(alpha = 0.10f),
                    ElectricCyan.copy(alpha = 0.4f),
                    ElectricCyan,
                    Icons.Default.Info
                )
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = bgColor,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = item.title,
                        tint = iconColor,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = iconColor
                        )
                        Text(
                            text = item.message,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
