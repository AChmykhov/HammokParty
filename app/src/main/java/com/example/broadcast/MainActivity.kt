package com.example.broadcast

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private val PICK_CONTACT_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val thisActivity = this@MainActivity

        if (ContextCompat.checkSelfPermission(
                thisActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (!(ActivityCompat.shouldShowRequestPermissionRationale(
                    thisActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
            ) {
                ActivityCompat.requestPermissions(
                    thisActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
            }
        }
    }

    fun scanQR(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this@MainActivity, ScanActivity::class.java)
        startActivityForResult(intent, PICK_CONTACT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            PICK_CONTACT_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    var data = findViewById<TextInputEditText>(R.id.IPPortInput)
                    data.setText(intent?.data.toString())
                    joinParty(findViewById<Button>(R.id.joinButton))
                }
            }
        }
    }

    fun startParty(@Suppress("UNUSED_PARAMETER") view: View) {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!(wifiManager.isWifiEnabled)) {
            Toast.makeText(this, "No connection to Wi-Fi network", Toast.LENGTH_LONG).show()
        } else {
            startActivity(Intent(this, TransmitterActivity::class.java))
        }
    }

    fun joinParty(@Suppress("UNUSED_PARAMETER") view: View) {
        val data = findViewById<TextInputEditText>(R.id.IPPortInput)
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (data.text.toString() == "") {
            Toast.makeText(this, "No IP address entered", Toast.LENGTH_LONG).show()
        } else {
            if (!(wifiManager.isWifiEnabled)) {
                Toast.makeText(this, "No connection to Wi-Fi network", Toast.LENGTH_LONG).show()
            } else {
                val receiverIntent = Intent(this, ReceiverActivity::class.java)
                receiverIntent.putExtra(ReceiverActivity.ipPort, data.text.toString())
                startActivity(receiverIntent)
            }
        }
    }
}