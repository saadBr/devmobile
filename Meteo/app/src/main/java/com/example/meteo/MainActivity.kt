package com.example.meteo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.meteo.ui.theme.MeteoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.Scaffold
import androidx.compose.ui.platform.LocalConfiguration
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeteoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherScreen()
                }
            }
        }
    }
}
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = WeatherViewModel()) {
    val config = LocalConfiguration.current
    val isWear = config.screenWidthDp < 300 // rough check for watch

    if (isWear) {
        WearWeatherUI(viewModel)
    } else {
        PhoneWeatherUI(viewModel)
    }
}

@Composable
fun PhoneWeatherUI(viewModel: WeatherViewModel = WeatherViewModel()) {
    val city by remember { mutableStateOf("Fes") }
    val weatherList = viewModel.weatherItems
    var inputCity by remember { mutableStateOf(city) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = inputCity,
            onValueChange = { inputCity = it },
            label = { Text("Ville") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.fetchWeather(inputCity) }) {
            Text("Rechercher")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(weatherList.toList()) { item ->
                WeatherItemView(item)
            }
        }
    }
}

@Composable
fun WearWeatherUI(viewModel: WeatherViewModel) {
    val city by remember { mutableStateOf("Fes") }
    val weatherList = viewModel.weatherItems
    var inputCity by remember { mutableStateOf(city) }

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) }
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Text("Ville", style = MaterialTheme.typography.labelSmall)
                OutlinedTextField(
                    value = inputCity,
                    onValueChange = { inputCity = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Button(onClick = { viewModel.fetchWeather(inputCity) }) {
                    Text("Go")
                }
            }
            items(weatherList) { item ->
                WeatherItemView(item)
            }
        }
    }
}


@Composable
fun WeatherItemView(item: WeatherItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${item.iconCode}@2x.png"),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = item.date, style = MaterialTheme.typography.bodyMedium)
                Text("Min: ${item.minTemp}°C  Max: ${item.maxTemp}°C")
                Text("Pression: ${item.pressure} hPa")
                Text("Humidité: ${item.humidity}%")
            }
        }
    }
}

class WeatherViewModel : ViewModel() {
    private val _weatherItems = mutableStateListOf<WeatherItem>()
    val weatherItems: SnapshotStateList<WeatherItem> get() = _weatherItems

    private val apiKey = "3aed5994bdf03d38f905f7778ecd3842"

    init {
        fetchWeather("Fes")
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val urlString = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(stream)
                    val list = jsonObject.getJSONArray("list")

                    val newItems = mutableListOf<WeatherItem>()
                    for (i in 0 until minOf(6, list.length())) {
                        val item = list.getJSONObject(i)
                        val main = item.getJSONObject("main")
                        val weather = item.getJSONArray("weather").getJSONObject(0)
                        val dt_txt = item.getString("dt_txt")

                        val tempMin = main.getDouble("temp_min").toInt()
                        val tempMax = main.getDouble("temp_max").toInt()
                        val pressure = main.getInt("pressure")
                        val humidity = main.getInt("humidity")
                        val icon = weather.getString("icon")

                        newItems.add(
                            WeatherItem(
                                date = dt_txt,
                                minTemp = tempMin,
                                maxTemp = tempMax,
                                pressure = pressure,
                                humidity = humidity,
                                iconCode = icon
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        _weatherItems.clear()
                        _weatherItems.addAll(newItems)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class WeatherItem(
    val date: String,
    val minTemp: Int,
    val maxTemp: Int,
    val pressure: Int,
    val humidity: Int,
    val iconCode: String
)