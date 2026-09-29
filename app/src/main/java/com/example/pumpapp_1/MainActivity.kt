package com.example.pumpapp_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.pumpapp_1.mqtt.MqttClientManager
import com.example.pumpapp_1.ui.theme.PumpApp_1Theme

class MainActivity : ComponentActivity() {
    private val mqttServerUri = "tcp://x.x.x.x:1883"
    private val mqttTopic = "topic/test"
    private lateinit var mqttClientManager: MqttClientManager
    private var messageState by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mqttClientManager = MqttClientManager(mqttServerUri, mqttTopic) { message ->
            messageState = message
        }

        setContent {
            PumpApp_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MqttView(messageState)
                        mqttClientManager.publish("Nova mensagem")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mqttClientManager.isInitialized) {
            mqttClientManager.disconnect()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MqttView(messageState: String) {
        Column {
            TopAppBar(title = { Text(text = "MQTT message") })
            Surface {
                Column {
                    Text(text = "MESSSAGE:")
                    Text(text = messageState)
                }
            }
        }
    }
}
