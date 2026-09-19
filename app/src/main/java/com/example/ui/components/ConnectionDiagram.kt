package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.domain.model.ConnectionType
import com.example.ui.theme.AmberCopper
import com.example.ui.theme.CobaltBlueLight

@Composable
fun ConnectionDiagram(
    type: ConnectionType,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(40.dp)) {
        val w = size.width
        val h = size.height
        val stroke = 3.dp.toPx()
        val color = if (type == ConnectionType.DELTA) AmberCopper else CobaltBlueLight

        when (type) {
            ConnectionType.DELTA -> {
                // Desenhar Triângulo
                val p1 = Offset(w / 2, 0f)
                val p2 = Offset(0f, h)
                val p3 = Offset(w, h)
                drawLine(color, p1, p2, stroke, StrokeCap.Round)
                drawLine(color, p2, p3, stroke, StrokeCap.Round)
                drawLine(color, p3, p1, stroke, StrokeCap.Round)
            }
            ConnectionType.STAR -> {
                // Desenhar Estrela (Y)
                val center = Offset(w / 2, h / 2)
                val p1 = Offset(w / 2, 0f)
                val p2 = Offset(0f, h)
                val p3 = Offset(w, h)
                drawLine(color, center, p1, stroke, StrokeCap.Round)
                drawLine(color, center, p2, stroke, StrokeCap.Round)
                drawLine(color, center, p3, stroke, StrokeCap.Round)
            }
        }
    }
}
