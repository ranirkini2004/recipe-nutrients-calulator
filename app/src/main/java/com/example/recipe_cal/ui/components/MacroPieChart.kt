package com.example.recipe_cal.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MacroPieChart(protein: Float, carbs: Float, fat: Float, modifier: Modifier = Modifier) {
    val total = protein + carbs + fat

    if (total == 0f) return

    val proteinAngle = (protein / total) * 360f
    val carbsAngle = (carbs / total) * 360f
    val fatAngle = (fat / total) * 360f

    Canvas(modifier = modifier.size(200.dp)) {
        drawArc(
            color = Color(0xFFE57373),
            startAngle = 0f,
            sweepAngle = proteinAngle,
            useCenter = true
        )
        drawArc(
            color = Color(0xFF81C784),
            startAngle = proteinAngle,
            sweepAngle = carbsAngle,
            useCenter = true
        )
        drawArc(
            color = Color(0xFFFFD54F),
            startAngle = proteinAngle + carbsAngle,
            sweepAngle = fatAngle,
            useCenter = true
        )
    }
}