package com.example.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import com.example.data.model.RewindService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfGenerator {

    fun generateServicePdf(context: Context, service: RewindService): File? {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titlePaint = Paint()

        // Configuração da página (A4: 595 x 842 pixels a 72 DPI)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Desenhar Cabeçalho
        titlePaint.textSize = 20f
        titlePaint.isFakeBoldText = true
        titlePaint.color = Color.BLACK
        canvas.drawText("FICHA TÉCNICA DE REBOBINAGEM", 50f, 50f, titlePaint)

        paint.textSize = 12f
        paint.color = Color.DKGRAY
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        canvas.drawText("Data: ${sdf.format(Date(service.dateTimestamp))}", 50f, 75f, paint)
        canvas.drawText("Cliente: ${service.clientName}", 50f, 95f, paint)
        canvas.drawText("Motor: ${service.motorDescription}", 50f, 115f, paint)

        // Linha divisória
        canvas.drawLine(50f, 130f, 545f, 130f, paint)

        // Conteúdo Técnico
        paint.textSize = 10f
        paint.color = Color.BLACK
        var yPos = 150f
        val lines = service.technicalSummary.split("\n")
        for (line in lines) {
            if (yPos > 800) break // Evitar estouro de página simples
            canvas.drawText(line, 50f, yPos, paint)
            yPos += 15f
        }

        // Rodapé
        paint.textSize = 8f
        paint.color = Color.GRAY
        canvas.drawText("Gerado pelo App Rebobina motor", 50f, 820f, paint)

        pdfDocument.finishPage(page)

        // Salvar Arquivo
        val fileName = "Ficha_${service.clientName.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF Gerado: ${file.name}", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            pdfDocument.close()
        }

        return file
    }
}
