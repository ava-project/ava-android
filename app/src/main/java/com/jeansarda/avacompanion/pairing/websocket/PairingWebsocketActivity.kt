package com.jeansarda.avacompanion.pairing.websocket

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jeansarda.avacompanion.R
import com.jeansarda.avacompanion.recording.websocket.RecordingWebsocketActivity

class PairingWebsocketActivity : AppCompatActivity() {

    private lateinit var connectButton: Button
    private lateinit var ipEditText: EditText
    private lateinit var welcomeTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var enterAddressTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairing_websocket)

        supportActionBar?.title = "Pairing"

        welcomeTextView = findViewById(R.id.welcomeTextView)
        instructionsTextView = findViewById(R.id.instructionsTextView)
        enterAddressTextView = findViewById(R.id.enterAddressTextView)

        welcomeTextView.text = "Welcome to the WiFi pairing wizard! We will take you through the necessary steps to connect your phone to AVA and use it as your microphone input."
        instructionsTextView.text = "Please make sure AVA is running and ready to be paired."
        enterAddressTextView.text = "Then, enter the AVA mobile Bride Address in the field below and tap Connect."

        connectButton = findViewById<Button>(R.id.connect_button)
        connectButton.setOnClickListener {
            val address = ipEditText.text.toString()
            var intent = Intent(this, RecordingWebsocketActivity::class.java)
            intent.putExtra("address", address)
            startActivity(intent)
        }

        ipEditText = findViewById(R.id.ip_edit_text)

    }
}
