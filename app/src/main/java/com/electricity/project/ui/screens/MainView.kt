package com.electricity.project.ui.screens

import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.auth0.android.jwt.JWT
import com.electricity.project.R
import com.electricity.project.api.aggregated.power.production.entity.AggregatedPowerProductionDTO
import com.electricity.project.api.aggregated.power.production.viewmodel.PowerProductionViewModel
import com.electricity.project.api.power.station.viewmodel.PowerStationViewModel
import com.electricity.project.api.token.Roles
import com.electricity.project.api.token.TokenManager
import com.electricity.project.api.token.TokenViewModel
import com.electricity.project.ui.theme.LogoBlue
import com.electricity.project.ui.theme.LogoBlueBackground
import com.electricity.project.ui.theme.TopBarBackground

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    powerProductionViewModel: PowerProductionViewModel,
    powerStationViewModel: PowerStationViewModel,
    tokenViewModel: TokenViewModel
) {
    val localContext = LocalContext.current

    var isLaunched by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isLaunched) {
        if (!isLaunched) {
            val handlerThread = HandlerThread("Data-Update")
            handlerThread.start()
            val mHandler = Handler(handlerThread.looper)

            val runnable: Runnable = object : Runnable {
                override fun run() {
                    if (navController.currentBackStackEntry?.destination?.route.equals(
                            AppScreen.MainView.route
                        )
                    ) {
                        Toast.makeText(
                            localContext,
                            "Odświeżono dane",
                            Toast.LENGTH_LONG
                        ).show()
                        powerProductionViewModel.getAggregatedPowerProduction()
                        powerStationViewModel.getPowerStationsStatusCount()
                        mHandler.postDelayed(this, 30000)
                    }
                }
            }
            mHandler.post(runnable)
            isLaunched = true
        }
    }


    val jwtToken by tokenViewModel.jwtToken.observeAsState()

    val powerProductionLastMinute by powerProductionViewModel.aggregatedPowerProduction.observeAsState(
        initial = AggregatedPowerProductionDTO()
    )
    val powerStationsStates by powerStationViewModel.powerStationsCount.observeAsState(initial = emptyMap())

    Scaffold(
        topBar = {
            LogoutTopBar(tokenViewModel)
        }
    ) {
        Surface(
            modifier = modifier.padding(it),
            color = LogoBlueBackground
        ) {
            LazyColumn(modifier.fillMaxSize()) {
                item {
                    MainViewCard(
                        "Sumaryczna produkcja energii elektrycznej",
                        "${powerProductionLastMinute.aggregatedValue} kWh"
                    )
                }
                itemsIndexed(items = powerStationsStates.keys.toList()) { _, item ->
                    MainViewCard(
                        "Liczba elektrowni ${stringResource(id = item.stateName)}",
                        "${powerStationsStates[item]}"
                    )
                }
                item {
                    MainViewCard(
                        "Nazwa użytkownika: ${
                            getPreferredUsername(jwtToken)
                        }",
                        "Rola: ${
                            getRolesFromJwtToken(jwtToken)
                        }"
                    )
                }
            }
        }
    }
}


private fun getPreferredUsername(jwtToken: JWT?) =
    jwtToken?.getClaim("preferred_username")?.asString()


private fun getRolesFromJwtToken(jwtToken: JWT?) =
    (jwtToken?.getClaim("realm_access")
        ?.asObject(Map::class.java)
        ?.get("roles") as ArrayList<*>)
        .map { it.toString() }
        .filter { jwtRole ->
            val role = Roles.entries.find {
                it.name == jwtRole
            }
            role != null
        }.joinToString { it }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutTopBar(tokenViewModel: TokenViewModel) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TopBarBackground
        ),
        title = {
            Row (
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .height(40.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo aplikacji"
                )
                Spacer(modifier = Modifier.width(10.dp))
                CardText("SZOZE")
            }
        },
        actions = {
            IconButton(onClick = {
                tokenViewModel.clearTokens()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_power_off_96),
                    contentDescription = "Wyloguj się"
                )
            }
        })
}

@Composable
fun MainViewCard(topText: String, bottomText: String) {
    Card(
        colors = CardDefaults.elevatedCardColors(containerColor = LogoBlue),
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
            CardText(text = topText)
            CardText(text = bottomText)
        }
    }
}

@Composable
fun CardText(text: String) {
    Text(
        text = text,
        maxLines = 1,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        lineHeight = TextUnit(15f, TextUnitType.Sp),
        fontSize = TextUnit(15f, TextUnitType.Sp)
    )
}

@Preview
@Composable
fun MainViewCardPreview() {
    MainViewCard("Sumaryczna produkcja prądu", "300000 kWh")
}

@Preview
@Composable
fun LogoutTopBarPreview() {
    LogoutTopBar(TokenViewModel(TokenManager(LocalContext.current)))
}