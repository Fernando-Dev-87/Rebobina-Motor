package com.example.domain.model

enum class FileCategory(val label: String) {
    TODOS("Todos"),
    APOSTILAS("Apostilas & Manuais"),
    ESQUEMAS_TRIFASICOS("Esquemas Trifásicos"),
    ESQUEMAS_MONOFASICOS("Esquemas Monofásicos"),
    TABELAS_TECNICAS("Tabelas AWG & WEG"),
    MOTORES_ESPECIAIS("Dahlander & Especiais")
}

data class RebobinadorFile(
    val id: String,
    val title: String,
    val fileName: String,
    val category: FileCategory,
    val author: String,
    val dateAdded: String,
    val fileSize: String,
    val fileExtension: String,
    val description: String,
    val tags: List<String>,
    val technicalContent: String,
    val downloadUrl: String = "https://www.facebook.com/groups/395592083967391/files/files"
)
