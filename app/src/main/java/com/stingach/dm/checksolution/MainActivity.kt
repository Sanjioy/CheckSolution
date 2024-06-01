package com.stingach.dm.checksolution

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stingach.dm.checksolution.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var right = 0
    private var lose = 0
    private var all = 0
    private var startTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loseButton.isEnabled = false
        binding.reightButton.isEnabled = false

        binding.startButton.setOnClickListener {
            button()
            generatePrimer()
        }
    }

    private fun button() {
        binding.loseButton.isEnabled = true
        binding.reightButton.isEnabled = true
        binding.startButton.isEnabled = false
        binding.startButton.visibility = View.GONE
    }

    private fun generatePrimer(){

        val numberOne = Random.nextInt(10, 100)
        val numberTwo = Random.nextInt(10, 100)
        val operators = arrayOf('+', '-', '*', '/')

        val operator = operators.random()

        binding.nullNull1.text = numberOne.toString()
        binding.nullNull2.text = numberTwo.toString()
        binding.znak.text = operator.toString()

        val isCorrect = Random.nextBoolean()

        val correctResult = when (operator) {
            '+' -> numberOne + numberTwo
            '-' -> numberOne - numberTwo
            '*' -> numberOne * numberTwo
            '/' -> {
                val result = numberOne.toDouble() / numberTwo.toDouble()
                String.format("%.2f", result).replace(',', '.').toDouble()
            }
            else -> throw IllegalArgumentException("Unknown operator")
        }
        if (isCorrect) {
            binding.vvvod.text = correctResult.toString()
        } else {
            var wrongResult: Number
            do {
                val randomNumberString = String.format("%.2f", Random.nextDouble(0.1, 10.0)).replace(',', '.')
                wrongResult = when (operator) {
                    '/' -> randomNumberString.toDouble()
                    else -> Random.nextInt(5, 200)
                }
            } while (wrongResult == correctResult)
            binding.vvvod.text = wrongResult.toString()
        }

        proverPrimer(binding.vvvod.text.toString().toDouble(),
            correctResult.toDouble())
    }

    private fun proverPrimer(result: Double, correct: Double) {

        binding.loseButton.setOnClickListener {
            if (result != correct)
                right++
            else
                lose++
            all++
            generatePrimer()
            time()
            voodoo()
        }
        binding.reightButton.setOnClickListener {
            if (result == correct)
                right++
            else
                lose++
            all++
            generatePrimer()
            time()
            voodoo()
        }
    }

    private val timeIntervals = mutableListOf<Long>()
    private fun time(){
        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        startTime = System.currentTimeMillis()
        val seconds = elapsedTime / 1000 % 10

        timeIntervals.add(seconds)

        val maxTime = timeIntervals.maxOrNull() ?: 0
        val minTime = timeIntervals.minOrNull() ?: 0

        binding.maxNull.text = maxTime.toString()
        binding.minNull.text = minTime.toString()

        val averageTime = timeIntervals.average()
        binding.credeeNull.text = String.format("%.2f", averageTime)
    }
    private fun voodoo(){
        binding.itogoNull.text = all.toString()
        binding.rightNull.text = right.toString()
        binding.loseNull.text = lose.toString()

        val present = String.format("%.2f%%", (right.toDouble() / all.toDouble()) * 100)
        binding.prosenttext.text = present
    }
}
