package com.jeansarda.avacompanion.pairing.analog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.jeansarda.avacompanion.R
import com.jeansarda.avacompanion.recording.analog.RecordingAnalogActivity

class PairingAnalogMainActivity : AppCompatActivity() {

    private lateinit var welcomeTextView: TextView
    private lateinit var pickOSTextView: TextView
    private lateinit var macOSButton: Button
    private lateinit var windowsButton: Button
    private lateinit var linuxButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairing_analog_main)

        welcomeTextView = findViewById(R.id.welcomeTextView)
        pickOSTextView = findViewById(R.id.pickOSTextView)

        welcomeTextView.text = "Welcome to the analog pairing wizard!"
        pickOSTextView.text = "Please choose the OS on which AVA is installed."

        macOSButton = findViewById(R.id.macOSButton)
        windowsButton = findViewById(R.id.windowsButton)
        linuxButton = findViewById(R.id.linuxButton)

        macOSButton.text = "macOS"
        windowsButton.text = "Windows"
        linuxButton.text = "Linux"

        macOSButton.setOnClickListener {
            var intent = Intent(this, PairingAnalogMacOSActivity::class.java)
            startActivity(intent)
        }

        windowsButton.setOnClickListener {
            var intent = Intent(this, PairingAnalogWindowsActivity::class.java)
            startActivity(intent)
        }

        linuxButton.setOnClickListener {
            var intent = Intent(this, PairingAnalogLinuxActivity::class.java)
            startActivity(intent)
        }

    }
}
