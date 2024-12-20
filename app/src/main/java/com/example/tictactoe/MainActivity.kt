package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
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

        val statusText = findViewById<TextView>(R.id.statusText)
        val restartButton = findViewById<Button>(R.id.restartButton)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (isGameActive && button.text.isEmpty()) {
                    val row = index / 3
                    val col = index % 3
                    board[row][col] = currentPlayer
                    button.text = currentPlayer

                    button.setTextColor(if (currentPlayer == "X") Color.RED else Color.BLUE)

                    val winningButtons = checkWinner(buttons)
                    if (winningButtons.isNotEmpty()) {
                        highlightWinningButtons(winningButtons)
                        statusText.text = "Player $currentPlayer wins!"
                        isGameActive = false
                        restartButton.visibility = View.VISIBLE
                    } else if (isBoardFull()) {
                        statusText.text = "It's a draw!"
                        isGameActive = false
                        restartButton.visibility = View.VISIBLE
                    } else {
                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                        statusText.text = "Player $currentPlayer's turn"
                    }
                }
            }
        }

        restartButton.setOnClickListener {
            restartGame(buttons, statusText)
        }
    }

    private fun highlightWinningButtons(winningButtons: List<Button>) {
        for (button in winningButtons) {
            val animation = AlphaAnimation(0.2f, 1.0f)
            animation.duration = 500
            animation.repeatMode = Animation.REVERSE
            animation.repeatCount = Animation.INFINITE
            button.startAnimation(animation)
            button.setBackgroundColor(Color.YELLOW) // Highlight color
        }
    }

    private fun checkWinner(buttons: List<Button>): List<Button> {
        val winningButtons = mutableListOf<Button>()

        // Check rows for a win
        for (i in 0..2) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                winningButtons.add(buttons[i * 3])
                winningButtons.add(buttons[i * 3 + 1])
                winningButtons.add(buttons[i * 3 + 2])
                return winningButtons
            }
        }

        // Check columns for a win
        for (i in 0..2) {
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                winningButtons.add(buttons[i])
                winningButtons.add(buttons[i + 3])
                winningButtons.add(buttons[i + 6])
                return winningButtons
            }
        }

        // Check diagonals for a win
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            winningButtons.add(buttons[0])
            winningButtons.add(buttons[4])
            winningButtons.add(buttons[8])
            return winningButtons
        }
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            winningButtons.add(buttons[2])
            winningButtons.add(buttons[4])
            winningButtons.add(buttons[6])
            return winningButtons
        }

        return emptyList() // No winner yet
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it != null } }
    }

    private fun restartGame(buttons: List<Button>, statusText: TextView) {
        // Reset the board array to null (empty state)
        board.forEachIndexed { row, cols ->
            cols.fill(null)
        }

        // Reset button texts, animations, and background color
        buttons.forEach {
            it.text = ""  // Clear button text
            it.clearAnimation()  // Stop any ongoing animation
            it.setBackgroundResource(android.R.drawable.btn_default)  // Restore default button background
        }

        // Reset the game state and UI
        currentPlayer = "X"  // Set player X to start
        isGameActive = true  // Make the game active
        statusText.text = "Player X's turn"  // Update status text
        findViewById<Button>(R.id.restartButton).visibility = View.GONE  // Hide restart button
    }
}
