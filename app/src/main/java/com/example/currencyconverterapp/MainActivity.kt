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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencyconverterapp.ui.theme.CurrencyconverterappTheme
import com.example.currencyconverterapp.ui.viewmodel.CurrencyViewModel
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyconverterappTheme {
                val viewModel: CurrencyViewModel = viewModel()
                CurrencyConverterScreen(viewModel)
            }
        }
    }
}

@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel) {
    var startCurrency by remember { mutableStateOf("EUR") }
    var targetCurrency by remember { mutableStateOf("USD") }
    var amount by remember { mutableStateOf("") }
    val convertedAmount by viewModel.exchangeRate.collectAsState()

    val currencies = listOf("EUR", "USD", "GBP", "KWD")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Startwährung")
            DropdownMenuComponent(
                selectedCurrency = startCurrency,
                currencies = currencies,
                onCurrencySelected = { startCurrency = it }
            )
            Text("Zielwährung")
            DropdownMenuComponent(
                selectedCurrency = targetCurrency,
                currencies = currencies,
                onCurrencySelected = { targetCurrency = it }
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                },
                label = { Text("Betrag eingeben") },
                singleLine = true
            )
            Button(onClick = {
                viewModel.fetchExchangeRate(startCurrency, targetCurrency, amount)
            }) {
                Text("Umrechnen")
            }
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
fun CurrencyConverterScreenPreview() {
    val dummyViewModel = object : CurrencyViewModel() {
    }
    CurrencyconverterappTheme {
        CurrencyConverterScreen(viewModel = dummyViewModel)
    }
}
