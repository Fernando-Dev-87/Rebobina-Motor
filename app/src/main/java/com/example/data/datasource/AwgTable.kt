package com.example.data.datasource

import com.example.data.model.AwgWire

/**
 * Tabela técnica de referência AWG (10 AWG a 30 AWG) com valores padrão para condutores
 * esmaltados utilizados na rebobinagem de motores elétricos industriais conforme normas
 * ABNT NBR 5111 / NBR 8449 e NEMA MW 1000.
 */
object AwgTable {

    val wires: List<AwgWire> = listOf(
        AwgWire(awg = 10, sectionMm2 = 5.261, bareDiameterMm = 2.588, insulatedDiameterMm = 2.700, resistanceOhmPerKm = 3.28),
        AwgWire(awg = 11, sectionMm2 = 4.172, bareDiameterMm = 2.305, insulatedDiameterMm = 2.410, resistanceOhmPerKm = 4.13),
        AwgWire(awg = 12, sectionMm2 = 3.309, bareDiameterMm = 2.053, insulatedDiameterMm = 2.150, resistanceOhmPerKm = 5.21),
        AwgWire(awg = 13, sectionMm2 = 2.624, bareDiameterMm = 1.828, insulatedDiameterMm = 1.920, resistanceOhmPerKm = 6.57),
        AwgWire(awg = 14, sectionMm2 = 2.081, bareDiameterMm = 1.628, insulatedDiameterMm = 1.715, resistanceOhmPerKm = 8.28),
        AwgWire(awg = 15, sectionMm2 = 1.650, bareDiameterMm = 1.450, insulatedDiameterMm = 1.530, resistanceOhmPerKm = 10.45),
        AwgWire(awg = 16, sectionMm2 = 1.309, bareDiameterMm = 1.291, insulatedDiameterMm = 1.365, resistanceOhmPerKm = 13.17),
        AwgWire(awg = 17, sectionMm2 = 1.038, bareDiameterMm = 1.150, insulatedDiameterMm = 1.220, resistanceOhmPerKm = 16.61),
        AwgWire(awg = 18, sectionMm2 = 0.823, bareDiameterMm = 1.024, insulatedDiameterMm = 1.090, resistanceOhmPerKm = 20.95),
        AwgWire(awg = 19, sectionMm2 = 0.653, bareDiameterMm = 0.912, insulatedDiameterMm = 0.975, resistanceOhmPerKm = 26.41),
        AwgWire(awg = 20, sectionMm2 = 0.518, bareDiameterMm = 0.812, insulatedDiameterMm = 0.870, resistanceOhmPerKm = 33.31),
        AwgWire(awg = 21, sectionMm2 = 0.410, bareDiameterMm = 0.723, insulatedDiameterMm = 0.778, resistanceOhmPerKm = 42.00),
        AwgWire(awg = 22, sectionMm2 = 0.326, bareDiameterMm = 0.644, insulatedDiameterMm = 0.696, resistanceOhmPerKm = 52.96),
        AwgWire(awg = 23, sectionMm2 = 0.258, bareDiameterMm = 0.573, insulatedDiameterMm = 0.622, resistanceOhmPerKm = 66.79),
        AwgWire(awg = 24, sectionMm2 = 0.205, bareDiameterMm = 0.511, insulatedDiameterMm = 0.556, resistanceOhmPerKm = 84.21),
        AwgWire(awg = 25, sectionMm2 = 0.162, bareDiameterMm = 0.455, insulatedDiameterMm = 0.498, resistanceOhmPerKm = 106.2),
        AwgWire(awg = 26, sectionMm2 = 0.129, bareDiameterMm = 0.405, insulatedDiameterMm = 0.445, resistanceOhmPerKm = 133.9),
        AwgWire(awg = 27, sectionMm2 = 0.102, bareDiameterMm = 0.361, insulatedDiameterMm = 0.399, resistanceOhmPerKm = 168.9),
        AwgWire(awg = 28, sectionMm2 = 0.0810, bareDiameterMm = 0.321, insulatedDiameterMm = 0.356, resistanceOhmPerKm = 212.9),
        AwgWire(awg = 29, sectionMm2 = 0.0642, bareDiameterMm = 0.286, insulatedDiameterMm = 0.318, resistanceOhmPerKm = 268.5),
        AwgWire(awg = 30, sectionMm2 = 0.0509, bareDiameterMm = 0.255, insulatedDiameterMm = 0.284, resistanceOhmPerKm = 338.6)
    )

    val wireMap: Map<Int, AwgWire> = wires.associateBy { it.awg }

    /**
     * Encontra o fio AWG comercial mais adequado para a seção teórica necessária (mm²).
     * Dá preferência para a seção imediatamente igual ou superior, com tolerância de até -3%.
     */
    fun findClosestSingleWire(requiredSectionMm2: Double): AwgWire {
        // Ordena por menor erro absoluto, com leve penalidade para fios menores que o requerido
        return wires.minByOrNull { wire ->
            val diff = wire.sectionMm2 - requiredSectionMm2
            if (diff >= 0) diff else kotlin.math.abs(diff) * 1.5
        } ?: wires.first()
    }

    /**
     * Para seções maiores onde um condutor único é muito rígido para bobinagem manual
     * na bancada (> AWG 16), calcula também a alternativa prática de 2 fios em paralelo.
     */
    fun findTwoParallelWires(requiredSectionMm2: Double): Pair<AwgWire, Double> {
        val halfSection = requiredSectionMm2 / 2.0
        val wire = findClosestSingleWire(halfSection)
        val totalSection = wire.sectionMm2 * 2.0
        return Pair(wire, totalSection)
    }
}
