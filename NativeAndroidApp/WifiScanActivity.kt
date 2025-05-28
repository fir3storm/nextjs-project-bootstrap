package com.wifisecurityapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WifiScanActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var listView: ListView
    private val wifiList = mutableListOf<String>()
    private val PERMISSIONS_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_scan)

        listView = findViewById(R.id.wifi_list)
        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_CODE)
        } else {
            scanWifi()
        }
    }

    private fun scanWifi() {
        val success = wifiManager.startScan()
        if (!success) {
            Toast.makeText(this, "WiFi scan failed", Toast.LENGTH_SHORT).show()
            return
        }
        val results: List<ScanResult> = wifiManager.scanResults
        wifiList.clear()
        for (result in results) {
            val security = getSecurityType(result)
            wifiList.add("${result.SSID} - $security - Signal: ${result.level}")
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, wifiList)
        listView.adapter = adapter
    }

    private fun getSecurityType(scanResult: ScanResult): String {
        val capabilities = scanResult.capabilities
        return when {
            capabilities.contains("WEP") -> "WEP"
            capabilities.contains("WPA") -> "WPA/WPA2"
            capabilities.contains("EAP") -> "EAP"
            else -> "Open"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanWifi()
            } else {
                Toast.makeText(this, "Permission denied. Cannot scan WiFi.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
