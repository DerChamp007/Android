package com.example.currencyconverterapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverterapp.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class CurrencyViewModel : ViewModel() {
    private val _exchangeRate = MutableStateFlow(0.0)
    val exchangeRate: StateFlow<Double> = _exchangeRate

    fun fetchExchangeRate(base: String, target: String, amount: String) {
        viewModelScope.launch {
            try {
                // API-Aufruf
                val response = RetrofitInstance.api.getExchangeRates(base, target)
                if (response.isSuccessful) {
                    val rate = response.body()?.rates?.get(target) ?: 1.0
                    println("Umrechnungskurs für $base zu $target: $rate") // Debug-Ausgabe

                    // Berechnung des Ergebnisses
                    val calculatedAmount = rate * (amount.toDoubleOrNull() ?: 0.0)
                    println("Berechneter Betrag: $calculatedAmount") // Debug-Ausgabe

                    _exchangeRate.value = calculatedAmount
                } else {
                    println(
                        "API-Fehler: ${
                            response.errorBody()?.string()
                        }"
                    ) // Debug-Ausgabe bei Fehlern
                }
            } catch (e: Exception) {
                // Ausnahmebehandlung und Logging
                e.printStackTrace()
            }
        }
    }
}