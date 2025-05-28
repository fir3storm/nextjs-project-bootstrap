package com.wifisecurityapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wifiScanButton: MaterialButton = findViewById(R.id.btn_wifi_scan)
        val networkTestButton: MaterialButton = findViewById(R.id.btn_network_test)

        wifiScanButton.setOnClickListener {
            val intent = Intent(this, WifiScanActivity::class.java)
            startActivity(intent)
        }

        networkTestButton.setOnClickListener {
            val intent = Intent(this, NetworkTestActivity::class.java)
            startActivity(intent)
        }
    }
}
