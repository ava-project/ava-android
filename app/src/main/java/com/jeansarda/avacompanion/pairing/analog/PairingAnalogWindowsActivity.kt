package com.jeansarda.avacompanion.pairing.analog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jeansarda.avacompanion.R

class PairingAnalogWindowsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pairing_analog_windows)

        supportActionBar?.title = "Pairing"
    }
}