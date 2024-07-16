package com.cronometro

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cronometro.databinding.ActivityMainBinding
import org.w3c.dom.Text
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {
    private var timeSelected: Int = 0
    private var timeCounteDown: CountDownTimer? = null
    private var timePrograss = 0
    private var pauseOffset: Long = 0
    private var isStart = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addBtn: ImageButton = findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            setTimeFunction()
        }
        val startBtn: Button = findViewById(R.id.bt_play_pause)
        startBtn.setOnClickListener {
            startTimerSetup()
        }
        val resetBtn: ImageButton = findViewById(R.id.bt_reset)
        resetBtn.setOnClickListener {
            resetTime()
        }
        val addTimerTv: TextView = findViewById(R.id.textAdd15s)
        addTimerTv.setOnClickListener {
            addExtraTime()
        }
    }

    private fun addExtraTime() {
        val progressBar: ProgressBar = findViewById(R.id.prograss_bar_timer)
        if (timeSelected != 0) {
            timeSelected += 15
            progressBar.max = timeSelected
            timePause()
            startTimer(pauseOffset)
            Toast.makeText(this, "+15 segundos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetTime() {
        if (timeCounteDown != null) {
            timeCounteDown!!.cancel()
            timePrograss = 0
            timeSelected = 0
            pauseOffset = 0
            timeCounteDown = null
            val startBtn: Button = findViewById(R.id.bt_play_pause)
            startBtn.text = "start"
            isStart = true
            val progressBar = findViewById<ProgressBar>(R.id.prograss_bar_timer)
            progressBar.progress = 0
            val timeLeftTv: TextView = findViewById(R.id.text_time_left)
            timeLeftTv.text = "0"
        }
    }

    private fun timePause() {
        if (timeCounteDown != null) {
            timeCounteDown!!.cancel()
        }
    }

    private fun startTimerSetup() {
        val startBtn: Button = findViewById(R.id.bt_play_pause)
        if (timeSelected > timePrograss) {
            if (isStart) {
                startBtn.text = "Pause"
                startTimer(pauseOffset)
                isStart = false
            } else {
                isStart = true
                startBtn.text = "Resumir"
                timePause()
            }
        } else {
            Toast.makeText(this, "digite um tempo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTimer(pauseOffsetL: Long) {
        val progressBar = findViewById<ProgressBar>(R.id.prograss_bar_timer)
        progressBar.progress = timePrograss
        timeCounteDown =
            object : CountDownTimer((timeSelected * 1000).toLong() - pauseOffsetL * 1000, 1000) {
                override fun onTick(p0: Long) {
                    timePrograss++
                    pauseOffset = timeSelected.toLong() - p0 / 1000
                    progressBar.progress = timeSelected - timePrograss
                    val timeLeftTv: TextView = findViewById(R.id.text_time_left)
                    timeLeftTv.text = (timeSelected - timePrograss).toString()
                }

                override fun onFinish() {
                    resetTime()
                    Toast.makeText(this@MainActivity, "tempo acabou!", Toast.LENGTH_SHORT).show()
                }

            }.start()
    }

    private fun setTimeFunction() {
        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_dialog)

        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        val timeLeftTv: TextView = findViewById(R.id.text_time_left)
        val btnStart: Button = findViewById(R.id.bt_play_pause)
        val progressBar = findViewById<ProgressBar>(R.id.prograss_bar_timer)
        timeDialog.findViewById<Button>(R.id.bt_ok).setOnClickListener {
            if (timeSet.text.isEmpty()) {
                Toast.makeText(this, "tempo invalido", Toast.LENGTH_LONG).show()
            } else {
                resetTime()
                timeLeftTv.text = timeSet.text
                btnStart.text = "Start"
                timeSelected = timeSet.text.toString().toInt()
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timeCounteDown != null) {
            timeCounteDown?.cancel()
            timePrograss = 0
        }
    }

}