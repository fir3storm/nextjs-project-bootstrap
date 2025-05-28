package com.wifisecurityapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkTestActivity : AppCompatActivity() {

    private lateinit var pingResultTextView: TextView
    private lateinit var portScanResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_test)

        pingResultTextView = findViewById(R.id.ping_result)
        portScanResultTextView = findViewById(R.id.port_scan_result)

        runPingTest("8.8.8.8")
        runPortScan("192.168.1.1", listOf(22, 80, 443))
    }

    private fun runPingTest(host: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val runtime = Runtime.getRuntime()
                val process = runtime.exec("/system/bin/ping -c 1 $host")
                val exitValue = process.waitFor()
                val result = if (exitValue == 0) "Ping to $host successful" else "Ping to $host failed"
                runOnUiThread {
                    pingResultTextView.text = result
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@NetworkTestActivity, "Ping test failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun runPortScan(host: String, ports: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val openPorts = mutableListOf<Int>()
            for (port in ports) {
                try {
                    val socket = Socket()
                    socket.connect(InetSocketAddress(host, port), 200)
                    socket.close()
                    openPorts.add(port)
                } catch (e: IOException) {
                    // Port closed or unreachable
                }
            }
            val resultText = if (openPorts.isEmpty()) "No open ports found" else "Open ports: ${openPorts.joinToString(", ")}"
            runOnUiThread {
                portScanResultTextView.text = resultText
            }
        }
    }
}
