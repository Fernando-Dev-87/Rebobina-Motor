package com.example.domain.calculator

import kotlin.math.PI
import kotlin.math.roundToInt

/**
 * Padrões técnicos extraídos do Manual de Bobinagem WEG e normas IEC/NEMA.
 */
object WegStandards {

    /**
     * Calcula o número de bobinas por grupo (Bg).
     * Essencial para definir o esquema de bobinagem (Imbricado ou Concêntrico).
     */
    fun calculateCoilsPerGroup(slots: Int, poles: Int, phases: Int): Double {
        return slots.toDouble() / (poles * phases)
    }

    /**
     * Calcula a defasagem de fase em ranhuras (Passo de Fase).
     * Para 3 fases, é a defasagem de 120 graus elétricos.
     */
    fun calculatePhaseStep(slots: Int, poles: Int): Double {
        return (slots.toDouble() / poles) * (2.0 / 3.0)
    }

    /**
     * Sugere a indução magnética (B) ideal conforme a aplicação.
     * Motores Standard: 0.65 - 0.75 T
     * Motores Premium (W22): 0.70 - 0.82 T
     */
    fun getRecommendedInduction(isPremium: Boolean = true): Double {
        return if (isPremium) 0.76 else 0.72
    }

    /**
     * Densidade de corrente (J) recomendada para motores fechados (TFVE).
     * Conforme isolação Classe F (155°C) e H (180°C).
     */
    fun getRecommendedCurrentDensity(isClassH: Boolean = false): Double {
        return if (isClassH) 5.5 else 4.8
    }

    /**
     * Constantes de correção para comprimento de cabeça de bobina (WEG).
     */
    /**
     * Sugere o tipo de enrolamento conforme slots/polos (WEG).
     */
    fun suggestWindingType(slots: Int, poles: Int): String {
        val q = slots.toDouble() / (poles * 3.0)
        return when {
            q % 1.0 == 0.0 -> "Imbricado (Camada Simples ou Dupla)"
            q % 0.5 == 0.0 -> "Concêntrico (Muito comum em motores WEG)"
            else -> "Fracionário (Requer cálculo de grupos especial)"
        }
    }
}
