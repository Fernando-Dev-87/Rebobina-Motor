package com.example.data.model

/**
 * Representação técnica de condutores esmaltados de cobre padrão AWG (American Wire Gauge)
 * para enrolamento de máquinas elétricas industriais.
 *
 * @param awg Bitola na escala AWG (10 a 30)
 * @param sectionMm2 Área da seção transversal nominal do condutor de cobre puro (mm²)
 * @param bareDiameterMm Diâmetro do condutor sem esmalte (mm)
 * @param insulatedDiameterMm Diâmetro médio aproximado com isolamento classe H/200°C (mm)
 * @param resistanceOhmPerKm Resistência elétrica ôhmica a 20°C (Ω/km)
 * @param maxCurrentAmpsAt4A Capacidade de corrente recomendada para densidade J = 4.0 A/mm²
 */
data class AwgWire(
    val awg: Int,
    val sectionMm2: Double,
    val bareDiameterMm: Double,
    val insulatedDiameterMm: Double,
    val resistanceOhmPerKm: Double,
    val maxCurrentAmpsAt4A: Double = sectionMm2 * 4.0
)
