package com.example.lneberknare2.services

import java.math.BigDecimal

/**
 * En dataklass som håller resultatet av en löneberäkning.
 *
 * @property grossSalary Bruttolön (före skatt).
 * @property taxAmount Det beräknade skattebeloppet.
 * @property netSalary Nettolön (efter skatt).
 * @property error Ett eventuellt felmeddelande om något gick fel.
 */
data class SalaryCalculation(
    val grossSalary: BigDecimal,
    val taxAmount: BigDecimal,
    val netSalary: BigDecimal,
    val error: String? = null
)
