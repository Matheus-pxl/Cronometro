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
                    val timeLeftTv:TextView = findViewById(R.id.text_time_left)
                    timeLeftTv.text = (timeSelected - timePrograss).toString()
                }

                override fun onFinish() {
                    Toast.makeText(this@MainActivity, "tempo acabou!", Toast.LENGTH_SHORT).show()
                }

            }
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
                timeLeftTv.text = timeSet.text
                btnStart.text = "Start"
                timeSelected = timeSet.text.toString().toInt()
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

}