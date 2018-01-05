package com.jeansarda.avacompanion.pairing.analog

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.jeansarda.avacompanion.R
import com.jeansarda.avacompanion.recording.analog.RecordingAnalogActivity

class PairingAnalogMacOSActivity : AppCompatActivity() {

    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairing_analog_mac_os)

        supportActionBar?.title = "Pairing"

        nextButton = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            val intent = Intent(this, RecordingAnalogActivity::class.java)
            startActivity(intent)
        }
    }
}
