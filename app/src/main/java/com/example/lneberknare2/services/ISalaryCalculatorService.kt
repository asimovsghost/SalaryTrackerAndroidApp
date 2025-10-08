package com.example.lneberknare2.services

import java.math.BigDecimal

/**
 * Definierar ett kontrakt (en mall) för alla tjänster som kan beräkna lön.
 * Detta gör det enkelt att byta ut beräkningslogiken i framtiden utan att
 * behöva ändra i gränssnittskoden.
 */
interface ISalaryCalculatorService {
    /**
     * Beräknar lön och skatt.
     * @param hourlyWage Timlönen.
     * @param hoursWorked Antal arbetade timmar.
     * @param taxColumn Vald skattekolumn (används inte i alla implementationer men finns med för flexibilitet).
     * @return Ett [SalaryCalculation]-objekt med resultatet.
     */
    fun calculate(hourlyWage: BigDecimal, hoursWorked: Double, taxColumn: Int): SalaryCalculation
}
