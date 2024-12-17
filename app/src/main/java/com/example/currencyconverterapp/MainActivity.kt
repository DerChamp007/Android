package com.example.currencyconverterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverterapp.ui.theme.CurrencyconverterappTheme
import java.util.Currency

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyconverterappTheme {
                CurrencyConverterScreen()
            }
        }
    }
}

@Composable
fun CurrencyConverterScreen(){
    var startCurrency by remember { mutableStateOf("EUR") }
    var targetCurrency by remember { mutableStateOf("USD") }
    var amount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf(0.0) }

    val currencies = listOf("EUR","USD","GBP","KWD")

    fun convertCurrency(amount: String, start: String, target: String): Double{
        val rate = when{
            start == "EUR" && target == "USD" -> 1.05
            start == "USD" && target == "EUR" -> 0.95
            start == "EUR" && target == "GBP" -> 0.83
            start == "GBP" && target == "EUR" -> 1.21
            start == "EUR" && target == "KWD" -> 0.32
            start == "KWD" && target == "EUR" -> 3.10
            else -> 1.0
        }
        return  amount.toDoubleOrNull()?.times(rate) ?: 0.0
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE)),
        contentAlignment = Alignment.Center
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 24.sp
            )

            Text("Startwährung")
            DropdownMenuComponent(
                selectedCurrency = startCurrency,
                currencies = currencies,
                onCurrencySelected = {
                    startCurrency = it
                    convertedAmount = convertCurrency(amount, startCurrency, targetCurrency)
                }
            )
            Text("Zielwährung")
            DropdownMenuComponent(
                selectedCurrency = targetCurrency,
                currencies = currencies,
                onCurrencySelected = {
                    targetCurrency = it
                    convertedAmount = convertCurrency(amount, startCurrency, targetCurrency)
                }
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    convertedAmount = convertCurrency(amount, startCurrency, targetCurrency)
                },
                label = { Text("Betrag eingeben")},
                singleLine = true
            )
            Text(
                text = "Umgerechnet: %.2f %s".format(convertedAmount, targetCurrency),
                fontSize = 18.sp,
                color = Color.Black
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_currency),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun DropdownMenuComponent(
    selectedCurrency: String,
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    Box{
        Button(onClick = {expanded = true}, shape = RoundedCornerShape(8.dp)) {
            Text(text = selectedCurrency)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
            currencies.forEach{ currency ->
                DropdownMenuItem(
                    text = {Text(text = currency)},
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                }
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CurrencyConverterScreenPreview(){
    CurrencyconverterappTheme {
        CurrencyConverterScreen()
    }
}