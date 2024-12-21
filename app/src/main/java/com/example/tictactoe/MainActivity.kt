package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var currentPlayer = "X"
    private val board = Array(3) { arrayOfNulls<String>(3) }
    private var isGameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttons = listOf(
            findViewById<Button>(R.id.button_0),
            findViewById<Button>(R.id.button_1),
            findViewById<Button>(R.id.button_2),
            findViewById<Button>(R.id.button_3),
            findViewById<Button>(R.id.button_4),
            findViewById<Button>(R.id.button_5),
            findViewById<Button>(R.id.button_6),
            findViewById<Button>(R.id.button_7),
            findViewById<Button>(R.id.button_8)
        )

        val statusText: TextView = findViewById(R.id.statusText)
        val restartButton: Button = findViewById(R.id.restartButton)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                handleMove(index, button, buttons, statusText, restartButton)
            }
        }

        restartButton.setOnClickListener {
            resetGame(buttons, statusText, restartButton)
        }
    }

    private fun handleMove(index: Int, button: Button, buttons: List<Button>, statusText: TextView, restartButton: Button) {
        if (!isGameActive || button.text.isNotEmpty()) return

        val row = index / 3
        val col = index % 3
        board[row][col] = currentPlayer
        button.text = currentPlayer
        button.setTextColor(if (currentPlayer == "X") Color.RED else Color.BLUE)

        val winningButtons = checkWinner(buttons)
        when {
            winningButtons.isNotEmpty() -> {
                highlightWinningButtons(winningButtons)
                statusText.text = "Player $currentPlayer wins!"
                isGameActive = false
                restartButton.visibility = View.VISIBLE
            }
            isBoardFull() -> {
                statusText.text = "It's a draw!"
                isGameActive = false
                restartButton.visibility = View.VISIBLE
            }
            else -> {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                statusText.text = "Player $currentPlayer's turn"
            }
        }
    }

    private fun highlightWinningButtons(winningButtons: List<Button>) {
        val animation = AlphaAnimation(0.2f, 1.0f).apply {
            duration = 500
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }
        winningButtons.forEach {
            it.startAnimation(animation)
            it.setBackgroundColor(Color.YELLOW)
        }
    }

    private fun checkWinner(buttons: List<Button>): List<Button> {
        val winningLines = listOf(
            listOf(0, 1, 2), // Rows
            listOf(3, 4, 5),
            listOf(6, 7, 8),
            listOf(0, 3, 6), // Columns
            listOf(1, 4, 7),
            listOf(2, 5, 8),
            listOf(0, 4, 8), // Diagonals
            listOf(2, 4, 6)
        )
        for (line in winningLines) {
            if (line.all { board[it / 3][it % 3] == currentPlayer }) {
                return line.map { buttons[it] }
            }
        }
        return emptyList()
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it != null } }
    }

    private fun resetGame(buttons: List<Button>, statusText: TextView, restartButton: Button) {
        board.forEach { row -> row.fill(null) }
        buttons.forEach { button ->
            button.text = ""
            button.clearAnimation()
            button.setBackgroundResource(android.R.drawable.btn_default)
        }
        currentPlayer = "X"
        isGameActive = true
        statusText.text = "Player X's turn"
        restartButton.visibility = View.GONE
    }
}
