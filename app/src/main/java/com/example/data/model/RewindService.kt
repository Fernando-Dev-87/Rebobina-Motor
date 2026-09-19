package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewind_services")
data class RewindService(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientName: String,
    val motorDescription: String, // ex: WEG W22 5CV 1800RPM
    val dateTimestamp: Long = System.currentTimeMillis(),
    val technicalSummary: String, // O resumo completo para cópia
    val isFerragem: Boolean = false,
    val powerCv: Double = 0.0,
    val rpm: Int = 0,
    val voltage: Double = 0.0
)
