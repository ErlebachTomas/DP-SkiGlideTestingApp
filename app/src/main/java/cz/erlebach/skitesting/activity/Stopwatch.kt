package cz.erlebach.skitesting.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import cz.erlebach.skitesting.R
import cz.erlebach.skitesting.common.utils.lg
import cz.erlebach.skitesting.databinding.ActivityStopwatchBinding
import java.util.Locale

/**
 * Jednopduché stopky bžící v hlavním vlákně aplikace
 * @property timeTAG Konstanta pro ukládání času časomíry
 * @property format Formátovací řetězec pro zobrazení času časomíry
 * @property timer čas
 * @property isRunning flag zda je časomíra spuštěna
 * @property handler Handler pro manipulaci s časovačem a UI
 */
class Stopwatch : AppCompatActivity() {
    private lateinit var binding: ActivityStopwatchBinding

    companion object {
        const val timeTAG = "stopwatchTimmer"
        const val format = "%d:%02d:%02d.%02d"
    }

    private var timer: Long = 0
    private var isRunning = false
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            timer = savedInstanceState
                .getLong("timer")
            isRunning = savedInstanceState
                .getBoolean("isRunning")
        }
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        binding.swRun.setOnClickListener {

            toggleStopwatch()
        }

        binding.swReset.setOnClickListener {
            resetTimer()
        }

        binding.swOk.setOnClickListener {
            closeAndReturn()
        }

    }

    /** Ovládání stopek */
    private fun toggleStopwatch() {
        if (!isRunning) {
            isRunning = true
            startTimer()
            binding.swRun.text = getString(R.string.stopwatch_stop)

        } else {
            isRunning = false
            stopTimer()
            binding.swRun.text = getString(R.string.stopwatch_start)

        }
    }

    /** Ovládání stopek tlačítkem pro hlasitost */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            toggleStopwatch()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onSaveInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState
            .putLong("timer", timer)
        savedInstanceState
            .putBoolean("isRunning", isRunning)
    }

    private fun resetTimer() {
        isRunning = false
        timer = 0
        binding.swTime.text = java.lang.String.format(
            Locale.getDefault(),
            format, 0, 0, 0, 0
        )
    }

    private fun stopTimer() {
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun startTimer() {
        lg("start timer")

        handler.post(object : Runnable {
            override fun run() {

                val delay: Long = 10
                val ms = ((timer * delay) % 1000) / 10 // výpis pouze 2
                val sec = (timer * delay) / 1000
                val minutes = (sec % 3600) / 60
                val hours = sec / 3600

                val time: String = java.lang.String.format(
                    Locale.getDefault(),
                    format, hours, minutes, sec, ms
                )

                binding.swTime.text = time

                if (isRunning) {
                    timer++
                }

                handler.postDelayed(this, delay)
            }
        })
    }

    /** Návratová funkce s výsledkem */
    private fun closeAndReturn() {
        val intent = Intent()
        intent.putExtra(timeTAG, timer)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


}