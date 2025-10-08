package com.example.lneberknare2.services

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * En implementation av ISalaryCalculatorService som använder en
 * matematisk modell för att *uppskatta* skatten istället för att slå upp i en tabell.
 */
class TaxApproximationService : ISalaryCalculatorService {

    // Konstanter för skattemodellen 2025 (kan justeras för högre precision)
    private val STATE_TAX_THRESHOLD_MONTHLY = BigDecimal("51275")
    private val STATE_TAX_RATE = BigDecimal("0.20")
    private val BASE_TAX_RATE = BigDecimal("0.32") // Genomsnittlig kommunalskatt

    // Förenklad modell för jobbskatteavdraget (större effekt på lägre löner)
    private val JOB_CREDIT_THRESHOLD = BigDecimal("35000")
    private val JOB_CREDIT_FACTOR_LOW_INCOME = BigDecimal("0.80") // Innebär att ca 80% av lönen beskattas
    private val JOB_CREDIT_FACTOR_HIGH_INCOME = BigDecimal("0.90") // Innebär att ca 90% av lönen beskattas

    override fun calculate(hourlyWage: BigDecimal, hoursWorked: Double, taxColumn: Int): SalaryCalculation {
        if (hoursWorked < 0) {
            return SalaryCalculation(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "Antal timmar kan inte vara negativt.")
        }

        val grossSalary = hourlyWage.multiply(BigDecimal.valueOf(hoursWorked))

        // Steg 1: Justera den skattepliktiga basen för att simulera jobbskatteavdrag/grundavdrag
        val adjustmentFactor = if (grossSalary < JOB_CREDIT_THRESHOLD) {
            JOB_CREDIT_FACTOR_LOW_INCOME
        } else {
            JOB_CREDIT_FACTOR_HIGH_INCOME
        }
        val taxableBase = grossSalary.multiply(adjustmentFactor)

        // Steg 2: Beräkna grundskatten (kommunal nivå)
        var totalTax = taxableBase.multiply(BASE_TAX_RATE)

        // Steg 3: Beräkna och lägg till eventuell statlig skatt
        if (grossSalary > STATE_TAX_THRESHOLD_MONTHLY) {
            val amountOverThreshold = grossSalary.subtract(STATE_TAX_THRESHOLD_MONTHLY)
            val stateTax = amountOverThreshold.multiply(STATE_TAX_RATE)
            totalTax = totalTax.add(stateTax)
        }

        // Säkerställ att skatten inte är negativ
        totalTax = totalTax.max(BigDecimal.ZERO)

        val netSalary = grossSalary.subtract(totalTax)

        // Avrunda alla värden till 2 decimaler för presentation
        val roundedGross = grossSalary.setScale(2, RoundingMode.HALF_UP)
        val roundedTax = totalTax.setScale(2, RoundingMode.HALF_UP)
        val roundedNet = netSalary.setScale(2, RoundingMode.HALF_UP)

        return SalaryCalculation(roundedGross, roundedTax, roundedNet)
    }
}
