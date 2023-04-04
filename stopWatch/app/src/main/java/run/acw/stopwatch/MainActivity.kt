package run.acw.stopwatch

import android.media.AudioManager
import android.media.ToneGenerator
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import run.acw.stopwatch.databinding.ActivityMainBinding
import run.acw.stopwatch.databinding.DialogCountDownBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var countDownValue: Int = 0
    private var countDownValueNow:Int=0
    private var countDownDeciValue = 0
    private var currentDeciSecond = 0
    private var timer: Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            startButton.setOnClickListener {
                start()
                binding.apply {
                    startButton.visibility = View.INVISIBLE
                    stopButton.visibility = View.INVISIBLE
                    pauseButton.visibility = View.VISIBLE
                    lapButton.visibility = View.VISIBLE
                }
            }
            countDownNumber.setOnClickListener {
                showCountDownSettingDialog()
            }
            stopButton.setOnClickListener {
                showStopDialog()

            }
            pauseButton.setOnClickListener {
                pause()
                binding.apply {
                    startButton.visibility = View.VISIBLE
                    stopButton.visibility = View.VISIBLE
                    pauseButton.visibility = View.INVISIBLE
                    lapButton.visibility = View.INVISIBLE
                }
            }
            lapButton.setOnClickListener {
                lap()
                binding.apply {
                    startButton.visibility = View.INVISIBLE
                    stopButton.visibility = View.INVISIBLE
                    pauseButton.visibility = View.VISIBLE
                    lapButton.visibility = View.VISIBLE
                }
            }

        }
        initView()
    }

    private fun initView() {
        binding.countDownNumber.text = String.format("%02d", countDownValue)
        countDownDeciValue=countDownValue*100
    }

    private fun start() {

        timer = timer(initialDelay = 0, period = 100) {
            if ((countDownDeciValue/10) == 0) {

                currentDeciSecond++
                val minutes = currentDeciSecond.div(10) / 60
                val seconds = currentDeciSecond.div(10) % 60
                val deciSeconds = currentDeciSecond % 10
                runOnUiThread {
                    binding.time.text = String.format("%02d:%02d", minutes, seconds)
                    binding.timeTick.text = deciSeconds.toString()
                    binding.countDownGroup.isVisible=false
                }
            } else {
                countDownDeciValue--
                binding.countDownProgress.apply {
                    progress=(((countDownDeciValue/10f)/countDownValue)*100).toInt()
                }
                if(countDownDeciValue<31 && countDownDeciValue%10==0){
                    ToneGenerator(AudioManager.STREAM_ALARM,ToneGenerator.MAX_VOLUME)//볼륨 관련 type설정 현재는 알람으로 설정해서 휴대폰의 알람 음량에 따라 출력될 거임
                        .startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,100)//어떤소리로 출력할지 설정.
                }

                binding.root.post {
                    binding.countDownNumber.text = String.format("%02d", countDownDeciValue/10)
                }
            }

        }


    }

    private fun pause() {
        timer?.cancel()
        timer = null
    }

    private fun stop() {
        binding.apply {
            startButton.visibility = View.VISIBLE
            stopButton.visibility = View.VISIBLE
            pauseButton.visibility = View.INVISIBLE
            lapButton.visibility = View.INVISIBLE


            currentDeciSecond = 0
            time.text = "00:00"
            binding.timeTick.text = "0"
            binding.countDownGroup.isVisible=true
        }

    }

    private fun showCountDownSettingDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("카운트 다운 값 설정")
            val countDownDialogBinding = DialogCountDownBinding.inflate(layoutInflater)
            countDownDialogBinding.numberPicker.apply {
                maxValue = 30
                minValue = 0
                value = countDownValue
            }
            setPositiveButton("확인") { _, _ ->
                countDownValue = countDownDialogBinding.numberPicker.value
                binding.countDownNumber.text = String.format("%02d", countDownValue)
                countDownDeciValue=countDownValue*10
            }
            setNegativeButton("취소", null)
            setView(countDownDialogBinding.root)
        }.show()
    }

    private fun showStopDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("종료?")
            setPositiveButton("yap") { dialog, id ->
                stop()
            }
            setNegativeButton("no", null)
        }.show()
    }

    private fun lap() {
        val container=binding.lapContainer

        TextView(this).apply{
            textSize=20f
            gravity= Gravity.CENTER
            text=binding.time.text
            setPadding(20)
        }.let {
            container.addView(it,0)
        }



    }
}