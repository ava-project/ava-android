package com.jeansarda.avacompanion.recording.analog

import android.content.pm.PackageManager
import android.media.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log.d
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jeansarda.avacompanion.R
import java.io.IOException

class RecordingAnalogActivity : AppCompatActivity() {

    private lateinit var recordButton: Button
    private lateinit var statusTextView: TextView
    internal var isRecording = false
    private var mr: MediaRecorder? = null
    private var FILENAME: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording_analog)

        supportActionBar?.title = "Recording"

        FILENAME += Environment.getExternalStorageDirectory().absolutePath + "/avarecording.3gp"


        d("WRITING ON", FILENAME)
        statusTextView = findViewById(R.id.status_textview)
        statusTextView.text = "Checking permissions"

        recordButton = findViewById<Button>(R.id.record_button)
        recordButton.setOnClickListener {
            d("ISRECORDING", isRecording.toString())
            if (isRecording) {
                statusTextView.text = "Stopped recording"
                stopRecording()
            } else {
                statusTextView.text = "Recording"
                startRecording()
            }
        }
        recordButton.isEnabled = false
        requestPermission()
    }


    private fun startRecording() {
        val pm = packageManager
        if (pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            mr = MediaRecorder()
            mr!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr!!.setOutputFile(FILENAME)
            try {
                mr!!.prepare()
                mr!!.start()
                statusTextView.text = "Recording"
                isRecording = true
            } catch (e: IOException) {
                d("ERROR", e.localizedMessage)
                isRecording = false
            }

        } else {
            Toast.makeText(this, "No microphone found", Toast.LENGTH_LONG).show()
            isRecording = false
        }

    }

    private fun stopRecording() {
        mr!!.stop()
        mr!!.reset()
        mr!!.release()
        startPlaying()
        isRecording = false
    }

    private fun startPlaying() {
        statusTextView.text = "Transmitting"
        val mp = MediaPlayer()
        mp.setDataSource(FILENAME)
        mp.prepare()
        mp.start()
    }

    private fun requestPermission() {
        val permCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        val readCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), 62621)
        } else {
            recordButton.isEnabled = true
            statusTextView.text = "Ready to record"
        }
        if (readCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 62622)
        } else {
            recordButton.isEnabled = true
            statusTextView.text = "Ready to record"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val result = grantResults[0]
        if (result == PackageManager.PERMISSION_GRANTED) {
            d("PERMISSION", "granted")
            recordButton.isEnabled = true
            statusTextView.text = "Ready to record"
        } else {
            d("PERMISSION", "denied")
            recordButton.isEnabled = false
            statusTextView.text = "Permission error"
        }


    }

}
