package com.example.domain.model

enum class ValidationSeverity {
    INFO,
    WARNING,
    CRITICAL_ERROR
}

data class ValidationItem(
    val title: String,
    val message: String,
    val severity: ValidationSeverity
)

data class ValidationReport(
    val isValid: Boolean,
    val items: List<ValidationItem>
)
