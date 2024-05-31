package com.stingach.dm.checksolution

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {
    // UI элементы для отображения результатов и управления процессом
    private lateinit var resultDisplay : TextView
    private lateinit var correctCount : TextView
    private lateinit var wrongCount : TextView
    private lateinit var overallResult : TextView
    private lateinit var one : TextView
    private lateinit var two : TextView
    private lateinit var calculationType : TextView
    private lateinit var inputField : TextView
    private lateinit var submitCorrect : Button
    private lateinit var submitWrong : Button
    private lateinit var initiateCalculation : Button
    private lateinit var minimumTime : TextView
    private lateinit var maximumTime : TextView
    private lateinit var averageTime : TextView

    // Переменные для отслеживания статистики ответов и времени
    private var lowestTime = Int.MAX_VALUE
    private var highestTime = Int.MIN_VALUE
    private var cumulativeTime = 0
    private var successfulAnswers = 0
    private var failedAnswers = 0
    private val possibleOperations = listOf("+", "-", "*", "/")
    private var currentSecond = 0
    private val totalDurationInSeconds = 1000
    private lateinit var timeTracker : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация UI элементов
        resultDisplay = findViewById(R.id.textView1)
        correctCount = findViewById(R.id.textView4)
        wrongCount = findViewById(R.id.textView5)
        overallResult = findViewById(R.id.textView6)
        one = findViewById(R.id.textView7)
        two = findViewById(R.id.textView9)
        calculationType = findViewById(R.id.textView8)
        inputField = findViewById(R.id.editTextText)
        minimumTime = findViewById(R.id.textView30)
        maximumTime = findViewById(R.id.textView31)
        averageTime = findViewById(R.id.textView32)
        submitCorrect = findViewById<Button>(R.id.button1)
        submitWrong = findViewById<Button>(R.id.button2)
        initiateCalculation = findViewById<Button>(R.id.button)

        // Настройка слушателей для кнопок
        submitCorrect.setOnClickListener {
            submitCorrectAction()
        }
        submitWrong.setOnClickListener {
            submitWrongAction()
        }
        initiateCalculation.setOnClickListener {
            initiateCalculationProcess()
        }

        // Отключение кнопок до начала процесса
        submitCorrect.isEnabled = false
        submitWrong.isEnabled = false

        // Инициализация таймера
        timeTracker = object : CountDownTimer(totalDurationInSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                currentSecond++
            }
            override fun onFinish() { }
        }.start()
    }

    // Обработчик нажатия на кнопку "Правильный ответ"
    private fun submitCorrectAction() {
        updateResults()
        submitCorrect.isEnabled = false
        submitWrong.isEnabled = false
        initiateCalculation.isEnabled = true

        val computedAnswer = calculateAnswer().toInt().toString()

        if (inputField.text.toString() == computedAnswer) {
            successfulAnswers++
        } else {
            failedAnswers++
        }

        correctCount.text = successfulAnswers.toString()
        wrongCount.text = failedAnswers.toString()
        overallResult.text = (successfulAnswers + failedAnswers).toString()
        overallResult.text = String.format("%.2f", (successfulAnswers.toDouble() / (successfulAnswers + failedAnswers) * 100)) + "%"
    }

    // Обработчик нажатия на кнопку "Неправильный ответ"
    private fun submitWrongAction() {
        updateResults()
        submitCorrect.isEnabled = false
        submitWrong.isEnabled = false
        initiateCalculation.isEnabled = true

        val computedAnswer = calculateAnswer().toInt().toString()

        if (inputField.text.toString()!= computedAnswer) {
            successfulAnswers++
        } else {
            failedAnswers++
        }

        correctCount.text = successfulAnswers.toString()
        wrongCount.text = failedAnswers.toString()
        overallResult.text = (successfulAnswers + failedAnswers).toString()
        overallResult.text = String.format("%.2f", (successfulAnswers.toDouble() / (successfulAnswers + failedAnswers) * 100)) + "%"
    }

    // Инициирует новый процесс вычисления
    private fun initiateCalculationProcess() {
        initiateCalculation.isEnabled = false
        submitCorrect.isEnabled = true
        submitWrong.isEnabled = true
        inputField.isEnabled = true
        inputField.setBackgroundColor(Color.TRANSPARENT)

        generateNewProblem()

        currentSecond = 0
    }

    // Обновляет статистику времени выполнения
    private fun updateResults() {
        if (currentSecond < lowestTime) {
            lowestTime = currentSecond
        }
        if (currentSecond > highestTime) {
            highestTime = currentSecond
        }
        cumulativeTime += currentSecond

        minimumTime.text = lowestTime.toString()
        maximumTime.text = highestTime.toString()
        averageTime.text = String.format("%.2f", cumulativeTime.toDouble() / (successfulAnswers + failedAnswers))
    }

    // Генерирует новую математическую задачу
    private fun generateNewProblem() {
        one.text = Random.nextInt(10, 100).toString()
        two.text = Random.nextInt(10, 100).toString()
        calculationType.text = possibleOperations[Random.nextInt(0, 4)]

        while (calculationType.text == "/" && one.text.toString().toDouble() % two.text.toString().toDouble()!= 0.0) {
            one.text = Random.nextInt(10, 100).toString()
            two.text = Random.nextInt(10, 100).toString()
        }

        if (Random.nextInt(0, 2) == 1) {
            inputField.text = calculateAnswer().toInt().toString()
        } else {
            inputField.text = Random.nextInt(10, 100).toString()
        }
    }

    // Выполняет указанное математическое действие
    private fun calculateAnswer(): Double {
        when (calculationType.text) {
            "+" -> {
                return one.text.toString().toDouble() + two.text.toString().toDouble()
            }
            "-" -> {
                return one.text.toString().toDouble() - two.text.toString().toDouble()
            }
            "*" -> {
                return one.text.toString().toDouble() * two.text.toString().toDouble()
            }
            "/" -> {
                return one.text.toString().toDouble() / two.text.toString().toDouble()
            }
        }
        return 0.0
    }
}
