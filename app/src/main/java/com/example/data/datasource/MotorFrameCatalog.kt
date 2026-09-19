package com.example.data.datasource

object MotorFrameCatalog {
    val allFrames = listOf(
        "63", "71", "80", "90S", "90L", "100L", "112M", "132S", "132M",
        "160M", "160L", "180M", "180L", "200L", "225S", "225M", "250S", "250M",
        "280S", "280M", "315S", "315M", "315L", "355M", "355L"
    )

    val series = listOf("W22", "W21", "W11")

    fun getFullList(): List<String> {
        val list = mutableListOf<String>()
        series.forEach { s ->
            allFrames.forEach { f ->
                // W11 geralmente vai até 132
                if (s == "W11" && f.filter { it.isDigit() }.toIntOrNull() ?: 0 > 132) {
                    return@forEach
                }
                list.add("$s - $f")
            }
        }
        return list
    }
}
