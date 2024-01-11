package com.electricity.project

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.electricity.project.api.ServiceApiBuilder
import com.electricity.project.api.viewmodel.PowerProductionViewModel
import com.electricity.project.api.viewmodel.PowerStationViewModel
import com.electricity.project.ui.theme.CardBackground
import com.electricity.project.ui.theme.MobileapplicationTheme
import java.util.Date
import kotlin.math.ceil

@Composable
fun MainView(modifier: Modifier = Modifier) {
    val powerProductionViewModel: PowerProductionViewModel =
        viewModel(factory = ServiceApiBuilder.viewModelFactory)
    val powerStationViewModel: PowerStationViewModel =
        viewModel(factory = ServiceApiBuilder.viewModelFactory)

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        val powerProductionLastMinute by powerProductionViewModel.aggregatedPowerProduction.collectAsState()
        val powerStationsStates by powerStationViewModel.powerStationsCount.collectAsState()
        val shouldStopLoop = false
        val mHandler = Handler(Looper.getMainLooper())
                val localContext = LocalContext.current


        val runnable: Runnable = object : Runnable {
            override fun run() {
                Toast.makeText(localContext, "Odświeżono dane", Toast.LENGTH_LONG).show()
                powerStationViewModel.getPowerStationsStatusCount()
                powerProductionViewModel.getAggregatedPowerProduction()
                if (!shouldStopLoop) {
                    mHandler.postDelayed(this, 30000)
                }
            }
        }


        LazyColumn(modifier.fillMaxSize()) {
            item {
               mHandler.post(runnable)
            }
            item {
                Card(
                    colors = CardDefaults.elevatedCardColors(containerColor = CardBackground),
                    elevation = CardDefaults.outlinedCardElevation(3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Sumaryczna produkcja prądu")
                        Text(text = "${powerProductionLastMinute.aggregatedValue} kWh")
                    }
                }
            }

            itemsIndexed(items = powerStationsStates.keys.toList()) { _, item ->
                Card(
                    colors = CardDefaults.elevatedCardColors(containerColor = CardBackground),
                    elevation = CardDefaults.outlinedCardElevation(3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Liczba elektrowni o stanie: ${item.name}")
                        Text(text = "${powerStationsStates[item]}")
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileapplicationTheme {
        MainView(Modifier.fillMaxSize())
    }
}