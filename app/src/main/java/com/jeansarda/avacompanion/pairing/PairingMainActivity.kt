package com.jeansarda.avacompanion.pairing

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.jeansarda.avacompanion.pairing.websocket.PairingWebsocketActivity
import com.jeansarda.avacompanion.R
import com.jeansarda.avacompanion.pairing.analog.PairingAnalogMainActivity
import com.jeansarda.avacompanion.recording.analog.RecordingAnalogActivity

class PairingMainActivity : AppCompatActivity() {

    private lateinit var websocketButton: Button
    private lateinit var analogButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairing_main)

        supportActionBar?.title = "Pairing"

        websocketButton = findViewById<Button>(R.id.websocket_button)
        websocketButton.setOnClickListener {
            var intent = Intent(this, PairingWebsocketActivity::class.java)
            startActivity(intent)
        }

        analogButton = findViewById<Button>(R.id.analog_button)
        analogButton.setOnClickListener {
            var intent = Intent(this, PairingAnalogMainActivity::class.java)
            startActivity(intent)
        }

    }
}
