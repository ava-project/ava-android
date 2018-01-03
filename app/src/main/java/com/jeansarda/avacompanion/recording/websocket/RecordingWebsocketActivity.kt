package com.jeansarda.avacompanion.recording.websocket

import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.koushikdutta.async.http.AsyncHttpClient
import java.io.File
import java.io.IOException
import com.koushikdutta.async.http.AsyncHttpGet
import cafe.adriel.androidaudioconverter.callback.ILoadCallback
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudioconverter.model.AudioFormat
import com.jeansarda.avacompanion.R
import java.io.FileNotFoundException
import java.io.FileOutputStream


class RecordingWebsocketActivity : AppCompatActivity() {

    private lateinit var ipAddress: String
    private lateinit var recordButton: Button
    private lateinit var statusTextView: TextView
    internal var isRecording = false
    private var mr: MediaRecorder? = null
    private var ar: AudioRecord? = null
    private var FILENAME: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording_websocket)

        title = "Recording"
        supportActionBar?.title = "Recording"

        ipAddress = intent.getStringExtra("address")
        //ipAddress = savedInstanceState
        Log.d("WS_ADDR", ipAddress)

        FILENAME += Environment.getExternalStorageDirectory().absolutePath + "/avarecording.mp3"
        Log.d("WRITING ON", FILENAME)


        AndroidAudioConverter.load(this, object : ILoadCallback {
            override fun onSuccess() {
                // Great!
            }

            override fun onFailure(error: Exception) {
                // FFmpeg is not supported by device
            }
        })


        statusTextView = findViewById(R.id.status_textview)
        statusTextView.text = "Checking permissions"

        recordButton = findViewById<Button>(R.id.record_button)
        recordButton.setOnClickListener {
            Log.d("ISRECORDING", isRecording.toString())
            if (isRecording) {
                stopRecording()
            } else {
                startRecording()
            }
        }
        recordButton.isEnabled = false
        requestPermission()
    }



    private fun startRecording() {
        val pm = packageManager
        if (pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            /*mr = MediaRecorder()
            mr!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS)
            mr!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mr!!.setOutputFile(FILENAME)
            try {
                mr!!.prepare()
                mr!!.start()
                statusTextView.text = "Recording"
                isRecording = true
            } catch (e: IOException) {
                Log.d("ERROR", e.localizedMessage)
                isRecording = false
            }*/
            ar = AudioRecord(MediaRecorder.AudioSource.MIC, 44100, 16, 2, 10)
            try {
                ar!!.startRecording()
                val recordingThread = Thread(Runnable {
                    writeAudioDataToFile()
                })
                recordingThread.start()
                statusTextView.text = "Recording"
                isRecording = true
            } catch (e: IOException) {
                Log.d("ERROR", e.localizedMessage)
                isRecording = false
            }

        } else {
            Toast.makeText(this, "No microphone found", Toast.LENGTH_LONG).show()
            isRecording = false
        }

    }

    private fun writeAudioDataToFile() {
        // Write the output audio in byte

        val sData = ShortArray(1024)

        var os: FileOutputStream? = null
        try {
            os = FileOutputStream(FILENAME)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            ar!!.read(sData, 0, 1024)
            println("Short writing to file" + sData.toString())
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                //val bData = short2byte(sData)
                os!!.write(sData as ByteArray, 0, 1024 * 2)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        try {
            os!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    private fun stopRecording() {
        ar!!.stop()
        //mr!!.reset()
        ar!!.release()
        startPlaying()
        isRecording = false
    }

    private fun startPlaying() {
        statusTextView.text = "Ready to record"


        val aacFile = File(FILENAME)
        val callback = object : IConvertCallback {
            override fun onSuccess(convertedFile: File) {
                // So fast? Love it!
                Log.d("CONVERSION", "success")
                val get = AsyncHttpGet("http://" + ipAddress)

                AsyncHttpClient.getDefaultInstance().websocket(get, "a-protocol", AsyncHttpClient.WebSocketConnectCallback { ex, webSocket ->
                    if (ex != null) {
                        Log.d("ERRORRRRR", ex.localizedMessage)
                        //statusTextView.text = "Could not connect to " + ipAddress
                    } else {
                        Log.d("COOOL", "sending")
                        webSocket.send(convertedFile.readBytes())
                    }
                })
            }

            override fun onFailure(error: Exception) {
                // Oops! Something went wrong
                Log.d("CONVERSION", "error")
            }
        }
        AndroidAudioConverter.with(this)
                // Your current audio file
                .setFile(aacFile)

                // Your desired audio format
                .setFormat(AudioFormat.WAV)

                // An callback to know when conversion is finished
                .setCallback(callback)

                // Start conversion
                .convert()

    }

    private fun requestPermission() {
        val permCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        if (permCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), 62621)
        } else {
            recordButton.isEnabled = true
            statusTextView.text = "Ready to record"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val result = grantResults[0]
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "granted")
            recordButton.isEnabled = true
            statusTextView.text = "Ready to record"
        } else {
            Log.d("PERMISSION", "denied")
            recordButton.isEnabled = false
            statusTextView.text = "Permission error"
        }


    }


}
